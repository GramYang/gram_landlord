package com.gram.gram_landlord.sdk.game.protocols.request;

public class GiveUpLandlordRequest implements Request {
    private int seatNum;

    public GiveUpLandlordRequest() {
    }

    public GiveUpLandlordRequest(int seatNum) {
        this.seatNum = seatNum;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }
}
