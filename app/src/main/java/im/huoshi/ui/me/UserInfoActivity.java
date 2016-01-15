package im.huoshi.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.ui.main.MainActivity;

/**
 * Created by Lyson on 15/12/27.
 */
public class UserInfoActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    public static void launch(MainActivity activity) {
        activity.startActivity(new Intent(activity, UserInfoActivity.class));
    }
}
