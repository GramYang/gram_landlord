package com.gram.gram_landlord.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.gram.gram_landlord.activity.LoginActivity;
import com.gram.gram_landlord.sdk.game.protocols.HeartBeatStop;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDisposable();
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeartBeatStop(HeartBeatStop heartBeatStop) {
        if(heartBeatStop != null && !(this instanceof LoginActivity)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    protected abstract @LayoutRes int getLayoutId();

    protected void init() {}

    protected void addDisposable(Disposable... disposables) {
        if(compositeDisposable == null) compositeDisposable = new CompositeDisposable();
        for(Disposable disposable : disposables) {
            compositeDisposable.add(disposable);
        }
    }

    public void clearDisposable() {
        if(compositeDisposable != null) compositeDisposable.clear();
    }

    /**
     * 根据resid隐藏view
     */
    protected void gone(@IdRes int... id) {
        if(id != null && id.length > 0) {
            for(int resId : id) {
                View view = findViewById(resId);
                if(view != null) gone(view);
            }
        }
    }

    /**
     * 根据view实例隐藏view
     */
    protected void gone(View... views) {
        if(views != null && views.length > 0) {
            for(View view : views) {
                if(view != null && view.getVisibility() == View.VISIBLE) view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 根据resid显示view
     */
    protected void visible(@IdRes int... id) {
        if(id != null && id.length > 0) {
            for(int resId : id) {
                View view = findViewById(resId);
                if(view != null) visible(view);
            }
        }
    }

    /**
     * 根据view实例显示view
     */
    protected void visible(View... views) {
        if(views != null && views.length > 0) {
            for(View view : views) {
                if(view != null && view.getVisibility() == View.GONE) view.setVisibility(View.VISIBLE);
            }
        }
    }
}
