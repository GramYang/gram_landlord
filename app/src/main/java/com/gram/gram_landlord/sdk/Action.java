package com.gram.gram_landlord.sdk;

import java.util.HashMap;

public class Action {
    private int action;
    private HashMap<String, Object> data;

    public static final int ACTION_CONNECTION_SUCCESSFUL = 1;
    public static final int ACTION_CONNECTION_FAILURE = 2;
    public static final int ACTION_RESPONSE_RECEIVED = 3;
    public static final int ACTION_CONNECTION_EXCEPTION = 4;
    public static final int ACTION_CONNECTION_CLOSED = 5;
    public static final int ACTION_SEND_SUCCESSFUL = 6;
    public static final int ACTION_SEND_FAILED = 7;

    public Action(int action) {
        this.action = action;
        data = new HashMap<>();
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void putData(String key, Object value) {data.put(key, value);}

    public Object getData(String key) {
        return data.get(key);
    }

    public long getLongExtra(String key) {
        Object v = getData(key);
        try {
            return Long.parseLong(v.toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
