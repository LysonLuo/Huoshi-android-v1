package im.huoshi.database.dao;

import android.database.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.database.helper.BibleDBHelper;
import im.huoshi.model.CourageWord;


/**
 * Created by Lyson on 16/5/2.
 */
public class CourageDao {
    private Dao<CourageWord, String> mCourageDao;
    private BibleDBHelper mHelper;

    public CourageDao() {
        try {
            mHelper = BibleDBHelper.getInstance();
            mCourageDao = mHelper.getDao(CourageWord.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CourageWord getCourageByTypeAndStatus(int type, boolean isSelected) {
        List<CourageWord> courageWords = new ArrayList<>();
        try {
            QueryBuilder<CourageWord, String> queryBuilder = mCourageDao.queryBuilder();
            queryBuilder.where().eq("type", type).and().eq("is_selected", isSelected);
            PreparedQuery<CourageWord> preparedQuery = queryBuilder.prepare();
            courageWords = mCourageDao.query(preparedQuery);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return courageWords.get(0);
    }

    /**
     * 获取每种类型的鼓励语数量
     *
     * @param type
     * @return
     */
    public List<CourageWord> getCourageByType(int type) {
        List<CourageWord> courageWords = new ArrayList<>();
        try {
            QueryBuilder<CourageWord, String> queryBuilder = mCourageDao.queryBuilder();
            queryBuilder.orderBy("id", true);
            queryBuilder.where().eq("type", type);
            PreparedQuery<CourageWord> preparedQuery = queryBuilder.prepare();
            courageWords = mCourageDao.query(preparedQuery);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return courageWords;
    }

    /**
     * 根据类型和id更新状态
     *
     * @param type
     * @param id
     * @param isSelected
     */
    public void updateStatusByTypeAndId(int type, int id, boolean isSelected) {
        try {
            UpdateBuilder<CourageWord, String> updateBuilder = mCourageDao.updateBuilder();
            updateBuilder.updateColumnValue("is_selected", isSelected);
            updateBuilder.where().eq("type", type).and().eq("id", id);
            PreparedUpdate<CourageWord> preparedUpdate = updateBuilder.prepare();
            mCourageDao.update(preparedUpdate);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
