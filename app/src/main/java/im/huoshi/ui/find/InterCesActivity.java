package im.huoshi.ui.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/26.
 */
public class InterCesActivity extends BaseActivity {
    private InterCesFragment mInterCesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interces);
        ViewUtils.inject(this);

        setupViews();
    }

    private void setupViews() {
        mToolbarUtils.setTitleText(getString(R.string.text_interces));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mInterCesFragment == null) {
            mInterCesFragment = InterCesFragment.getInstance(InterCesFragment.INTERCES_TYPE_INTERCES);
        }
        if (!mInterCesFragment.isAdded()) {
            transaction.add(R.id.layout_content, mInterCesFragment);
        } else {
            transaction.show(mInterCesFragment);
        }
        transaction.commit();
    }

    public static void launch(BaseActivity act) {
        act.startActivity(new Intent(act, InterCesActivity.class));
    }
}
