package com.gram.gram_landlord.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.greenrobot.eventbus.EventBus;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    protected Context context;
    private CompositeDisposable compositeDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        clearDisposable();
        unbinder.unbind();
        super.onDestroy();

    }

    protected void init() {}

    public abstract @LayoutRes int getContentViewId();

    /**
     * 添加Rx订阅
     */
    protected void addDisposable(Disposable... disposables) {
        if(compositeDisposable == null) compositeDisposable = new CompositeDisposable();
        for(Disposable disposable : disposables) {
            compositeDisposable.add(disposable);
        }
    }

    /**
     * 取消所有Rx订阅
     */
    public void clearDisposable() {
        if(compositeDisposable != null) compositeDisposable.clear();
    }
}
