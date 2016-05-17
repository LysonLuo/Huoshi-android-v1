package im.huoshi.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import im.huoshi.HuoshiApplication;
import im.huoshi.R;

/**
 * Created by Lyson on 16/1/2.
 */
public class ToolbarUtils {
    private Context mContext;
    private TextView mRightView;
    private TextView mTitleTextView;
    private TextView mMiddleRightView;
    private OnToolBarClickListener mOnToolBarClickListener;

    public ToolbarUtils(Context mContext, Toolbar toolBar) {
        this.mContext = mContext;
        this.mTitleTextView = (TextView) toolBar.findViewById(R.id.toolbar_left_view);
        this.mRightView = (TextView) toolBar.findViewById(R.id.toolbar_right_view);
        this.mMiddleRightView = (TextView) toolBar.findViewById(R.id.toolbar_middle_right_view);
        initListener();
    }

    private void initListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.toolbar_right_view:
                        mOnToolBarClickListener.onToolBarRightViewClick(v);
                        break;
                    case R.id.toolbar_middle_right_view:
                        mOnToolBarClickListener.onToolBarMiddleRightViewClick(v);
                        break;
                    default:
                        break;
                }
            }
        };
        mMiddleRightView.setOnClickListener(onClickListener);
        mRightView.setOnClickListener(onClickListener);
    }

    public void setOnToolBarClickListener(OnToolBarClickListener onToolBarClickListener) {
        this.mOnToolBarClickListener = onToolBarClickListener;
    }

    public void setRightViewVisibility(int visibleState) {
        mRightView.setVisibility(visibleState);
    }

    public void setMiddleRightViewVisibility(int visibleState) {
        mMiddleRightView.setVisibility(visibleState);
    }

    public void setTitleText(String titleText) {
        mTitleTextView.setText(titleText);
    }

    public void setRightText(String rightText) {
        mRightView.setText(rightText);
    }

    public void setMiddleRightText(String rightText) {
        mMiddleRightView.setText(rightText);
    }

    public void setRightViewColor(int resId) {
        mRightView.setTextColor(ContextCompat.getColor(HuoshiApplication.getInstance(), resId));
    }

    public void setMiddleRightViewColor(int resId) {
        mMiddleRightView.setTextColor(ContextCompat.getColor(HuoshiApplication.getInstance(), resId));
    }

    public void setRightViewIcon(int resId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        mRightView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

    }

    public void setMiddleRightVIewIcon(int resId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        mMiddleRightView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    public void setRightViewEnable(boolean enable) {
        mRightView.setEnabled(enable);
    }

    public void setmMiddleRightViewEnable(boolean enable) {
        mMiddleRightView.setEnabled(enable);
    }

    public void setRightViewBg(int resId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        if (Build.VERSION.SDK_INT > 16) {
            mRightView.setBackground(drawable);
            return;
        }
        mRightView.setBackgroundDrawable(drawable);
    }

    public void setMiddleRightViewBg(int resId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        if (Build.VERSION.SDK_INT > 16) {
            mMiddleRightView.setBackground(drawable);
            return;
        }
        mMiddleRightView.setBackgroundDrawable(drawable);
    }

    public interface OnToolBarClickListener {

        void onToolBarRightViewClick(View v);

        void onToolBarMiddleRightViewClick(View v);
    }
}
