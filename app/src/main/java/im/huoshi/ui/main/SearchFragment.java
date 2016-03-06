package im.huoshi.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.base.BaseFragment;
import im.huoshi.common.OnRecClickListener;
import im.huoshi.database.dao.ChapterDao;
import im.huoshi.database.dao.HistoryDao;
import im.huoshi.database.helper.BibleDBHelper;
import im.huoshi.model.Chapter;
import im.huoshi.model.History;
import im.huoshi.model.SearchResult;
import im.huoshi.ui.bible.ChapterDetailsActivity;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/2/26.
 */
public class SearchFragment extends BaseFragment {
    @ViewInject(R.id.layout_history)
    private LinearLayout mHistoryLayout;
    @ViewInject(R.id.layout_result)
    private LinearLayout mResultLayout;
    @ViewInject(R.id.imageview_back)
    private ImageView mBackImageView;
    @ViewInject(R.id.edit_bible_search)
    private EditText mSearchEdit;
    @ViewInject(R.id.textview_search)
    private TextView mSearchTextView;
    @ViewInject(R.id.textview_search_result)
    private TextView mResultTextView;
    @ViewInject(R.id.textview_clear_history)
    private TextView mClearTextView;
    @ViewInject(R.id.recyclerview_history)
    private RecyclerView mHistoryRecyclerView;
    @ViewInject(R.id.recyclerview_result)
    private RecyclerView mResultRecyclerView;
    private ResultRecAdapter mResultAdapter;
    private HistoryRecAdapter mHistoryAdapter;
    private HistoryDao mHistoryDao;
    private ChapterDao mChapterDao;
    private List<History> mHistoryList = new ArrayList<>();
    private List<SearchResult> mResultList = new ArrayList<>();
    private String mKeyWord = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_search, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        loadHistory();
        return contentView;
    }

    private void setupViews() {
        toggleSearchEditTextFocus(false);
        mHistoryDao = new HistoryDao();
        mChapterDao = new ChapterDao();
        mHistoryAdapter = new HistoryRecAdapter(getActivity(), mHistoryList, new OnRecClickListener<History>() {
            @Override
            public void OnClick(History history) {
                mKeyWord = history.getKeyWord();
                loadResult();
            }
        });
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.remove(SearchFragment.this);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                transaction.commit();
            }
        });
        mClearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHistoryDao.clearHistory();
                loadHistory();
            }
        });
        mSearchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyWord = mSearchEdit.getText().toString().trim();
                if (TextUtils.isEmpty(mKeyWord)) {
                    return;
                }
                mHistoryDao.addHistory(mKeyWord);
                loadResult();
            }
        });
    }

    private void loadResult() {
        mResultList.clear();
        mHistoryLayout.setVisibility(View.GONE);
        mResultLayout.setVisibility(View.VISIBLE);
        String sql = "SELECT section.seqId as _id , bookId , bookName , chapterNo , sectionNo , sectionText , sectionIndex  from section join book "
                + "on section.bookId = book.seqId "
                + "where sectionText like ? and title=0 order by sectionIndex ";
        Cursor cursor = BibleDBHelper.get().rawQuery(sql, new String[]{"%" + mKeyWord + "%"});
        while (cursor.moveToNext()) {
            SearchResult searchResult = new SearchResult();
            searchResult.setBookId(cursor.getInt(cursor.getColumnIndex("bookId")));
            searchResult.setBookName(cursor.getString(cursor.getColumnIndex("bookName")));
            searchResult.setChapter(cursor.getInt(cursor.getColumnIndex("chapterNo")));
            searchResult.setSection(cursor.getInt(cursor.getColumnIndex("sectionNo")));
            searchResult.setSectionText(cursor.getString(cursor.getColumnIndex("sectionText")));
            mResultList.add(searchResult);
        }
        mResultTextView.setText("以下是'" + mKeyWord + "'的搜索结果,共" + mResultList.size() + "条");
        mResultRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mResultAdapter = new ResultRecAdapter(getActivity(), mKeyWord, mResultList, new OnRecClickListener<SearchResult>() {
            @Override
            public void OnClick(SearchResult searchResult) {
                ArrayList<Chapter> chapters = mChapterDao.getList(searchResult.getBookId());
                ChapterDetailsActivity.launch((BaseActivity) getActivity(), searchResult.getBookName(), chapters, searchResult.getChapter() - 1);
            }
        });
        mResultRecyclerView.setAdapter(mResultAdapter);
    }

    private void loadHistory() {
        mHistoryList = mHistoryDao.getList();
        mHistoryAdapter.resetData(mHistoryList);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toggleSearchEditTextFocus(true);
    }

    private void toggleSearchEditTextFocus(boolean isCloseKeyboard) {
        if (isCloseKeyboard) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mSearchEdit.getWindowToken(), 0);
            return;
        }
        mSearchEdit.requestFocus();
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);

    }
}
