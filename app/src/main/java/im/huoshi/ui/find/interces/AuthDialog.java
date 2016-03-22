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
        mFirstTextView.setText("由于代祷是针对通讯录好友之间的熟人关系，所以需要您的通讯录好友里至少有3位安装活石APP。");
        mSecondTextView.setText("现在就请你把活石APP分享给更多朋友，体验熟人代祷");
        mCommonTextView.setText("通过短信分享");
        mIsAuthView = false;
    }

    public interface AuthListener {
        void OnAuth();

        void OnMsg();
    }
}
