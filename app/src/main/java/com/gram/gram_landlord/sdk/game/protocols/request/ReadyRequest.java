package com.gram.gram_landlord.sdk.game.protocols.request;

public class ReadyRequest implements Request {
    private boolean ready;

    public ReadyRequest() {
    }

    public ReadyRequest(boolean isReady) {
        this.ready = isReady;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
