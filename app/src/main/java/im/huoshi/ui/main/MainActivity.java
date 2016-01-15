package im.huoshi.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.base.ToolbarSetter;
import im.huoshi.ui.bible.BibleFragment;
import im.huoshi.ui.find.FindFragment;
import im.huoshi.ui.find.SuffrageDialog;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        ToolbarSetter.setupMainToolbar(this, getToolBar());
        ViewUtils.inject(this);

        setupViews();
        switchToHuoshiFragment();
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

    public static void launch(Activity act) {
        act.startActivity(new Intent(act, MainActivity.class));
    }

    @Override
    public void onToolBarRightViewClick(View v) {
        super.onToolBarRightViewClick(v);
        Dialog dialog = new ReadDialog(this);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);
        dialog.show();
    }

    @Override
    public void onToolBarMiddleRightViewClick(View v) {
        super.onToolBarMiddleRightViewClick(v);
        Dialog dialog = new SuffrageDialog(this);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);
        dialog.show();
    }
}
