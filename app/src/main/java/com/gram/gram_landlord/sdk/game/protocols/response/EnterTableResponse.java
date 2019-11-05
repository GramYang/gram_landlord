package com.gram.gram_landlord.sdk.game.protocols.response;



import java.util.HashMap;

public class EnterTableResponse implements Response {
    private boolean isSuccess;
    private HashMap<Integer, String> tablePlayers;

    public EnterTableResponse() {
    }

    public EnterTableResponse(boolean isSuccess, HashMap<Integer, String> tablePlayers) {
        this.isSuccess = isSuccess;
        this.tablePlayers = tablePlayers;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public HashMap<Integer, String> getTablePlayers() {
        return tablePlayers;
    }

    public void setTablePlayers(HashMap<Integer, String> tablePlayers) {
        this.tablePlayers = tablePlayers;
    }
}
