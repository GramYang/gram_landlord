package com.gram.gram_landlord.sdk.game;

import com.google.gson.Gson;
import com.gram.gram_landlord.sdk.Action;
import com.gram.gram_landlord.sdk.Constants;
import com.gram.gram_landlord.sdk.game.protocols.request.Request;
import com.orhanobut.logger.Logger;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class GameClient {
    //连接超时10秒
    private final static int CONNECT_TIMEOUT = 10 * 1000;
    //信号量屏蔽多次连接
    private Semaphore semaphore = new Semaphore(1,true);
    private static GameClient client;
    private Bootstrap bootstrap;
    private Channel channel;
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private Gson gson;

    private GameClient() {
        gson = new Gson();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        ByteBuf delimiter = Unpooled.copiedBuffer(Constants.LINE_SEPARATOR.getBytes());
                        pipeline.addLast(new DelimiterBasedFrameDecoder(10240, delimiter));
                        pipeline.addLast(new IdleStateHandler(60, 60, 0));
                        pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
                        pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
                        pipeline.addLast(new GameClientHandler());
//                        SSLEngine engine = ContextSSLFactory.getInstance().getClientSslContext().createSSLEngine();
//                        engine.setUseClientMode(true);
//                        pipeline.addFirst(new SslHandler(engine, true));
                    }
                });
    }

    public synchronized static GameClient getClient() {
        if(client == null) client = new GameClient();
        return client;
    }

    public void connectServer() {
        if(isConnected() || !semaphore.tryAcquire()) return;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Logger.i("正在连接游戏服务器" + Constants.gameHost + ":" + Constants.gamePort + ".......");
                InetSocketAddress remoteAddress = new InetSocketAddress(Constants.gameHost, Constants.gamePort);
                bootstrap.connect(remoteAddress).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        semaphore.release();
                        future.removeListener(this);
                        if(!future.isSuccess() && future.cause() != null) handleConnectFailure(future.cause(), remoteAddress);
                        if(future.isSuccess()) {
                            Logger.w("服务器连接成功" + remoteAddress.getHostName() + ":" + remoteAddress.getPort());
                            channel = future.channel();
                        }
                    }
                });
            }
        });
    }

    public boolean isConnected() {
        return channel != null && channel.isActive();
    }

    private void handleConnectFailure(Throwable throwable, InetSocketAddress inetSocketAddress) {
        Action action = new Action(Action.ACTION_CONNECTION_FAILURE);
        action.putData(Throwable.class.getName(), throwable);
        action.putData("interval", Constants.RECONN_INTERVAL_TIME);
        sendAction(action);
        Logger.w("服务器连接失败 " + inetSocketAddress.getHostName() + ":" + inetSocketAddress.getPort() +
                "......将在" + Constants.RECONN_INTERVAL_TIME / 1000 + "秒后重新尝试连接");
    }

    void sendAction(Action action) {
        executor.execute(() ->
            GameHandler.getHandler().onHandle(action)
        );
    }



    public void send(Request request) {
        if(channel != null && channel.isActive()) channel.writeAndFlush
                (request.getClass().getSimpleName() + gson.toJson(request)
                        + Constants.LINE_SEPARATOR)
                .addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                future.removeListener(this);
                if(future.isSuccess()) {
                    Action action = new Action(Action.ACTION_SEND_SUCCESSFUL);
                    action.putData("request", request);
                    sendAction(action);
                } else {
                    Logger.e("发送信息失败：" + future.cause().getMessage());
                    Action action = new Action(Action.ACTION_SEND_FAILED);
                    action.putData("request", request);
                    sendAction(action);
                }
            }
        });
    }
}
