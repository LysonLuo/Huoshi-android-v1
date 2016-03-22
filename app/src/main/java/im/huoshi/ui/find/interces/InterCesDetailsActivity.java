package im.huoshi.ui.find.interces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * 代祷详情
 * <p>
 * Created by Lyson on 15/12/26.
 */
public class InterCesDetailsActivity extends BaseActivity {
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private InterCesDelRecAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interces_details);
        ViewUtils.inject(this);

        setupViews();

    }

    private void setupViews() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new InterCesDelRecAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void launch(BaseActivity activity) {
        activity.startActivity(new Intent(activity, InterCesDetailsActivity.class));
    }
}
