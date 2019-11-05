package com.gram.gram_landlord.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.gram.gram_landlord.R;
import com.gram.gram_landlord.sdk.assistant.AssistClient;
import com.gram.gram_landlord.sdk.assistant.RequestKey;
import com.gram.gram_landlord.sdk.assistant.entity.Request;
import com.gram.gram_landlord.utils.SharedPreferencesUtil;

/**
 * 游戏结束后的弹窗
 */
public class EndGamePopupWindow extends PopupWindow {
    private View rootView;
    private boolean isLandlordWin;
    private boolean isYouWin;
    private int doubleNum;
    private long yourMoney;

    public EndGamePopupWindow(View contentView, int width, int height,
                              boolean isLandlordWin, boolean isYouWin, int doubleNum, String yourMoney) {
        super(contentView, width, height);
        setContentView(contentView);
        this.rootView = contentView;
        this.isLandlordWin = isLandlordWin;
        this.isYouWin = isYouWin;
        this.doubleNum = doubleNum;
        this.yourMoney = Long.valueOf(yourMoney);
        init();
    }

    private void init() {
        TextView landlordWin = rootView.findViewById(R.id.landlord_win);
        TextView farmerWin = rootView.findViewById(R.id.farmer_win);
        TextView youWin = rootView.findViewById(R.id.is_you_win);
        TextView money1 = rootView.findViewById(R.id.money_result1);
        TextView money2 = rootView.findViewById(R.id.money_result2);
        Button confirm = rootView.findViewById(R.id.confirm);
        if(isLandlordWin) {
            landlordWin.setVisibility(View.VISIBLE);
            farmerWin.setVisibility(View.GONE);
        } else {
            landlordWin.setVisibility(View.GONE);
            farmerWin.setVisibility(View.VISIBLE);
        }
        long result;
        Request request = new Request();
        if(isYouWin) {
            if(isLandlordWin) result = doubleNum * 100 * 2;
            else result = doubleNum * 100;
            request.setKey(RequestKey.PLAYER_WIN);
            request.put("username", SharedPreferencesUtil.getUsername());
            request.put("password", SharedPreferencesUtil.getEncryptPassword());
            request.put("money", String.valueOf(result));
        } else {
            youWin.setText("你输了");
            if(isLandlordWin) result = doubleNum * 100 * -2;
            else result = doubleNum * -100;
            request.setKey(RequestKey.PLAYER_LOSE);
            request.put("username", SharedPreferencesUtil.getUsername());
            request.put("password", SharedPreferencesUtil.getEncryptPassword());
            request.put("money", String.valueOf(-result));
        }
        AssistClient.getClient().send(request);
        money1.append(result + "");
        yourMoney = yourMoney + result;
        SharedPreferencesUtil.saveMoney(yourMoney + "");
        money2.append(yourMoney + "");
        confirm.setOnClickListener(v -> dismiss());
    }
}
