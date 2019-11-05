package com.gram.gram_landlord;

import android.app.Application;
import com.gram.gram_landlord.api.AssistListenerImpl;
import com.gram.gram_landlord.api.GameListenerImpl;
import com.gram.gram_landlord.sdk.assistant.AssistClient;
import com.gram.gram_landlord.sdk.assistant.AssistHandler;
import com.gram.gram_landlord.sdk.game.GameClient;
import com.gram.gram_landlord.sdk.game.GameHandler;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.JdkLoggerFactory;

public class App extends Application {
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());
        CrashReport.initCrashReport(getApplicationContext(), "c85bddb186", false);
        app = this;
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE); //补netty的锅
        GameHandler.getHandler().setListener(new GameListenerImpl());
        AssistHandler.getHandler().setListener(new AssistListenerImpl());
        GameClient.getClient().connectServer();
        AssistClient.getClient().connectServer();
    }

    public static App getApp() {return app;}

}
