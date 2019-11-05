package com.gram.gram_landlord.sdk;

import com.gram.gram_landlord.R;

import java.util.HashMap;

public interface Constants {
    String gameHost = "192.168.101.5";
    int gamePort = 6789;
    String assistHost = "192.168.101.5";
    int assistPort = 6790;
    String LINE_SEPARATOR = "\r\n";
    long RECONN_INTERVAL_TIME = 30 * 1000;//重连时间
    int DATA_HEADER_LENGTH = 4;

    interface DataType {
        byte REQUEST = 1;
        byte RESPONSE = 2;
    }

    HashMap<Integer, String> cardsName = new HashMap<Integer, String>(54) {
        {
            put(12, "cluba"); put(13, "club2"); put(1, "club3"); put(2, "club4"); put(3, "club5");
            put(4, "club6"); put(5, "club7"); put(6, "club8"); put(7, "club9"); put(8, "club10");
            put(9, "clubj"); put(10, "clubq"); put(11, "clubk");
            put(32, "spadea"); put(33, "spade2"); put(21, "spade3"); put(22, "spade4"); put(23, "spade5");
            put(24, "spade6"); put(25, "spade7"); put(26, "spade8"); put(27, "spade9"); put(28, "spade10");
            put(29, "spadej"); put(30, "spadeq"); put(31, "spadek");
            put(52, "diamonda"); put(53, "diamond2"); put(41, "diamond3"); put(42, "diamond4"); put(43, "diamond5");
            put(44, "diamond6"); put(45, "diamond7"); put(46, "diamond8"); put(47, "diamond9"); put(48, "diamond10");
            put(49, "diamondj"); put(50, "diamondq"); put(51, "diamondk");
            put(72, "hearta"); put(73, "heart2"); put(61, "heart3"); put(62, "heart4"); put(63, "heart5");
            put(64, "heart6"); put(65, "heart7"); put(66, "heart8"); put(67, "heart9"); put(68, "heart10");
            put(69, "heartj"); put(70, "heartq"); put(71, "heartk");
            put(74, "joker1"); put(75, "joker2");
        }
    };

    HashMap<Integer, Integer> cardsResource = new HashMap<Integer, Integer>(54) {
        {
            put(12, R.mipmap.cluba); put(13, R.mipmap.club2); put(1, R.mipmap.club3); put(2, R.mipmap.club4);
            put(3, R.mipmap.club5); put(4, R.mipmap.club6); put(5, R.mipmap.club7); put(6, R.mipmap.club8);
            put(7, R.mipmap.club9); put(8, R.mipmap.club10); put(9, R.mipmap.clubj); put(10, R.mipmap.clubq);
            put(11, R.mipmap.clubk);
            put(32, R.mipmap.spadea); put(33, R.mipmap.spade2); put(21, R.mipmap.spade3); put(22, R.mipmap.spade4);
            put(23, R.mipmap.spade5); put(24, R.mipmap.spade6); put(25, R.mipmap.spade7); put(26, R.mipmap.spade8);
            put(27, R.mipmap.spade9); put(28, R.mipmap.spade10); put(29, R.mipmap.spadej); put(30, R.mipmap.spadeq);
            put(31, R.mipmap.spadek);
            put(52, R.mipmap.diamonda); put(53, R.mipmap.diamond2); put(41, R.mipmap.diamond3); put(42, R.mipmap.diamond4);
            put(43, R.mipmap.diamond5); put(44, R.mipmap.diamond6); put(45, R.mipmap.diamond7); put(46, R.mipmap.diamond8);
            put(47, R.mipmap.diamond9); put(48, R.mipmap.diamond10); put(49, R.mipmap.diamondj); put(50, R.mipmap.diamondq);
            put(51, R.mipmap.diamondk);
            put(72, R.mipmap.hearta); put(73, R.mipmap.heart2); put(61, R.mipmap.heart3); put(62, R.mipmap.heart4);
            put(63, R.mipmap.heart5); put(64, R.mipmap.heart6); put(65, R.mipmap.heart7); put(66, R.mipmap.heart8);
            put(67, R.mipmap.heart9); put(68, R.mipmap.heart10); put(69, R.mipmap.heartj); put(70, R.mipmap.heartq);
            put(71, R.mipmap.heartk);
            put(74, R.mipmap.joker1); put(75, R.mipmap.joker2);
        }
    };

}
