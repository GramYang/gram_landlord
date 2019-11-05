package com.gram.gram_landlord.sdk.game.protocols.request;

public class ExitSeatRequest implements Request {
    private int yourSeatNum;

    public ExitSeatRequest() {
    }

    public ExitSeatRequest(int yourSeatNum) {
        this.yourSeatNum = yourSeatNum;
    }

    public int getYourSeatNum() {
        return yourSeatNum;
    }

    public void setYourSeatNum(int yourSeatNum) {
        this.yourSeatNum = yourSeatNum;
    }
}
