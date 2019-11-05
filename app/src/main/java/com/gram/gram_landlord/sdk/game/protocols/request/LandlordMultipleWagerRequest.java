package com.gram.gram_landlord.sdk.game.protocols.request;

public class LandlordMultipleWagerRequest implements Request {
    private int landlordSeatNum;
    private int multipleNum;

    public LandlordMultipleWagerRequest() {
    }

    public LandlordMultipleWagerRequest(int landlordSeatNum, int multipleNum) {
        this.landlordSeatNum = landlordSeatNum;
        this.multipleNum = multipleNum;
    }

    public int getLandlordSeatNum() {
        return landlordSeatNum;
    }

    public void setLandlordSeatNum(int landlordSeatNum) {
        this.landlordSeatNum = landlordSeatNum;
    }

    public int getMultipleNum() {
        return multipleNum;
    }

    public void setMultipleNum(int multipleNum) {
        this.multipleNum = multipleNum;
    }
}
