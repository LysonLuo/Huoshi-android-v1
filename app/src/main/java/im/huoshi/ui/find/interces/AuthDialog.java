package im.huoshi.ui.find.interces;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import im.huoshi.R;
import im.huoshi.utils.ScreenUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/3/18.
 */
public class AuthDialog extends AppCompatDialog {
    @ViewInject(R.id.imageview_bg)
    private ImageView mBgImageView;
    @ViewInject(R.id.textview_first)
    private TextView mFirstTextView;
    @ViewInject(R.id.textview_second)
    private TextView mSecondTextView;
    @ViewInject(R.id.textview_common)
    private TextView mCommonTextView;
    //用于标志当前是否授权页面，否则是短信分享页面
    private boolean mIsAuthView = true;
    private AuthListener mAuthListener;


    public void setAuthListener(AuthListener authListener) {
        this.mAuthListener = authListener;
    }

    public AuthDialog(final Context context) {
        super(context, R.style.CustomPopup);
        View rootView = LayoutInflater.from(context).inflate(R.layout.widget_auth_dialog, null);
        final int dialogWidth = ScreenUtils.getScreenWidth(getWindow().getWindowManager().getDefaultDisplay());
        rootView.setMinimumWidth(dialogWidth - ScreenUtils.dip2px(context, 40));
        setContentView(rootView);
        ViewUtils.inject(this);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mCommonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuthListener == null) {
                    return;
                }
                if (mIsAuthView) {
                    mAuthListener.OnAuth();
                    return;
                }
                mAuthListener.OnMsg();
            }
        });
    }

    public void updateUI() {
        mBgImageView.setBackgroundResource(R.mipmap.img_share_bg);
        mFirstTextView.setText("活石熟人代祷功能是基于好友关系的半私密代祷空间。因此需要你的通讯录里至少有3个主内好友安装并注册了活石APP，才能解锁此项功能。（好友注册情况可以在【我】——【我的邀请】里查看)");
        mSecondTextView.setText("现在就请你把活石APP分享给更多弟兄姊妹，一起来体验手机上的全新代祷吧");
        mCommonTextView.setText("通过短信分享");
        mIsAuthView = false;
    }

    public interface AuthListener {
        void OnAuth();

        void OnMsg();
    }
}
