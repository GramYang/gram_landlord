package com.gram.gram_landlord.sdk.game;

import com.gram.gram_landlord.sdk.Action;
import com.gram.gram_landlord.sdk.Constants;
import com.gram.gram_landlord.sdk.game.protocols.HeartBeatStop;
import com.orhanobut.logger.Logger;
import org.greenrobot.eventbus.EventBus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

public class GameClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Logger.i("从服务器收到消息：" + msg);
        if(msg.contains("HeartBeatResponse")) {
            ctx.channel().attr(AttributeKey.valueOf("heartbeat")).set(System.currentTimeMillis());
            ctx.writeAndFlush("HeartBeatRequest" + Constants.LINE_SEPARATOR);
        } else {
            Action action = new Action(Action.ACTION_RESPONSE_RECEIVED);
            action.putData("response", msg);
            GameClient.getClient().sendAction(action);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Logger.e("客户端信息接收线程出现异常：" + ctx.channel().localAddress() +
                " NID:" + ctx.channel().id().asShortText());
        if(cause != null) cause.printStackTrace();
        Action action = new Action(Action.ACTION_CONNECTION_EXCEPTION);
        action.putData("throwable",cause);
        GameClient.getClient().sendAction(action);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Logger.i("连接服务器成功：" + ctx.channel().localAddress() + " NID:"
                + ctx.channel().id().asShortText());
        ctx.channel().attr(AttributeKey.valueOf("heartbeat")).set(System.currentTimeMillis());
        Action action = new Action(Action.ACTION_CONNECTION_SUCCESSFUL);
        action.putData("channel", ctx.channel());
        GameClient.getClient().sendAction(action);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Logger.i("与服务器断开连接:" + ctx.channel().localAddress() + " NID:"
                + ctx.channel().id().asShortText());
        Action action = new Action(Action.ACTION_CONNECTION_CLOSED);
        GameClient.getClient().sendAction(action);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state().equals(IdleState.READER_IDLE)) {
            Logger.d("读超时" + IdleState.READER_IDLE + ":" + ctx.channel().localAddress() + " NID:"
                    + ctx.channel().id().asShortText());
            if(System.currentTimeMillis() - (Long)ctx.channel().attr(AttributeKey.valueOf("heartbeat")).get() >= 120000L) {
                EventBus.getDefault().post(new HeartBeatStop());
                ctx.close();
                GameClient.getClient().connectServer();
            }
        }
    }

}
