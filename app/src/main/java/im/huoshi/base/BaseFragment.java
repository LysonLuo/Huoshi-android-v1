package im.huoshi.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import im.huoshi.data.ReadPreference;
import im.huoshi.data.UserPreference;
import im.huoshi.model.HuoshiData;
import im.huoshi.model.ReadStat;
import im.huoshi.model.User;
import im.huoshi.ui.main.LoginActivity;
import im.huoshi.utils.LogUtils;

/**
 * Created by Lyson on 15/12/24.
 */
public class BaseFragment extends Fragment {
    private static final String LOG_TAG = BaseFragment.class.getCanonicalName();
    private ProgressDialog mProgressDialog;
    private ProgressFragment mProgressFragment;
    private RetryFragment mRetryFragment;
    private NoDataFragment mNoDataFragment;
    private NetWorkFailFragment mNetWorkFailFragment;
    protected UserPreference mLocalUser;
    public static User mUser;
    public static ReadPreference mLocalRead;
    public static ReadStat mReadStat;
    public static HuoshiData mHuoshiData;

    private boolean mIsFragmentAlive = true;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsFragmentAlive = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reloadLocalData();
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadLocalData();
    }

    protected void reloadLocalData() {
        mLocalUser = UserPreference.getInstance();
        mUser = mLocalUser.getUser();
        mLocalRead = ReadPreference.getInstance();
        mReadStat = mLocalRead.getReadStat();
        mHuoshiData = mLocalRead.getHuoshiData();
    }

    protected boolean isLogin() {
        return mUser.getUserId() != -1;
    }

    protected boolean isNotLogin() {
        if (mUser.getUserId() == -1) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return true;
        }
        return false;
    }

    /**
     * 显示圆形进度条的Fragment，需要显示指定ViewGroup的id
     *
     * @param layoutId
     */
    protected void showProgressFragment(int layoutId) {
        if (!mIsFragmentAlive) {
            LogUtils.d(LOG_TAG, "fragment is not alive");
            return;
        }
        if (getActivity() != null && !((BaseActivity) getActivity()).isActivityAlive()) {
            LogUtils.d(LOG_TAG, "activity is not alive");
            return;
        }
        if (mProgressFragment == null) {
            mProgressFragment = new ProgressFragment();
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!mProgressFragment.isAdded()) {
            ft.add(layoutId, mProgressFragment);
        }
        ft.show(mProgressFragment);

        if (mRetryFragment != null && mRetryFragment.isVisible()) {
            ft.remove(mRetryFragment);
        }

        ft.commitAllowingStateLoss();
    }

    /**
     * 移除圆形进度条的Fragment
     */
    protected void removeProgressFragment() {
        if (!mIsFragmentAlive) {
            LogUtils.d(LOG_TAG, "fragment is not alive");
            return;
        }
        if (!((BaseActivity) getActivity()).isActivityAlive()) {
            LogUtils.d(LOG_TAG, "activity is not alive");
            return;
        }
        if (mProgressFragment == null) {
            return;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(mProgressFragment);
        ft.commitAllowingStateLoss();
        mProgressFragment = null;
    }

    /**
     * 在数据加载失败或其他异常情况的情况下，调用该方法显示RetryFragment，需要显示指定ViewGroup的id
     * 因为在回调方法定义的结尾主动移除了自身，所以不再需要独立的方法去移除
     */
    protected void showRetryFragment(int layoutId, RetryFragment.OnRetryListener listener) {
        if (!mIsFragmentAlive) {
            LogUtils.d(LOG_TAG, "fragment is not alive");
            return;
        }
        if (!((BaseActivity) getActivity()).isActivityAlive()) {
            LogUtils.d(LOG_TAG, "activity is not alive");
            return;
        }
        if (mRetryFragment == null) {
            mRetryFragment = new RetryFragment();
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!mRetryFragment.isAdded()) {
            ft.add(layoutId, mRetryFragment, "");
        }
        ft.show(mRetryFragment);
        ft.commitAllowingStateLoss();
        mRetryFragment.setOnRetryListener(listener);
    }

    /**
     * 无数据时所显示的页面，需要显示指定ViewGroup的id
     *
     * @param layouId
     */
    protected void showNoDataFragment(int layouId) {
        if (!mIsFragmentAlive) {
            LogUtils.d(LOG_TAG, "fragment is not alive");
            return;
        }
        if (!((BaseActivity) getActivity()).isActivityAlive()) {
            LogUtils.d(LOG_TAG, "activity is not alive");
            return;
        }
        if (mNoDataFragment == null) {
            mNoDataFragment = new NoDataFragment();
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!mNoDataFragment.isAdded()) {
            ft.add(layouId, mNoDataFragment, "");
        }
        ft.show(mNoDataFragment);
        ft.commitAllowingStateLoss();
    }

    protected void showNoDataFragment(int layouId, String text) {
        showNoDataFragment(layouId);
        if (mNoDataFragment != null) {
            mNoDataFragment.setText(text);
        }
    }

    protected void showNetWorkFailFragment(int layoutId) {
        if (!mIsFragmentAlive) {
            LogUtils.d(LOG_TAG, "fragment is not alive");
            return;
        }
        if (mNetWorkFailFragment == null) {
            mNetWorkFailFragment = new NetWorkFailFragment();
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!mNetWorkFailFragment.isAdded()) {
            ft.add(layoutId, mNetWorkFailFragment, "");
        }
        ft.show(mNetWorkFailFragment);
        ft.commitAllowingStateLoss();
    }

    protected void removeFailFragment() {
        if (!mIsFragmentAlive) {
            LogUtils.d(LOG_TAG, "fragment is not alive");
            return;
        }
        if (mNetWorkFailFragment == null) {
            return;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(mNetWorkFailFragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 移除没有数据的Fragment
     */
    protected void removeNoDataFragment() {
        if (!mIsFragmentAlive) {
            LogUtils.d(LOG_TAG, "fragment is not alive");
            return;
        }
        if (!((BaseActivity) getActivity()).isActivityAlive()) {
            LogUtils.d(LOG_TAG, "activity is not alive");
            return;
        }
        if (mNoDataFragment == null) {
            return;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(mNoDataFragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 显示信息进度条
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    /**
     * 移除信息进度条
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 段时间的提示
     *
     * @param message
     */
    protected void showShortToast(String message) {
        ((BaseActivity) getActivity()).showShortToast(message);
    }

    /**
     * 长时间的提示
     *
     * @param message
     */
    protected void showLongToast(String message) {
        ((BaseActivity) getActivity()).showLongToast(message);
    }
}
