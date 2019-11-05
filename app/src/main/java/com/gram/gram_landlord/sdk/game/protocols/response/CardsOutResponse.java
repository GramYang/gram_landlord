package com.gram.gram_landlord.sdk.game.protocols.response;


import java.util.HashMap;
import java.util.List;

public class CardsOutResponse implements Response {
    private boolean isPass; //要还是不要
    private boolean isAllPass; //我的牌是不是都要不起
    private int fromSeatNum; //出牌人的座位号
    private int toSeatNum; //接牌人的座位号
    private List<Integer> cardsOut; //出的牌
    private HashMap<Integer, Integer> throwOutCards; //实时更新打出去的牌
    private HashMap<Integer, Integer> playersCardsCount; //实时更新三个玩家手中持有的牌数，key是座位号

    public CardsOutResponse() {
    }

    public CardsOutResponse(boolean isPass, boolean isAllPass, int fromSeatNum, int toSeatNum, List<Integer> cardsOut,
                            HashMap<Integer, Integer> throwOutCards, HashMap<Integer, Integer> playersCardsCount) {
        this.isPass = isPass;
        this.isAllPass = isAllPass;
        this.fromSeatNum = fromSeatNum;
        this.toSeatNum = toSeatNum;
        this.cardsOut = cardsOut;
        this.throwOutCards = throwOutCards;
        this.playersCardsCount = playersCardsCount;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public int getFromSeatNum() {
        return fromSeatNum;
    }

    public void setFromSeatNum(int fromSeatNum) {
        this.fromSeatNum = fromSeatNum;
    }

    public int getToSeatNum() {
        return toSeatNum;
    }

    public void setToSeatNum(int toSeatNum) {
        this.toSeatNum = toSeatNum;
    }

    public boolean isAllPass() {
        return isAllPass;
    }

    public void setAllPass(boolean allPass) {
        isAllPass = allPass;
    }

    public List<Integer> getCardsOut() {
        return cardsOut;
    }

    public void setCardsOut(List<Integer> cardsOut) {
        this.cardsOut = cardsOut;
    }

    public HashMap<Integer, Integer> getThrowOutCards() {
        return throwOutCards;
    }

    public void setThrowOutCards(HashMap<Integer, Integer> throwOutCards) {
        this.throwOutCards = throwOutCards;
    }

    public HashMap<Integer, Integer> getPlayersCardsCount() {
        return playersCardsCount;
    }

    public void setPlayersCardsCount(HashMap<Integer, Integer> playersCardsCount) {
        this.playersCardsCount = playersCardsCount;
    }
}
