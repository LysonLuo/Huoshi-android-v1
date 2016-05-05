package im.huoshi.ui.find.interces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.InterCesRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/4/9.
 */
public class CommentActivity extends BaseActivity {
    private static final String LOG_TAG = CommentActivity.class.getSimpleName();
    public static final int ACTION_PUB_COMMENT = 100;
    @ViewInject(R.id.edit_comment_content)
    private EditText mContentEdit;
    private int mIntercessionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ViewUtils.inject(this);
        setupViews();
    }

    private void setupViews() {
        mIntercessionId = getIntent().getIntExtra("intercession_id", 0);
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mToolbarUtils.setTitleText("发布祝福");
        mToolbarUtils.setRightText("发布");
        mToolbarUtils.setRightViewBg(R.drawable.shape_white_rec_blue_solid);
        mToolbarUtils.setRightViewColor(R.color.text_color_white);
    }

    @Override
    public void onToolBarRightViewClick(View v) {
        super.onToolBarRightViewClick(v);
        pubBless();
    }

    private void pubBless() {
        String content = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showShortToast("祝福内容不能为空哦！");
            return;
        }
        InterCesRequest.comment(CommentActivity.this, mUser.getUserId(), mIntercessionId, content, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure() {
                LogUtils.d(LOG_TAG, "comment fail");
            }
        });
    }

    public static void launch(BaseActivity activity, int intercessionId) {
        Intent intent = new Intent(activity, CommentActivity.class);
        intent.putExtra("intercession_id", intercessionId);
        activity.startActivityForResult(intent, ACTION_PUB_COMMENT);
    }
}
