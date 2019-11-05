package com.gram.gram_landlord.sdk.game.protocols.response;

/**
 * 登录反馈
 */
public class LoginResponse implements Response{

    private String username;//登录的用户名
    private boolean isSuccessFul;//登录是否成功
    private String responseMsg;

    public LoginResponse() {
    }

    public LoginResponse(String username, boolean isSuccessFul, String responseMsg) {
        this.username = username;
        this.isSuccessFul = isSuccessFul;
        this.responseMsg = responseMsg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isSuccessFul() {
        return isSuccessFul;
    }

    public void setSuccessFul(boolean successFul) {
        isSuccessFul = successFul;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
