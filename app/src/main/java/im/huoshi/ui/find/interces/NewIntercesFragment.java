package im.huoshi.ui.find.interces;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.InterCesRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.base.BaseFragment;
import im.huoshi.common.OnRecClickListener;
import im.huoshi.model.Intercession;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * 公用的祷告页面
 * <p/>
 * Created by Lyson on 16/5/31.
 */
public class NewIntercesFragment extends BaseFragment {
    public static final int INTECES_TYPE_ALL = 0;//代祷
    public static final int INTECES_TYPE_MINE = 1;//我的祷告
    public static final int INTECES_TYPE_JOIN = 2;//我的代祷
    @ViewInject(R.id.refresh_layout)
    private SwipeRefreshLayout mRefreshLayout;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    private IntercesAdapter mAdapter;
    private RecyclerAdapterWithHF mHfAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mIntercesType;//类别，区分是代祷、我的祷告，我的代祷
    private List<Intercession> mIntercessionList = new ArrayList<>();
    private SwipeRefreshHelper mSwipeRefreshHelper;
    private int mStartPage = 1;
    private boolean mIsLoading = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_interces, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        return contentView;
    }

    private void setupViews() {
        mIntercesType = getArguments().getInt("type");
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRefreshLayout.setColorSchemeColors(Color.BLUE);
        mAdapter = new IntercesAdapter(getActivity(), mIntercessionList);
        mHfAdapter = new RecyclerAdapterWithHF(mAdapter);
        mRecyclerView.setAdapter(mHfAdapter);
        mSwipeRefreshHelper = new SwipeRefreshHelper(mRefreshLayout);
        mSwipeRefreshHelper.setOnSwipeRefreshListener(new SwipeRefreshHelper.OnSwipeRefreshListener() {
            @Override
            public void onfresh() {
                dataNofify();
            }
        });
        mSwipeRefreshHelper.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadInterces();
            }
        });

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshHelper.autoRefresh();
            }
        });

        mAdapter.setRecClickListener(new OnRecClickListener<Intercession>() {
            @Override
            public void OnClick(Intercession intercession) {
                InterCesDetailsActivity.launch((BaseActivity) getActivity(), intercession.getIntercessionId());
            }
        });
    }

    private void loadInterces() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        InterCesRequest.intercesList((BaseActivity) getActivity(), mUser.getUserId(), mStartPage, mIntercesType, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                mIsLoading = false;
                mSwipeRefreshHelper.refreshComplete();
                mSwipeRefreshHelper.setLoadMoreEnable(true);
                List<Intercession> intercessionList = new Gson().fromJson(responseString, new TypeToken<List<Intercession>>() {
                }.getType());
                setupViewsByInters(intercessionList);
            }

            @Override
            public void onFailure() {
                mIsLoading = false;
                mSwipeRefreshHelper.loadMoreComplete(true);
                showNetWorkFailFragment(R.id.framelayout);
                LogUtils.d("XXX", "what");
            }
        });
    }

    private void setupViewsByInters(List<Intercession> intercessionList) {
        mSwipeRefreshHelper.loadMoreComplete(true);
        if (intercessionList.size() > 0) {
            mStartPage++;
            mIntercessionList.addAll(intercessionList);
            mHfAdapter.notifyDataSetChanged();
            return;
        }

        mSwipeRefreshHelper.setNoMoreData();
        mSwipeRefreshHelper.setLoadMoreEnable(false);
    }

    public void dataNofify() {
        mSwipeRefreshHelper.setLoadMoreEnable(false);
        mStartPage = 1;
        mIntercessionList.clear();
        loadInterces();
    }

    public static NewIntercesFragment getInstance(int type) {
        NewIntercesFragment fragment = new NewIntercesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

}
