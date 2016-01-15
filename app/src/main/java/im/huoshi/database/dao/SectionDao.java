package im.huoshi.database.dao;

import android.database.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.database.helper.BibleDBHelper;
import im.huoshi.model.Section;

/**
 * Created by Lyson on 16/1/11.
 */
public class SectionDao {
    private Dao<Section, String> mSectionDao;
    private BibleDBHelper mHelper;

    public SectionDao() {
        try {
            mHelper = BibleDBHelper.getInstance();
            mSectionDao = mHelper.getDao(Section.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param bookId
     * @param chapterNo
     * @return
     */
    public List<Section> getList(int bookId, int chapterNo) {
        List<Section> sectionList = new ArrayList<>();
        try {
            QueryBuilder<Section, String> queryBuilder = mSectionDao.queryBuilder();
            queryBuilder.orderBy("sectionIndex", true);
            queryBuilder.where().eq("bookId", bookId).and().eq("chapterNo", chapterNo);
            PreparedQuery<Section> preparedQuery = queryBuilder.prepare();
            sectionList = mSectionDao.query(preparedQuery);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return sectionList;
    }
}
