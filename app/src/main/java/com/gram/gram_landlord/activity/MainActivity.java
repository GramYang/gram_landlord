package com.gram.gram_landlord.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gram.gram_landlord.R;
import com.gram.gram_landlord.base.BaseActivity;
import com.gram.gram_landlord.event.PictureCompressEvent;
import com.gram.gram_landlord.fragment.HallFragment;
import com.gram.gram_landlord.fragment.RegionFragment;
import com.gram.gram_landlord.sdk.assistant.AssistClient;
import com.gram.gram_landlord.sdk.assistant.RequestKey;
import com.gram.gram_landlord.sdk.assistant.ReturnCode;
import com.gram.gram_landlord.sdk.assistant.entity.Request;
import com.gram.gram_landlord.sdk.assistant.entity.Response;
import com.gram.gram_landlord.sdk.game.GameClient;
import com.gram.gram_landlord.sdk.game.protocols.request.ExitHallRequest;
import com.gram.gram_landlord.utils.AvatarChangeUtil;
import com.gram.gram_landlord.utils.CommonUtil;
import com.gram.gram_landlord.utils.SharedPreferencesUtil;
import com.gram.gram_landlord.utils.ToastUtil;
import com.gram.gram_landlord.widget.AvatarPopupWindow;
import com.gram.gram_landlord.widget.CircleImageView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CircleImageView userAvatar;
    private TextView name;
    private TextView win;
    private TextView lose;
    private TextView money;
    private AvatarPopupWindow avatarPopupWindow;
    private HallFragment hall;
    private RegionFragment region;
    //拍照照片的uri
    private Uri cameraUri = null;
    //裁剪图片的uri
    private Uri cropUri = null;

    public static final int REQUEST_IMAGE_GET = 0;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_IMAGE_CROP = 2;

    private long firstPressedTime;
    private boolean isRegion = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        toolbar.setTitle("游戏分区");
        setSupportActionBar(toolbar);
        requestPlayerInfo();
        region = new RegionFragment();
        hall = new HallFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, region)
                .add(R.id.fl_content, hall)
                .commit();
        hallToRegion();
        navView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            switch (menuItem.getItemId()) {
                case R.id.item_info:
//                    startActivity(new Intent(this, InfoActivity.class));
                    return true;
                case R.id.item_settings:
//                    startActivity(new Intent(this, SettingsActivity.class));
                    return true;
            }
            return false;
        });
        View headerView = navView.getHeaderView(0);
        userAvatar = headerView.findViewById(R.id.user_avatar);
        name = headerView.findViewById(R.id.tv_name);
        win = headerView.findViewById(R.id.tv_win);
        lose = headerView.findViewById(R.id.tv_lose);
        money = headerView.findViewById(R.id.tv_money);
        name.setText(SharedPreferencesUtil.getUsername());
        userAvatar.setOnClickListener(v -> {
            View contentView = View.inflate(this, R.layout.avatar_window_popup, null);
            avatarPopupWindow = new AvatarPopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, this);
            View rootView = View.inflate(this, getLayoutId(), null);
            avatarPopupWindow.showAtLocation(rootView,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    avatarPopupWindow.dismiss();
                    AvatarChangeUtil.selectPicture(this);
                } else {
                    avatarPopupWindow.dismiss();
                }
                break;
            case 300:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    avatarPopupWindow.dismiss();
                    AvatarChangeUtil.takePicture(this);
                } else {
                    avatarPopupWindow.dismiss();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPictureCompressEvent(PictureCompressEvent pictureCompressEvent) {
        //压缩后的图片不大于1MB
        Uri compressUri = AvatarChangeUtil.compress(this, pictureCompressEvent.getPictureUri(), 1024);
        if(compressUri != null) {
            SharedPreferencesUtil.saveUserAvatar(compressUri);
            userAvatar.setImageURI(compressUri);
            requestUploadAvatar(compressUri);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                //相册选取，不需要压缩，但是需要剪切
                case REQUEST_IMAGE_GET:
                    if(data == null) return;
                    Uri pictureUri = data.getData();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        String path = AvatarChangeUtil.formatUri(this, pictureUri);
                        pictureUri = FileProvider.getUriForFile(this, AvatarChangeUtil.FILEPROVIDER, new File(path));
                        AvatarChangeUtil.crop(this, pictureUri);
                    } else {
                        AvatarChangeUtil.crop(this, pictureUri);
                    }
                    break;
                //拍照，拍照不需要剪切，但是需要压缩
                case REQUEST_IMAGE_CAPTURE:
                    if(cameraUri != null)
                        //压缩需要异步，不能在UI线程
                        EventBus.getDefault().post(new PictureCompressEvent(cameraUri));
                    break;
                //crop进行图片剪切
                case REQUEST_IMAGE_CROP:
                    if(cropUri != null) userAvatar.setImageURI(cropUri);
                    break;
            }
        }
    }

    /**
     * 设置hallfragment回退退出大厅
     */
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - firstPressedTime < 2000) {
            if(!isRegion) {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for(Fragment fragment : fragments) {
                    if(fragment instanceof HallFragment) {
                        ExitHallRequest request = new ExitHallRequest(SharedPreferencesUtil.getUsername());
                        GameClient.getClient().send(request);
                        hallToRegion();
                        return;
                    }
                }
            } else {
                super.onBackPressed();
            }
        } else {
            ToastUtil.showCenterSingleToast( "再按一次退出");
            firstPressedTime = System.currentTimeMillis();
        }

    }

    /**
     * 玩家信息反馈
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAssistResponse(Response response) {
        if(response.getKey().equals(RequestKey.GET_PLAYER_INFO) && response.get("username").equals(SharedPreferencesUtil.getUsername())) {
            if(response.getCode() == ReturnCode.CODE_404) Logger.e("用户名不存在");
            if(response.getCode() == ReturnCode.CODE_200) {
                win.append(response.get("win"));
                SharedPreferencesUtil.saveWin(response.get("win"));
                lose.append(response.get("lose"));
                SharedPreferencesUtil.saveLose(response.get("lose"));
                money.append(response.get("money"));
                SharedPreferencesUtil.saveMoney(response.get("money"));
                if(CommonUtil.checkNotNull(response.get("avatar")))
                    userAvatar.setImageBitmap(AvatarChangeUtil.bytes2Bitmap(response.getPiture()));
            }

        }
        if(response.getKey().equals(RequestKey.UPDATE_AVATAR) && response.getCode() == ReturnCode.CODE_500) {
            ToastUtil.showCenterSingleToast("头像上传失败");
        }
    }

    public void setCropUri(Uri cropUri) {
        this.cropUri = cropUri;
    }

    public void setCameraUri(Uri cameraUri) {
        this.cameraUri = cameraUri;
    }

    private void requestPlayerInfo() {
        Request request = new Request();
        request.setKey(RequestKey.GET_PLAYER_INFO);
        request.put("username", SharedPreferencesUtil.getUsername());
        AssistClient.getClient().send(request);
    }

    private void requestUploadAvatar(Uri uri) {
        Request request = new Request();
        request.setKey(RequestKey.UPDATE_AVATAR);
        request.put("username", SharedPreferencesUtil.getUsername());
        request.put("password", SharedPreferencesUtil.getEncryptPassword());
        request.put("avatar", ".png");
        request.setPicture(AvatarChangeUtil.bitmap2Bytes(AvatarChangeUtil.getBitmapFromUri(this, uri)));
        AssistClient.getClient().send(request);
    }

    public void regionToHall() {
        getSupportFragmentManager().beginTransaction().hide(region).show(hall).commit();
        isRegion = false;
        toolbar.setTitle("游戏大厅");
    }

    public void hallToRegion() {
        getSupportFragmentManager().beginTransaction().hide(hall).show(region).commit();
        isRegion = true;
        toolbar.setTitle("游戏分区");
    }

}
