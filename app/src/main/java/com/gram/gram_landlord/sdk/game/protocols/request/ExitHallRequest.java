package com.gram.gram_landlord.sdk.game.protocols.request;

public class ExitHallRequest implements Request {
    private String userName;

    public ExitHallRequest() {
    }

    public ExitHallRequest(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
