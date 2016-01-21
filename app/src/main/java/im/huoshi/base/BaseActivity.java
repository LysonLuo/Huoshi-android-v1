package im.huoshi.base;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import im.huoshi.R;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ToolbarUtils;

/**
 * Created by Lyson on 15/12/24.
 */
public class BaseActivity extends AppCompatActivity implements ToolbarUtils.OnToolBarClickListener {
    private static final String LOG_TAG = BaseActivity.class.getCanonicalName();
    private Toolbar mToolbar;
    protected ToolbarUtils mToolbarUtils;
    private ProgressDialog mProgressDialog;
    private ProgressFragment mProgressFragment;
    private RetryFragment mRetryFragment;
    private NoDataFragment mNoDataFragment;

    private boolean mIsActivityAlive = true;
    private boolean isShowToolbar = true;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTitle();
        MobclickAgent.onResume(this);
        MobclickAgent.setSessionContinueMillis(60);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    protected void initTitle(){}

    @Override
    public void setContentView(View view) {
        FrameLayout container = new FrameLayout(this);
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.setId(R.id.content_layout_id);
        container.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        RelativeLayout topLevelLayout = null;

        if (isShowToolbar) {
            topLevelLayout = new RelativeLayout(this);
            topLevelLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.widget_toolbar, null);
            Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material));
            toolbar.setLayoutParams(layoutParams);
            setSupportActionBar(toolbar);
            ToolbarSetter.setupDefaultToolbar(this, toolbar);
            mToolbar = toolbar;
            initToolbarUtils();

            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams1.addRule(RelativeLayout.BELOW, mToolbar.getId());
            topLevelLayout.addView(mToolbar);
            topLevelLayout.addView(container, layoutParams1);
        }
        super.setContentView(isShowToolbar ? topLevelLayout : container);

    }

    private void initToolbarUtils() {
        mToolbarUtils = new ToolbarUtils(this, mToolbar);
        mToolbarUtils.setOnToolBarClickListener(this);
    }

    /**
     * 在setContentView前调用
     *
     * @param isShowToolbar
     */
    public void setShowToolbar(boolean isShowToolbar) {
        this.isShowToolbar = isShowToolbar;
    }


    /**
     * 判断Activity状态
     *
     * @return
     */
    public boolean isActivityAlive() {
        return mIsActivityAlive;
    }

    /**
     * 获取Toolbar
     *
     * @return
     */
    public Toolbar getToolBar() {
        return mToolbar;
    }

    /**
     * 显示圆形进度条的Fragment，区别于ProgressDialog，覆盖在整个Activity中
     */
    protected void showProgressFragment() {
        if (!mIsActivityAlive) {
            LogUtils.d(LOG_TAG, "activity is not alive");
            return;
        }
        if (mProgressFragment == null) {
            mProgressFragment = new ProgressFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!mProgressFragment.isAdded()) {
            ft.add(R.id.content_layout_id, mProgressFragment, "");
        }
        ft.show(mProgressFragment);

        if (mRetryFragment != null && mRetryFragment.isVisible()) {
            ft.remove(mRetryFragment);
        }

        ft.commitAllowingStateLoss();
    }

    /**
     * 把ProgressFragment从Activity中移除
     */
    protected void removeProgressFragment() {
        if (!mIsActivityAlive) {
            LogUtils.d(LOG_TAG, "activity is not alive");
            return;
        }
        if (mProgressFragment == null) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(mProgressFragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 在数据加载失败或其他异常情况的情况下，调用该方法显示RetryFragment，需要显示指定ViewGroup的id
     * 因为在回调方法定义的结尾主动移除了自身，所以不再需要独立的方法去移除
     */
    protected void showRetryFragment(RetryFragment.OnRetryListener listener) {
        if (!mIsActivityAlive) {
            LogUtils.d(LOG_TAG, "activity is not alive");
            return;
        }
        if (mRetryFragment == null) {
            mRetryFragment = new RetryFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!mRetryFragment.isAdded()) {
            ft.add(R.id.content_layout_id, mRetryFragment, "");
        }
        ft.show(mRetryFragment);
        ft.commitAllowingStateLoss();
        mRetryFragment.setOnRetryListener(listener);
    }

    /**
     * 显示没有数据的Fragment
     */
    protected void showNoDataFragment() {
        showNoDataFragment(R.id.content_layout_id);
    }

    /**
     * 显示没有数据的Fragment，带文本
     */
    protected void showNoDataFragment(String text) {
        showNoDataFragment(R.id.content_layout_id, text);
    }

    protected void showNoDataFragment(int layouId) {
        if (!mIsActivityAlive) {
            LogUtils.d(LOG_TAG, "activity is not alive");
            return;
        }
        if (mNoDataFragment == null) {
            mNoDataFragment = new NoDataFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
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

    /**
     * 显示圆形进度条对话框，获取整个window的焦点，一般用于登录等具有输入操作的Activity
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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
     * 移除圆形进度条对话框
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间的提示
     *
     * @param message
     */
    protected void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onToolBarRightViewClick(View v) {

    }

    @Override
    public void onToolBarMiddleRightViewClick(View v) {

    }
}
