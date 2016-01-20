package im.huoshi.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import im.huoshi.HuoshiApplication;
import im.huoshi.common.Constants;

/**
 * Created by Lyson on 16/1/8.
 */
public class BibleDBHelper extends OrmLiteSqliteOpenHelper {
    private Map<String, Dao> mDaos = new HashMap<>();
    private static BibleDBHelper mInstance;


    private BibleDBHelper(Context context) {
        super(context, Constants.DATA_BASE_DIR + Constants.DATA_BASE_NAME, null, 1);
    }

    public static synchronized BibleDBHelper getInstance() {
        if (mInstance == null) {
            synchronized (BibleDBHelper.class) {
                if (mInstance == null) {
                    mInstance = new BibleDBHelper(HuoshiApplication.getInstance());
                }
            }
        }
        return mInstance;
    }

    public synchronized Dao getDao(Class clazz) {
        Dao dao = null;
        String className = clazz.getSimpleName();
        try {
            if (mDaos.containsKey(className)) {
                dao = mDaos.get(className);
            }
            if (dao == null) {
                dao = super.getDao(clazz);
                mDaos.put(className, dao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dao;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }
}
