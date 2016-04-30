package im.huoshi.ui.bible;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.base.BaseFragment;
import im.huoshi.database.dao.BookDao;
import im.huoshi.database.dao.ChapterDao;
import im.huoshi.model.Book;
import im.huoshi.model.Chapter;
import im.huoshi.model.LastHistory;
import im.huoshi.model.event.RefreshEvent;
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * 新约、旧约复用界面
 * <p>
 * Created by Lyson on 15/12/25.
 */
public class BibleContentFragment extends BaseFragment {
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.ll_last_history)
    private LinearLayout mHistoryLayout;
    @ViewInject(R.id.tv_history_time)
    private TextView mTvHistoryTime;
    @ViewInject(R.id.tv_book_detail)
    private TextView mTvBookDetail;

    private LinearLayoutManager mLayoutManager;
    private BibleAdapter mAdapter;
    private boolean mIsNew;//新约、旧约
    private BookDao mBookDao;
    private ChapterDao mChapterDao;
    private LastHistory mHistory;

    private List<Book> mBookList = new ArrayList<>();


    public static BibleContentFragment getInstance(boolean isNew) {
        BibleContentFragment fragment = new BibleContentFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isNew", isNew);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(RefreshEvent refreshEvent) {
        setupViewsByHistory();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_bible_content, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        loadBook();
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupViewsByHistory();
    }

    private void setupViewsByHistory() {
        if (mIsNew) {
            mHistory = mLocalRead.getLastHistory();
            if (mHistory != null && mHistory.getBookId() != 0) {
                mHistoryLayout.setVisibility(View.VISIBLE);
                mTvHistoryTime.setText(DateUtils.formatToString(mHistory.getTime(), "MM/dd HH:MM"));
                mTvBookDetail.setText(" 读到 ：" + mHistory.getBookName() + " " + mHistory.getChapterNo() + "章" + mHistory.getSectionNo() + "节");
                return;
            }
            mHistoryLayout.setVisibility(View.GONE);
            return;
        }
        mHistoryLayout.setVisibility(View.GONE);
    }

    private void setupViews() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new BibleAdapter(getActivity(), mBookList);
        mRecyclerView.setAdapter(mAdapter);
        mIsNew = getArguments().getBoolean("isNew", false);
        mBookDao = new BookDao();
        mChapterDao = new ChapterDao();
        mHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Chapter> chapters = mChapterDao.getList(mHistory.getBookId());
                ChapterDetailsActivity.launch((BaseActivity) getActivity(), mHistory.getBookName(), mHistory.getBookId(), "", chapters, mHistory.getChapterNo() - 1, mHistory.getLastPosition());
            }
        });
    }

    private void loadBook() {
        mBookList = mBookDao.getList(mIsNew);
        mAdapter.resetData(mBookList);
    }
}
