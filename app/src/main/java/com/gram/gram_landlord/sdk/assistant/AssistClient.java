package com.gram.gram_landlord.sdk.assistant;

import com.gram.gram_landlord.sdk.Action;
import com.gram.gram_landlord.sdk.Constants;
import com.gram.gram_landlord.sdk.assistant.entity.Request;
import com.gram.gram_landlord.sdk.assistant.proto.AssistantProto;
import com.orhanobut.logger.Logger;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GenericFutureListener;

public class AssistClient {
    //连接超时10秒
    private final static int CONNECT_TIMEOUT = 10 * 1000;
    //信号量屏蔽多次连接
    private Semaphore semaphore = new Semaphore(1,true);
    private static AssistClient client;
    private Bootstrap bootstrap;
    private Channel channel;
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private AssistClientHandler handler;
    private NioEventLoopGroup work;

    private AssistClient() {
        handler = new AssistClientHandler();
        bootstrap = new Bootstrap();
        work = new NioEventLoopGroup();
        bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new IdleStateHandler(120, 0, 0));
                        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        ch.pipeline().addLast(new ProtobufDecoder(AssistantProto.Response.getDefaultInstance()));
                        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        ch.pipeline().addLast(new ProtobufEncoder());
                        ch.pipeline().addLast(handler);
//                        SSLEngine engine = ContextSSLFactory.getInstance().getClientSslContext().createSSLEngine();
//                        engine.setUseClientMode(true);
//                        ch.pipeline().addFirst(new SslHandler(engine));
                    }
                });
    }

    public synchronized static AssistClient getClient() {
        if(client == null) client = new AssistClient();
        return client;
    }

    public void connectServer() {
        if(!semaphore.tryAcquire()) return;
        executor.execute(() -> {
            Logger.i("正在连接Assist服务器" + Constants.assistHost + ":" + Constants.gamePort + "......");
            InetSocketAddress address = new InetSocketAddress(Constants.assistHost, Constants.assistPort);
            if(bootstrap != null) bootstrap.connect(address).addListener(new GenericFutureListener<ChannelFuture>() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    semaphore.release();
                    future.removeListener(this);
                    if(!future.isSuccess() && future.cause() != null) handleConnectionFailure(future.cause(), address);
                    if(future.isSuccess()) {
                        channel = future.channel();
                    }
                }
            });
        });
    }

    private boolean isConnected() {return channel != null && channel.isActive();}

    private void handleConnectionFailure(Throwable error, InetSocketAddress address) {
        Action action = new Action(Action.ACTION_CONNECTION_FAILURE);
        action.putData(Throwable.class.getName(), error);
        action.putData("interval", Constants.RECONN_INTERVAL_TIME);
        sendAction(action);
        Logger.w("服务器连接失败 " + address.getHostName() + ":" + address.getPort() +
                "......将在" + Constants.RECONN_INTERVAL_TIME / 1000 + "秒后重新尝试连接");
    }

    void sendAction(Action action) {
        executor.execute(() ->
                AssistHandler.getHandler().onHandle(action)
        );
    }

    public void send(Request request) {
        if(!isConnected()) connectServer();
        handler.setRequest(request);
    }

    void destroy() {
        channel.close();
        work.shutdownGracefully();
        bootstrap = null;
    }
}
