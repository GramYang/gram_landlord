package com.gram.gram_landlord.api;

import com.gram.gram_landlord.sdk.game.GameListener;
import com.gram.gram_landlord.sdk.game.protocols.response.CancelReadyResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.CardsOutResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.ChatMsgResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.EndGameResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.EndGrabLandlordResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.EnterTableResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.ExitSeatResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.GiveUpLandlordResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.GrabLandlordResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.InitHallResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.LandlordMultipleWagerResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.LoginResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.MultipleWagerResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.ReadyResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.RefreshHallResponse;
import com.gram.gram_landlord.sdk.game.protocols.response.Response;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import io.netty.channel.Channel;

public class GameListenerImpl implements GameListener {

    @Override
    public void onConnectionSuccessful(Channel channel) {

    }

    @Override
    public void onConnectionFailure() {

    }

    @Override
    public void onConnectionException(Throwable e) {

    }

    @Override
    public void onConnectionClosed() {

    }

    @Override
    public void onRequestSendSuccess() {

    }

    @Override
    public void onRequestSendFailed() {
        Logger.w("Request发送失败");
    }

    @Override
    public void onResponseReceived(Object object) {
        if(object instanceof CancelReadyResponse) {
            Response response = (CancelReadyResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof CardsOutResponse) {
            Response response = (CardsOutResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof ChatMsgResponse) {
            Response response = (ChatMsgResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof EndGameResponse) {
            Response response = (EndGameResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof EndGrabLandlordResponse) {
            Response response = (EndGrabLandlordResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof EnterTableResponse) {
            Response response = (EnterTableResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof ExitSeatResponse) {
            Response response = (ExitSeatResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof GiveUpLandlordResponse) {
            Response response = (GiveUpLandlordResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof GrabLandlordResponse) {
            Response response = (GrabLandlordResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof InitHallResponse) {
            Response response = (InitHallResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof LandlordMultipleWagerResponse) {
            Response response = (LandlordMultipleWagerResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof LoginResponse) {
            Response response = (LoginResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof MultipleWagerResponse) {
            Response response = (MultipleWagerResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof ReadyResponse) {
            Response response = (ReadyResponse) object;
            EventBus.getDefault().postSticky(response);
        }
        if(object instanceof RefreshHallResponse) {
            Response response = (RefreshHallResponse) object;
            EventBus.getDefault().postSticky(response);
        }
    }
}
