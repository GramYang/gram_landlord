package com.gram.gram_landlord.sdk.game;


import io.netty.channel.Channel;

public interface GameListener {

    void onConnectionSuccessful(Channel channel);

    void onConnectionFailure();

    void onConnectionException(Throwable e);

    void onConnectionClosed();

    void onRequestSendSuccess();

    void onRequestSendFailed();

    void onResponseReceived(Object object);
}
