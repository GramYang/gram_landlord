package com.gram.gram_landlord.sdk.game.protocols.response;

public class GiveUpLandlordResponse implements Response {
    private int nextLandlordSeatNum;

    public GiveUpLandlordResponse() {
    }

    public GiveUpLandlordResponse(int nextLandlordSeatNum) {
        this.nextLandlordSeatNum = nextLandlordSeatNum;
    }

    public int getNextLandlordSeatNum() {
        return nextLandlordSeatNum;
    }

    public void setNextLandlordSeatNum(int nextLandlordSeatNum) {
        this.nextLandlordSeatNum = nextLandlordSeatNum;
    }
}
