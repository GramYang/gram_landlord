package com.gram.gram_landlord.sdk.game.protocols.response;

import java.util.HashMap;
import java.util.List;

public class GrabLandlordResponse implements Response {
    private int landlordSeatNum; //地主座位号
    private HashMap<Integer, String> tablePlayers; //座位号-用户名map
    private List<Integer> threeCards; //最后三张牌
    private List<Integer> cards; //每人17张牌

    public GrabLandlordResponse() {
    }

    public GrabLandlordResponse(int landlordSeatNum, HashMap<Integer, String> tablePlayers, List<Integer> threeCards, List<Integer> cards) {
        this.landlordSeatNum = landlordSeatNum;
        this.tablePlayers = tablePlayers;
        this.threeCards = threeCards;
        this.cards = cards;
    }

    public int getLandlordSeatNum() {
        return landlordSeatNum;
    }

    public void setLandlordSeatNum(int landlordSeatNum) {
        this.landlordSeatNum = landlordSeatNum;
    }

    public HashMap<Integer, String> getTablePlayers() {
        return tablePlayers;
    }

    public void setTablePlayers(HashMap<Integer, String> tablePlayers) {
        this.tablePlayers = tablePlayers;
    }

    public List<Integer> getThreeCards() {
        return threeCards;
    }

    public void setThreeCards(List<Integer> threeCards) {
        this.threeCards = threeCards;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }
}
