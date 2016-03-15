package im.huoshi.ui.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.ui.main.MainActivity;

/**
 * Created by Lyson on 15/12/26.
 */
public class ShareHuoshiActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_huoshi);
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, ShareHuoshiActivity.class));
    }
}
