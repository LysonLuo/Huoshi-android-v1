package im.huoshi.base;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;

import im.huoshi.R;

/**
 * Created by Lyson on 15/12/24.
 */
public class ToolbarSetter {
    public static void setupDefaultToolbar(final Context context, Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).finish();
                }
            }
        });
    }

    public static void setupMainToolbar(Context context, Toolbar toolbar) {
//        toolbar.setTitle(context.getString(R.string.app_name));
        toolbar.setNavigationIcon(null);
        toolbar.setNavigationOnClickListener(null);
    }
}
