package com.gram.gram_landlord.sdk.game.protocols.request;

public class CancelReadyRequest implements Request {
    private boolean isCancelReady;

    public CancelReadyRequest(boolean isCancelReady) {
        this.isCancelReady = isCancelReady;
    }

    public boolean isCancelReady() {
        return isCancelReady;
    }
}
