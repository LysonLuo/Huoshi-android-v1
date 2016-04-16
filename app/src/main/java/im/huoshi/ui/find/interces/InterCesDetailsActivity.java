package im.huoshi.ui.find.interces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.InterCesRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.Comment;
import im.huoshi.model.Intercession;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ShareUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;
import im.huoshi.views.RecyclerViewScrollListener;

/**
 * 代祷详情
 * <p>
 * Created by Lyson on 15/12/26.
 */
public class InterCesDetailsActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_left_view)
    private TextView mLeftTextView;
    @ViewInject(R.id.toolbar_right_view)
    private TextView mRightTextView;
    @ViewInject(R.id.toolbar_middle_right_view)
    private TextView mMiddleRightTextView;
    @ViewInject(R.id.refresh_layout)
    private SwipeRefreshLayout mRefreshLayout;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.fabButton)
    private FloatingActionButton mFabButton;
    private LinearLayoutManager mLayoutManager;
    private InterCesDelRecAdapter mAdapter;
    private Intercession mIntercession;
    private int mIntercessionId;
    private List<Comment> mCommentList = new ArrayList<>();
    private InterCesDialog mIntercesDialog;
    private RecyclerViewScrollListener mScrollListener;
    private int mStartPage = 1;
    private boolean mIsLoadMore = false;
    private boolean mNoMoreData = false;
    private boolean mIsLoading = false;
    private boolean mIsFirtResetData = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowToolbar(false);
        setContentView(R.layout.activity_interces_details);
        ViewUtils.inject(this);

        setupViews();
        loadInterces();
    }


    private void setupViews() {
        mIntercessionId = getIntent().getIntExtra("intercession_id", 0);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new InterCesDelRecAdapter(this, mIntercession, mCommentList);
        mRecyclerView.setAdapter(mAdapter);
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.init(InterCesDetailsActivity.this);
            }
        });
        mMiddleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.launch(InterCesDetailsActivity.this, mIntercessionId);
            }
        });
        mScrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                if (!mNoMoreData) {
                    mIsLoadMore = true;
                    loadComments();
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetStatus();
                loadInterces();
            }
        });
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自己发布的代祷，点击操作是更新代祷，否则是加入代祷
                if (mIntercession.getUserId() == mUser.getUserId()) {
                    PubInterCesActivity.launch(InterCesDetailsActivity.this, true, mIntercessionId);
                    return;
                }
                joinInterces();
            }
        });
    }

    private void resetStatus() {
        mStartPage = 1;
        mNoMoreData = false;
        mCommentList.clear();
        mAdapter.setNoMoreData(mNoMoreData);
        mScrollListener.reSetPreviousTotal();
    }

    private void joinInterces() {
        if (mIntercession.isInterceded()) {
            showShortToast("已加入过该代祷！");
            return;
        }
        InterCesRequest.joinInterces(InterCesDetailsActivity.this, mUser.getUserId(), mIntercessionId, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                int totalTimes = 0;
                try {
                    totalTimes = new JSONObject(responseString).getInt("total_join_intercession");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mIntercesDialog = new InterCesDialog(InterCesDetailsActivity.this, totalTimes);
                mIntercesDialog.setIntercesListener(new InterCesDialog.IntercesListener() {
                    @Override
                    public void onBless() {
                        CommentActivity.launch(InterCesDetailsActivity.this, mIntercessionId);
                    }

                    @Override
                    public void onShare() {
                        ShareUtils.init(InterCesDetailsActivity.this);
                    }

                    @Override
                    public void onClose() {
                        finish();
                    }
                });
                mIntercesDialog.show();
            }

            @Override
            public void onFailure() {
                LogUtils.d("XXX", "what");
            }
        });
    }

    private void loadComments() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        if (!mIsLoadMore) {
            showProgressFragment();
        }
        InterCesRequest.intercesComments(this, mUser.getUserId(), mIntercessionId, mStartPage, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                removeProgressFragment();
                mIsLoading = false;
                mRefreshLayout.setRefreshing(false);
                List<Comment> commentList = new Gson().fromJson(responseString, new TypeToken<List<Comment>>() {
                }.getType());
                setupViewsByComments(commentList);
            }

            @Override
            public void onFailure() {
                removeProgressFragment();
                mIsLoading = false;
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setupViewsByComments(List<Comment> commentList) {
        if (commentList.size() > 0) {
            mIsFirtResetData = false;
            mStartPage++;
            mCommentList.addAll(commentList);
            mAdapter.resetData(mIntercession, mCommentList);
            return;
        }
        if (mIsFirtResetData) {
            mAdapter.resetData(mIntercession, mCommentList);
            mIsFirtResetData = false;
            return;
        }
        mNoMoreData = true;
        mAdapter.setNoMoreData(mNoMoreData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == CommentActivity.ACTION_PUB_COMMENT) {
            loadComments();
        }
        if (requestCode == PubInterCesActivity.ACTION_PUB_OR_UPDATE_INTERCES) {
            resetStatus();
            loadInterces();
        }
    }

    private void loadInterces() {
        InterCesRequest.intercesById(InterCesDetailsActivity.this, mUser.getUserId(), mIntercessionId, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                mIntercession = new Gson().fromJson(responseString, new TypeToken<Intercession>() {
                }.getType());
                loadComments();
            }

            @Override
            public void onFailure() {
                LogUtils.d("XXX", "what");
            }
        });
    }

    public static void launch(BaseActivity activity, int intercessionId) {
        activity.startActivity(new Intent(activity, InterCesDetailsActivity.class).putExtra("intercession_id", intercessionId));
    }
}
