package im.huoshi.ui.find.interces;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import im.huoshi.views.RecyclerViewScrollListener;

/**
 * 公用的祷告页面
 * <p/>
 * Created by Lyson on 15/12/27.
 */
public class InterCesFragment extends BaseFragment {
    public static final int INTECES_TYPE_ALL = 0;//代祷
    public static final int INTECES_TYPE_MINE = 1;//我的祷告
    public static final int INTECES_TYPE_JOIN = 2;//我的代祷
    @ViewInject(R.id.refresh_layout)
    private SwipeRefreshLayout mRefreshLayout;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private InterCesRecAdapter mAdapter;
    private int mIntercesType;//类别，区分是代祷、我的祷告，我的代祷
    private List<Intercession> mIntercessionList = new ArrayList<>();
    private RecyclerViewScrollListener mScrollListener;
    private int mStartPage = 1;
    private boolean mIsLoadMore = false;
    private boolean mNoMoreData = false;
    private boolean mIsLoading = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_interces, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        loadInterces();
        return contentView;
    }

    private void setupViews() {
        mIntercesType = getArguments().getInt("type");
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new InterCesRecAdapter(getActivity(), mIntercessionList);
        mRecyclerView.setAdapter(mAdapter);
        mScrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                if (!mNoMoreData) {
                    mIsLoadMore = true;
                    loadInterces();
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);
        mAdapter.setmRecClickListener(new OnRecClickListener<Intercession>() {
            @Override
            public void OnClick(Intercession intercession) {
                InterCesDetailsActivity.launch((BaseActivity) getActivity(), intercession.getIntercessionId());
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mStartPage = 1;
                mNoMoreData = false;
                mIntercessionList.clear();
                mAdapter.setNoMoreData(mNoMoreData);
                mScrollListener.reSetPreviousTotal();
                loadInterces();
            }
        });
    }

    private void loadInterces() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        if (!mIsLoadMore) {
            showProgressFragment(R.id.framelayout);
        }
        InterCesRequest.intercesList((BaseActivity) getActivity(), mUser.getUserId(), mStartPage, mIntercesType, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                mIsLoading = false;
                mRefreshLayout.setRefreshing(false);
                removeProgressFragment();
                List<Intercession> intercessionList = new Gson().fromJson(responseString, new TypeToken<List<Intercession>>() {
                }.getType());
                setupViewsByInters(intercessionList);
            }

            @Override
            public void onFailure() {
                mIsLoading = false;
                mRefreshLayout.setRefreshing(false);
                removeProgressFragment();
                showNetWorkFailFragment(R.id.framelayout);
                LogUtils.d("XXX", "what");
            }
        });
    }

    private void setupViewsByInters(List<Intercession> intercessionList) {
        if (intercessionList.size() > 0) {
            mStartPage++;
            mIntercessionList.addAll(intercessionList);
            mAdapter.resetData(mIntercessionList);
            return;
        }
        mNoMoreData = true;
        mAdapter.setNoMoreData(mNoMoreData);
    }

    public void dataNofify() {
        mStartPage = 1;
        mNoMoreData = false;
        mIntercessionList.clear();
        loadInterces();
    }

    public static InterCesFragment getInstance(int type) {
        InterCesFragment fragment = new InterCesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }
}
