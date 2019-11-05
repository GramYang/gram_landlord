package com.gram.gram_landlord.sdk.assistant;

import com.gram.gram_landlord.sdk.Action;
import com.gram.gram_landlord.sdk.assistant.entity.Response;
import com.orhanobut.logger.Logger;
import java.util.Timer;
import java.util.TimerTask;
import io.netty.channel.ChannelHandlerContext;

public class AssistHandler {
    private static AssistHandler handler;
    private AssistListener listener;
    private Timer timer = new Timer();

    private AssistHandler() {}

    public static AssistHandler getHandler() {
        if(handler == null) handler = new AssistHandler();
        return handler;
    }

    public void setListener(AssistListener listener) {
        this.listener = listener;
    }

    public void onHandle(Action action) {
        //连接成功
        if(action.getAction() == Action.ACTION_CONNECTION_SUCCESSFUL) {
            ChannelHandlerContext ctx = (ChannelHandlerContext) action.getData("ctx");
            listener.onConnectionSuccessful(ctx);
        }

        //连接失败后定时重连
        if(action.getAction() == Action.ACTION_CONNECTION_FAILURE) {
            long interval = action.getLongExtra("interval");
            timer.schedule(new ConnectionTask(), interval);
            listener.onConnectionFailure();
        }

        //接收服务器反馈
        if(action.getAction() == Action.ACTION_RESPONSE_RECEIVED) {
            Logger.i("收到服务器返回Response");
            listener.onResponseReceived((Response) action.getData(Response.class.getName()));
        }

        //连接异常
        if(action.getAction() == Action.ACTION_CONNECTION_EXCEPTION) {
            listener.onConnectionException((Throwable) action.getData("throwable"));
        }

        //连接关闭
        if(action.getAction() == Action.ACTION_CONNECTION_CLOSED) {
            listener.onConnectionClosed();
        }

        //发送成功
        if(action.getAction() == Action.ACTION_SEND_SUCCESSFUL) {
            Logger.i("请求：" + action.getData("request") + "发送成功");
            listener.onRequestSendSuccess();
        }

        //发送失败
        if(action.getAction() == Action.ACTION_SEND_FAILED) {
            Logger.i("请求：" + action.getData("request") + "发送失败");
            listener.onRequestSendFailed();
        }
    }

    class ConnectionTask extends TimerTask {
        public void run() {
            AssistClient.getClient().connectServer();}
    }
}
