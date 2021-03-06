package im.huoshi.ui.bible;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.base.BaseFragment;
import im.huoshi.database.dao.BookDao;
import im.huoshi.model.Book;
import im.huoshi.model.LastHistory;
import im.huoshi.model.event.RefreshBibleEvent;
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
    private LinearLayoutManager mLayoutManager;
    private BibleAdapter mAdapter;
    private boolean mIsNew;//新约、旧约
    private BookDao mBookDao;
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
    public void onEvent(RefreshBibleEvent refreshBibleEvent) {
        mHistory = mLocalRead.getLastHistory();
        mAdapter.updateLastHistory(mHistory);
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

    private void setupViews() {
        mIsNew = getArguments().getBoolean("isNew", false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mHistory = mLocalRead.getLastHistory();
        mAdapter = new BibleAdapter(getActivity(), mBookList, mHistory, mIsNew);
        mRecyclerView.setAdapter(mAdapter);
        mBookDao = new BookDao();
    }

    private void loadBook() {
        mBookList = mBookDao.getList(mIsNew);
        mAdapter.resetData(mBookList);
    }
}
