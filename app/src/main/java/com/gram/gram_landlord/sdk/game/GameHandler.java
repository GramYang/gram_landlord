package com.gram.gram_landlord.sdk.game;

import com.google.gson.Gson;
import com.gram.gram_landlord.sdk.Action;
import com.orhanobut.logger.Logger;
import java.util.Timer;
import java.util.TimerTask;

import io.netty.channel.Channel;

public class GameHandler {
    private static GameHandler handler;
    private GameListener listener;
    private Timer timer;
    private Gson gson;

    private static final String RESPONSE_PATH = "com.gram.gram_landlord.sdk.game.protocols.response.";
    private static final String REQUEST_PATH = "com.gram.gram_landlord.sdk.game.protocols.request.";

    private GameHandler() {
        gson = new Gson();
        timer = new Timer();
    }

    public static GameHandler getHandler() {
        if(handler == null) handler = new GameHandler();
        return handler;
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public void onHandle(Action action) {
        //连接成功
        if(action.getAction() == Action.ACTION_CONNECTION_SUCCESSFUL) {
            Channel channel = (Channel) action.getData("channel");
            listener.onConnectionSuccessful(channel);
        }

        //连接失败后定时重连
        if(action.getAction() == Action.ACTION_CONNECTION_FAILURE) {
            long interval = action.getLongExtra("interval");
            timer.schedule(new ConnectionTask(), interval);
            listener.onConnectionFailure();
        }

        //接收服务器反馈
        if(action.getAction() == Action.ACTION_RESPONSE_RECEIVED) {
            String msg = (String) action.getData("response");
            int endIndex = msg.indexOf("{");
            if(endIndex > 0 ) {
                String className = RESPONSE_PATH + msg.substring(0, endIndex);
                String classContent = msg.substring(endIndex);
                Class<?> class1 = null;
                try {
                    class1 = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(class1 != null) listener.onResponseReceived(gson.fromJson(classContent, class1));
            }
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
            GameClient.getClient().connectServer();}
    }
}
