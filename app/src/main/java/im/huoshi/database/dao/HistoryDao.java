package im.huoshi.database.dao;

import android.database.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.database.helper.BibleDBHelper;
import im.huoshi.model.History;

/**
 * Created by Lyson on 16/2/28.
 */
public class HistoryDao {
    private Dao<History, Integer> mHistoryDao;
    private BibleDBHelper mHelper;

    public HistoryDao() {
        try {
            mHelper = BibleDBHelper.getInstance();
            mHistoryDao = mHelper.getDao(History.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<History> getList() {
        List<History> historyList = new ArrayList<>();
        try {
            historyList = mHistoryDao.queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    public void addHistory(String keyWord) {
        History history = new History();
        history.setKeyWord(keyWord);
        try {
            if (mHistoryDao.queryForEq("key_word",keyWord).size() > 0){
                return;
            }
            mHistoryDao.createOrUpdate(history);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearHistory(){
        DeleteBuilder<History, Integer>  deleteBuilder = mHistoryDao.deleteBuilder();
        deleteBuilder.reset();
        try {
            PreparedDelete<History> preparedDelete = deleteBuilder.prepare();
            mHistoryDao.delete(preparedDelete);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
