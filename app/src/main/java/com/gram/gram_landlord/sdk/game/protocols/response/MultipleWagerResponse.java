package com.gram.gram_landlord.sdk.game.protocols.response;

public class MultipleWagerResponse implements Response {
    private int multipleNum;

    public MultipleWagerResponse() {
    }

    public MultipleWagerResponse(int multipleNum) {
        this.multipleNum = multipleNum;
    }

    public int getMultipleNum() {
        return multipleNum;
    }

    public void setMultipleNum(int multipleNum) {
        this.multipleNum = multipleNum;
    }
}
