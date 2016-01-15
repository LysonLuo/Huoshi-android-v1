package im.huoshi.ui.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.ui.main.MainActivity;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/26.
 */
public class SuffrageActivity extends BaseActivity {
    private SuffrageFragment mSuffrageFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suffrage);
        ViewUtils.inject(this);

        setupViews();
    }

    private void setupViews() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mSuffrageFragment == null) {
            mSuffrageFragment = new SuffrageFragment();
        }
        if (!mSuffrageFragment.isAdded()) {
            transaction.add(R.id.layout_content, mSuffrageFragment);
        } else {
            transaction.show(mSuffrageFragment);
        }
        transaction.commit();
    }

    public static void launch(MainActivity act) {
        act.startActivity(new Intent(act, SuffrageActivity.class));
    }
}
