package im.huoshi.ui.find.interces;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
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
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ShareUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * 代祷详情
 * <p/>
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
    private InterCesDelAdapter mAdapter;
    private RecyclerAdapterWithHF mAdapterHf;
    private Intercession mIntercession;
    private int mIntercessionId;
    private List<Comment> mCommentList = new ArrayList<>();
    private InterCesDialog mIntercesDialog;
    private SwipeRefreshHelper mSwipeRefreshHelper;
    private int mStartPage = 1;
    private boolean mIsLoading = false;
    private boolean mIsFirtResetData = true;
    private boolean mIsPubOrUpdateInterces = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowToolbar(false);
        setContentView(R.layout.activity_interces_details);
        ViewUtils.inject(this);

        setupViews();
    }


    private void setupViews() {
        mIntercessionId = getIntent().getIntExtra("intercession_id", 0);
        mLayoutManager = new LinearLayoutManager(this);
        mRefreshLayout.setColorSchemeColors(Color.BLUE);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new InterCesDelAdapter(this, mIntercession, mCommentList);
        mAdapterHf = new RecyclerAdapterWithHF(mAdapter);
        mRecyclerView.setAdapter(mAdapterHf);
        mSwipeRefreshHelper = new SwipeRefreshHelper(mRefreshLayout);
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
        mSwipeRefreshHelper.setOnSwipeRefreshListener(new SwipeRefreshHelper.OnSwipeRefreshListener() {
            @Override
            public void onfresh() {
                resetStatus();
                loadInterces();
            }
        });
        mSwipeRefreshHelper.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadComments();
            }
        });
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshHelper.autoRefresh();
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
        mSwipeRefreshHelper.setLoadMoreEnable(false);
        mStartPage = 1;
        mCommentList.clear();
    }

    private void joinInterces() {
        if (mIntercession.isInterceded()) {
            showShortToast("已加入过该代祷！");
            return;
        }
        mIntercesDialog = new InterCesDialog(InterCesDetailsActivity.this, mLocalRead.getTotalJoinIntercession());
        mIntercesDialog.setIntercesListener(new InterCesDialog.IntercesListener() {
            @Override
            public void onFinish() {
                int dayBetweenInterces = DateUtils.getDayBetween(mLocalRead.getLastIntercesTime());
                int continuousIntercesDays = mLocalRead.getContinuousIntercesDays();
                //只有时间间隔为1的时候，连续代祷天数累加
                if (dayBetweenInterces == 1) {
                    continuousIntercesDays++;
                } else if (dayBetweenInterces == 0) {

                } else {
                    continuousIntercesDays = 1;
                }
                InterCesRequest.joinInterces(InterCesDetailsActivity.this, mUser.getUserId(), mIntercessionId, continuousIntercesDays, System.currentTimeMillis(), new RestApiCallback() {
                    @Override
                    public void onSuccess(String responseString) {
                        int totalTimes;
                        try {
                            JSONObject jsonObject = new JSONObject(responseString);
                            totalTimes = jsonObject.getInt("total_join_intercession");
                            mLocalRead.updateTotalJoinIntercession(totalTimes);
                            mLocalRead.updateContinuousIntercesDays(jsonObject.getInt("continuous_interces_days"));
                            mLocalRead.updateLastIntercesTime(jsonObject.getLong("last_interces_time"));
                            mIntercession.setInterceded(true);
                            mIntercesDialog.finishJoinInterces();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure() {
                        LogUtils.d("XXX", "what");
                    }
                });
            }

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

    private void loadComments() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        InterCesRequest.intercesComments(this, mUser.getUserId(), mIntercessionId, mStartPage, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                mIsLoading = false;
                mSwipeRefreshHelper.setLoadMoreEnable(true);
                mSwipeRefreshHelper.refreshComplete();
                List<Comment> commentList = new Gson().fromJson(responseString, new TypeToken<List<Comment>>() {
                }.getType());
                setupViewsByComments(commentList);
            }

            @Override
            public void onFailure() {
                mIsLoading = false;
                mSwipeRefreshHelper.loadMoreComplete(true);
            }
        });
    }

    private void setupViewsByComments(List<Comment> commentList) {
        mSwipeRefreshHelper.loadMoreComplete(true);
        if (commentList.size() > 0) {
            mIsFirtResetData = false;
            mStartPage++;
            mCommentList.addAll(commentList);
            mAdapter.resetData(mIntercession, mCommentList);
            return;
        }
        mSwipeRefreshHelper.setNoMoreData();
        mSwipeRefreshHelper.setLoadMoreEnable(false);
        if (mIsFirtResetData) {
            mAdapter.resetData(mIntercession, mCommentList);
            mIsFirtResetData = false;
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == CommentActivity.ACTION_PUB_COMMENT) {
            resetStatus();
            loadComments();
        }
        if (requestCode == PubInterCesActivity.ACTION_PUB_OR_UPDATE_INTERCES) {
            mIsPubOrUpdateInterces = true;
            loadInterces();
        }
    }

    private void loadInterces() {
        InterCesRequest.intercesById(InterCesDetailsActivity.this, mUser.getUserId(), mIntercessionId, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                mIntercession = new Gson().fromJson(responseString, new TypeToken<Intercession>() {
                }.getType());
                if (mIsPubOrUpdateInterces) {
                    setupViewsByIntercession();
                    mIsPubOrUpdateInterces = false;
                    mSwipeRefreshHelper.setLoadMoreEnable(true);
                    mSwipeRefreshHelper.refreshComplete();
                    return;
                }
                loadComments();
            }

            @Override
            public void onFailure() {
                LogUtils.d("XXX", "what");
            }
        });
    }

    private void setupViewsByIntercession() {
        mAdapter.resetData(mIntercession);
    }

    public static void launch(BaseActivity activity, int intercessionId) {
        activity.startActivity(new Intent(activity, InterCesDetailsActivity.class).putExtra("intercession_id", intercessionId));
    }
}
