package com.gram.gram_landlord.sdk.game.protocols.request;

public class EndGrabLandlordRequest implements Request {
    private int meSeatNum;

    public EndGrabLandlordRequest() {
    }

    public EndGrabLandlordRequest(int meSeatNum) {
        this.meSeatNum = meSeatNum;
    }

    public int getMeSeatNum() {
        return meSeatNum;
    }

    public void setMeSeatNum(int meSeatNum) {
        this.meSeatNum = meSeatNum;
    }
}
