package com.gram.gram_landlord.sdk.game.protocols.response;

import com.gram.gram_landlord.sdk.game.entity.HallTable;

import java.util.ArrayList;

/**
 * 初始化大厅座位信息
 */
public class InitHallResponse implements Response {
    private ArrayList<HallTable> hallTables;

    public InitHallResponse() {
        this.hallTables = new ArrayList<>();
    }

    public ArrayList<HallTable> getTableList() {
        return hallTables;
    }

    public void setTableList(ArrayList<HallTable> hallTables) {
        this.hallTables = hallTables;
    }
}
