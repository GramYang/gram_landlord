package com.gram.gram_landlord.sdk.assistant.entity;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private String key;
    private int code;
    private String message;
    private Map<String, String> data;
    private byte[] picture;

    public Response() {
        data = new HashMap<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getPiture() {
        return picture;
    }

    public void setPiture(byte[] piture) {
        this.picture = piture;
    }

    public void put(String k, String v) {
        if(v != null && k != null) data.put(k, v);
    }

    public void putAll(Map<String, String> map) {data.putAll(map);}

    public Map<String, String> getAll() {return data;}

    public String get(String k) {return data.get(k);}
}
