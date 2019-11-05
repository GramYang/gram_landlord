package com.gram.gram_landlord.sdk.assistant.entity;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String key;
    private Map<String, String> data;
    private byte[] picture;

    public Request() {
        data = new HashMap<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String get(String k) {return data.get(k);}

    public void put(String k, String v) {
        if(v != null && k != null) data.put(k, v);
    }

    public void putAll(Map<String, String> map) {data.putAll(map);}

    public Map<String, String> getAll() {return data;}
}
