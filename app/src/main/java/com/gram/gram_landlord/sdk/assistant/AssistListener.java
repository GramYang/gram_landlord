package com.gram.gram_landlord.sdk.assistant;

import com.gram.gram_landlord.sdk.assistant.entity.Response;

import io.netty.channel.ChannelHandlerContext;

public interface AssistListener {
    void onConnectionSuccessful(ChannelHandlerContext ctx);

    void onConnectionFailure();

    void onConnectionException(Throwable e);

    void onConnectionClosed();

    void onRequestSendSuccess();

    void onRequestSendFailed();

    void onResponseReceived(Response response);
}
