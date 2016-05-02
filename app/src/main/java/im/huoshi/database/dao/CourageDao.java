package im.huoshi.database.dao;

import android.database.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

import java.util.List;
import java.util.concurrent.Callable;

import me.lyson.read.TestDBHelper;
import me.lyson.read.model.CourageWord;

/**
 * Created by Lyson on 16/5/2.
 */
public class CourageDao {
    private Dao<CourageWord, Integer> mCourageDao;
    private TestDBHelper mHelper;

    public CourageDao() {
        try {
            mHelper = TestDBHelper.getInstance();
            mCourageDao = mHelper.getDao(CourageWord.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCourage(final List<CourageWord> courageWordList) {
        try {
            TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (CourageWord courageWord : courageWordList) {
                        mCourageDao.createOrUpdate(courageWord);
                    }
                    return null;
                }
            });
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
