package com.gram.gram_landlord.api;

import com.gram.gram_landlord.sdk.assistant.AssistListener;
import com.gram.gram_landlord.sdk.assistant.entity.Response;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import io.netty.channel.ChannelHandlerContext;

public class AssistListenerImpl implements AssistListener {
    @Override
    public void onConnectionSuccessful(ChannelHandlerContext ctx) {

    }

    @Override
    public void onConnectionFailure() {

    }

    @Override
    public void onConnectionException(Throwable e) {

    }

    @Override
    public void onConnectionClosed() {

    }

    @Override
    public void onRequestSendSuccess() {

    }

    @Override
    public void onRequestSendFailed() {
        Logger.e("Assist Request发送失败");
    }

    @Override
    public void onResponseReceived(Response response) {
        EventBus.getDefault().post(response);
    }
}
