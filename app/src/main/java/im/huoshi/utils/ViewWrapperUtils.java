package im.huoshi.utils;

import android.view.View;

/**
 * Created by Lyson on 15/12/31.
 */
public class ViewWrapperUtils {
    private View mTarget;

    public ViewWrapperUtils(View target) {
        mTarget = target;
    }

    public int getHeight() {
        return mTarget.getLayoutParams().height;
    }

    public void setHeight(int height) {
        mTarget.getLayoutParams().height = height;
        mTarget.requestLayout();
    }

    public int getWidth() {
        return mTarget.getLayoutParams().width;
    }

    public void setWidth(int width) {
        mTarget.getLayoutParams().width = width;
        mTarget.requestLayout();
    }
}
