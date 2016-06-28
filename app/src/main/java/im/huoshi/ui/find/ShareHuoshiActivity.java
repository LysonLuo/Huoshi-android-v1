package im.huoshi.ui.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import im.huoshi.BuildConfig;
import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.event.ShareEvent;
import im.huoshi.utils.ShareUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/26.
 */
public class ShareHuoshiActivity extends BaseActivity {
    @ViewInject(R.id.textview_share_number)
    private TextView mShareNumberTextView;
    @ViewInject(R.id.button_share_huoshi)
    private Button mShareButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_huoshi);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);

        setupViews();
    }

    @Subscribe
    public void onEvent(ShareEvent shareEvent) {
        mShareNumberTextView.setText(mLocalRead.getTotalShareTimes() + "");
    }


    private void setupViews() {
        mToolbarUtils.setTitleText(getString(R.string.text_share_huoshi));
        mShareNumberTextView.setText(mLocalRead.getTotalShareTimes() + "");
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 16/6/23 下载链接
                ShareUtils.init(ShareHuoshiActivity.this, "跟你分享一个能代祷的主内App工具。", BuildConfig.WEB_URI + "intercession/download.php");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, ShareHuoshiActivity.class));
    }
}
