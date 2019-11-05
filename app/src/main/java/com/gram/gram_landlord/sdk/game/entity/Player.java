package com.gram.gram_landlord.sdk.game.entity;


import java.util.ArrayList;

import io.netty.channel.Channel;

public class Player {
    private transient Channel channel;
    //用户名
    private String userName;
    //座位号
    private int seatNum = -1;
    //发的牌
    private ArrayList<Integer> cards;
    //入座牌桌号，-1代表没有入座
    private int tableNum = -1;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public ArrayList<Integer> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Integer> cards) {
        this.cards = cards;
    }
}
