package im.huoshi.database.dao;

import android.database.SQLException;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.database.helper.BibleDBHelper;
import im.huoshi.model.Chapter;

/**
 * Created by Lyson on 16/1/8.
 */
public class ChapterDao {
    private Dao<Chapter, Integer> mChapterDao;
    private BibleDBHelper mHelper;

    public ChapterDao() {
        try {
            mHelper = BibleDBHelper.getInstance();
            mChapterDao = mHelper.getDao(Chapter.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Chapter> getList(int bookId) {
        List<Chapter> chapterList = new ArrayList<>();
        try {
            chapterList = mChapterDao.queryForEq("bookId", bookId);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return chapterList;
    }
}
