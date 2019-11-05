package com.gram.gram_landlord.sdk.game.protocols.response;

public class CancelReadyResponse implements Response {
    private boolean isCancelReady;

    public CancelReadyResponse() {
    }

    public CancelReadyResponse(boolean isCancelReady) {
        this.isCancelReady = isCancelReady;
    }

    public boolean isCancelReady() {
        return isCancelReady;
    }

    public void setCancelReady(boolean cancelReady) {
        isCancelReady = cancelReady;
    }
}
