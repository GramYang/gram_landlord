package com.gram.gram_landlord.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.gram.gram_landlord.App;
import com.gram.gram_landlord.R;
import com.gram.gram_landlord.base.BaseActivity;
import com.gram.gram_landlord.sdk.game.GameClient;
import com.gram.gram_landlord.sdk.game.protocols.request.LoginRequest;
import com.gram.gram_landlord.sdk.game.protocols.response.LoginResponse;
import com.gram.gram_landlord.utils.EncryptUtil;
import com.gram.gram_landlord.utils.NetworkUtil;
import com.gram.gram_landlord.utils.SharedPreferencesUtil;
import com.gram.gram_landlord.utils.ToastUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.orhanobut.logger.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.delete_username)
    ImageButton deleteUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private String encryptPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        etUsername.setText(SharedPreferencesUtil.getUsername());
        etPassword.setText("");
        addDisposable(
                //用户名点击
                RxView.focusChanges(etUsername)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(hasFocus -> {
                            if(hasFocus) {
                                if(etUsername.getText().length() > 0) visible(deleteUsername);
                                else gone(deleteUsername);
                            } else {
                                gone(deleteUsername);
                            }
                        }),
                //用户名输入
                RxTextView.textChangeEvents(etUsername)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(textViewTextChangeEvent -> {
                            etPassword.setText("");
                            if(textViewTextChangeEvent.count() > 0) visible(deleteUsername);
                            else gone(deleteUsername);
                        }),
                //登录
                RxView.clicks(btnLogin)
                        .throttleFirst(5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            if(!NetworkUtil.isConnected(App.getApp())) ToastUtil.showCenterSingleToast("当前网络不可用");
                            if(!GameClient.getClient().isConnected()) ToastUtil.showCenterSingleToast("服务器未响应");
                            else login();
                        }),
                //清空用户名和密码
                RxView.clicks(deleteUsername)
                        .throttleFirst(5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            etUsername.setText("");
                            gone(deleteUsername);
                            etUsername.setFocusable(true);
                            etUsername.setFocusableInTouchMode(true);
                            etUsername.requestFocus();
                        })
        );
    }

    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            ToastUtil.showCenterSingleToast("用户名和密码不能为空");
            return;
        }
        encryptPassword = EncryptUtil.passwordEncryptDES(password);
        LoginRequest request = new LoginRequest(username, encryptPassword);
        GameClient.getClient().send(request);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onLoginResponse(LoginResponse loginResponse) {
        EventBus.getDefault().removeStickyEvent(loginResponse);
        if(loginResponse.isSuccessFul()) {
            Logger.i("获取登录反馈，登录成功");
            Logger.i(loginResponse.getUsername() + loginResponse.getResponseMsg());
            SharedPreferencesUtil.saveUsername(loginResponse.getUsername());
            SharedPreferencesUtil.saveEncryptPassword(encryptPassword);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Logger.i("获取登录反馈，登录失败");
            encryptPassword = null;
            ToastUtil.showCenterLongToast(loginResponse.getResponseMsg());
        }
    }
}
