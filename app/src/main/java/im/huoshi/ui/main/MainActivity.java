package im.huoshi.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.umeng.update.UmengUpdateAgent;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.base.ToolbarSetter;
import im.huoshi.service.AsynDataService;
import im.huoshi.ui.bible.BibleFragment;
import im.huoshi.ui.find.FindFragment;
import im.huoshi.ui.huoshi.HuoshiFragment;
import im.huoshi.ui.me.MeFragment;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/24.
 */
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.rg_main)
    private RadioGroup mGroup;
    private HuoshiFragment mHuoshiFragment;
    private BibleFragment mBibleFragment;
    private FindFragment mFindFragment;
    private MeFragment mMeFragment;
    private AsynDataService.ContactsBinder mBinder;
    private SearchFragment mSearchFragment;
    private Dialog mReadDialog;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (AsynDataService.ContactsBinder) service;
            mBinder.asynContacts(MainActivity.this, mUser.getUserId());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(null);
        setIsFromMain(true);
        setContentView(R.layout.activity_main);
        ToolbarSetter.setupMainToolbar(this, getToolBar());
        ViewUtils.inject(this);

        setupViews();
        switchToHuoshiFragment();
        //友盟版本更新
//        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
        if (isLogin()) {
            asyncContacts();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isLogin() && mConnection != null && mBinder != null) {
            unbindService(mConnection);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSearchFragment != null && mSearchFragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(mSearchFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.commit();
            return;
        }
        super.onBackPressed();
    }

    private void asyncContacts() {
        Intent serviceIntent = new Intent(MainActivity.this, AsynDataService.class);
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mToolbarUtils.setRightViewIcon(R.mipmap.icon_search_blue);
        mToolbarUtils.setMiddleRightVIewIcon(R.mipmap.icon_ring_blue);
    }

    private void setupViews() {
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_huoshi:
                        switchToHuoshiFragment();
                        break;
                    case R.id.radio_bible:
                        switchToBibleFragment();
                        break;
                    case R.id.radio_find:
                        switchToFindFragment();
                        break;
                    case R.id.radio_me:
                        switchToMeFragment();
                        break;
                }
            }
        });
    }

    private void switchToHuoshiFragment() {
        hideTitle();
        mToolbarUtils.setTitleText("活石");
        if (mHuoshiFragment == null) {
            mHuoshiFragment = new HuoshiFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //hide biblefragment
        if (mBibleFragment != null && mBibleFragment.isAdded()) {
            transaction.hide(mBibleFragment);
        }
        //hide findfragment
        if (mFindFragment != null && mFindFragment.isAdded()) {
            transaction.hide(mFindFragment);
        }
        //hide mefragment
        if (mMeFragment != null && mMeFragment.isAdded()) {
            transaction.hide(mMeFragment);
        }
        if (!mHuoshiFragment.isAdded()) {
            transaction.add(R.id.layout_content, mHuoshiFragment);
        } else {
            transaction.show(mHuoshiFragment);
        }
        transaction.commit();
    }

    private void switchToBibleFragment() {
        showTitle();
        mToolbarUtils.setTitleText("圣经");
        if (mBibleFragment == null) {
            mBibleFragment = new BibleFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //hide huoshifragment
        if (mHuoshiFragment != null && mHuoshiFragment.isAdded()) {
            transaction.hide(mHuoshiFragment);
        }
        //hide findfragment
        if (mFindFragment != null && mFindFragment.isAdded()) {
            transaction.hide(mFindFragment);
        }
        //hide mefragment
        if (mMeFragment != null && mMeFragment.isAdded()) {
            transaction.hide(mMeFragment);
        }
        if (!mBibleFragment.isAdded()) {
            transaction.add(R.id.layout_content, mBibleFragment);
        } else {
            transaction.show(mBibleFragment);
        }
        transaction.commit();
    }


    private void switchToFindFragment() {
        hideTitle();
        mToolbarUtils.setTitleText("发现");
        if (mFindFragment == null) {
            mFindFragment = new FindFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //hide huoshifragment
        if (mHuoshiFragment != null && mHuoshiFragment.isAdded()) {
            transaction.hide(mHuoshiFragment);
        }
        //hide biblefragment
        if (mBibleFragment != null && mBibleFragment.isAdded()) {
            transaction.hide(mBibleFragment);
        }
        //hide mefragment
        if (mMeFragment != null && mMeFragment.isAdded()) {
            transaction.hide(mMeFragment);
        }
        if (!mFindFragment.isAdded()) {
            transaction.add(R.id.layout_content, mFindFragment);
        } else {
            transaction.show(mFindFragment);
        }
        transaction.commit();
    }

    private void switchToMeFragment() {
        hideTitle();
        mToolbarUtils.setTitleText("我");
        if (mMeFragment == null) {
            mMeFragment = new MeFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //hide huoshifragment
        if (mHuoshiFragment != null && mHuoshiFragment.isAdded()) {
            transaction.hide(mHuoshiFragment);
        }
        //hide biblefragment
        if (mBibleFragment != null && mBibleFragment.isAdded()) {
            transaction.hide(mBibleFragment);
        }
        //hide findefragment
        if (mFindFragment != null && mFindFragment.isAdded()) {
            transaction.hide(mFindFragment);
        }
        if (!mMeFragment.isAdded()) {
            transaction.add(R.id.layout_content, mMeFragment);
        } else {
            transaction.show(mMeFragment);
        }
        transaction.commit();
    }

    private void addSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mSearchFragment = new SearchFragment();
        transaction.add(R.id.main_layout_id, mSearchFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    private void showTitle() {
        mToolbarUtils.setRightViewVisibility(View.VISIBLE);
        mToolbarUtils.setMiddleRightViewVisibility(View.VISIBLE);
    }

    private void hideTitle() {
        mToolbarUtils.setRightViewVisibility(View.GONE);
        mToolbarUtils.setMiddleRightViewVisibility(View.GONE);
    }

    @Override
    public void onToolBarRightViewClick(View v) {
        addSearchFragment();
    }

    @Override
    public void onToolBarMiddleRightViewClick(View v) {
//        Dialog dialog = new InterCesDialog(this);
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        window.setGravity(Gravity.CENTER);
//        window.setAttributes(layoutParams);
//        dialog.show();
        if (isNotLogin()) {
            return;
        }
        if (mReadDialog != null && mReadDialog.isShowing()) {
            return;
        }
        mReadDialog = new ReadDialog(this, mUser.getUserId(), mReadStat);
        Window window = mReadDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);
        mReadDialog.show();
    }

    public static void launch(Activity act) {
        act.startActivity(new Intent(act, MainActivity.class));
    }
}
