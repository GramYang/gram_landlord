package com.gram.gram_landlord.sdk.game.protocols.response;

public class LandlordMultipleWagerResponse implements Response {
    private int multipleNum;

    public LandlordMultipleWagerResponse() {
    }

    public LandlordMultipleWagerResponse(int multipleNum) {
        this.multipleNum = multipleNum;
    }

    public int getMultipleNum() {
        return multipleNum;
    }

    public void setMultipleNum(int multipleNum) {
        this.multipleNum = multipleNum;
    }
}
