package com.gram.gram_landlord.sdk.assistant;

public interface RequestKey {
    //获取玩家全部信息
    String GET_PLAYER_INFO = "get_player_info";
    //玩家赢了一盘，获得xxx积分
    String PLAYER_WIN = "player_win";
    //玩家输了一盘，扣掉xxx积分
    String PLAYER_LOSE = "player_lost";
    //上传头像
    String UPDATE_AVATAR = "update_avatar";
}
