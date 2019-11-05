package com.gram.gram_landlord.sdk.game.protocols.response;

public class EndGameResponse implements Response {
    private int winnerSeatNum;

    public EndGameResponse() {
    }

    public EndGameResponse(int winnerSeatNum) {
        this.winnerSeatNum = winnerSeatNum;
    }

    public int getWinnerSeatNum() {
        return winnerSeatNum;
    }

    public void setWinnerSeatNum(int winnerSeatNum) {
        this.winnerSeatNum = winnerSeatNum;
    }
}
