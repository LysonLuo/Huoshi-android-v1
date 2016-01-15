package im.huoshi.ui.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.ui.me.MyPrayerActivity;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * 代祷详情
 * <p>
 * Created by Lyson on 15/12/26.
 */
public class SuffrageDetailsActivity extends BaseActivity {
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SuffrageDelRecAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suffrage_details);
        ViewUtils.inject(this);

        setupViews();

    }

    private void setupViews() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SuffrageDelRecAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void launch(MyPrayerActivity activity) {
        activity.startActivity(new Intent(activity, SuffrageDetailsActivity.class));
    }
}
