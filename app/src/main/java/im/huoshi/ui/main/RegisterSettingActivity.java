package im.huoshi.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;

/**
 * Created by Lyson on 15/12/28.
 */
public class RegisterSettingActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_setting);
    }

    public static void launch(RegisterActivity activity) {
        activity.startActivity(new Intent(activity, RegisterSettingActivity.class));
    }
}
