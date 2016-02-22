package im.huoshi.ui.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import im.huoshi.R;
import im.huoshi.base.BaseFragment;
import im.huoshi.common.OnRecClickListener;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

import java.util.List;

/**
 * 公用的祷告页面
 * <p>
 * Created by Lyson on 15/12/27.
 */
public class InterCesFragment extends BaseFragment {
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private InterCesRecAdapter mAdapter;

    public static InterCesFragment getInstance(){
        InterCesFragment fragment = new InterCesFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_interces, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        return contentView;
    }

    private void setupViews() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new InterCesRecAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setmRecClickListener(new OnRecClickListener<List<String>>() {
            @Override
            public void OnClick(List<String> strings) {
                InterCesDetailsActivity.launch((InterCesActivity) getActivity());
            }
        });
    }
}
