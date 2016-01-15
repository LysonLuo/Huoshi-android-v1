package im.huoshi.database.dao;

import android.database.SQLException;
import com.j256.ormlite.dao.Dao;
import im.huoshi.database.helper.BibleDBHelper;
import im.huoshi.model.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lyson on 16/1/11.
 */
public class SectionDao {
    private Dao<Section, Integer> mSectionDao;
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
            Map<String, Object> params = new HashMap<>();
            params.put("bookId", bookId);
            params.put("chapterNo", chapterNo);
            sectionList = mSectionDao.queryForFieldValues(params);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return sectionList;
    }
}
