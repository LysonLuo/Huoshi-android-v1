package im.huoshi.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import im.huoshi.R;

/**
 * Created by Lyson on 16/1/2.
 */
public class ToolbarUtils {
    private Context mContext;
    private Toolbar mToolBar;
    private ImageView mRightView;
    private ImageView mMiddleRightView;
    private OnToolBarClickListener mOnToolBarClickListener;

    public ToolbarUtils(Context mContext, Toolbar toolBar) {
        this.mContext = mContext;
        this.mToolBar = toolBar;
        this.mRightView = (ImageView) toolBar.findViewById(R.id.toolbar_right_view);
        this.mMiddleRightView = (ImageView) toolBar.findViewById(R.id.toolbar_middle_right_view);
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

    public void setRightViewIcon(int resId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        mRightView.setImageDrawable(drawable);

    }

    public void setMiddleRightVIewIcon(int resId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        mMiddleRightView.setImageDrawable(drawable);
    }

    public interface OnToolBarClickListener {

        void onToolBarRightViewClick(View v);

        void onToolBarMiddleRightViewClick(View v);
    }
}
