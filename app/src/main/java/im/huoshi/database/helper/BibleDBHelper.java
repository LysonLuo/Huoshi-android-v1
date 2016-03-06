package im.huoshi.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import im.huoshi.BuildConfig;
import im.huoshi.HuoshiApplication;
import im.huoshi.common.Constants;
import im.huoshi.model.Contacts;
import im.huoshi.model.History;

/**
 * Created by Lyson on 16/1/8.
 */
public class BibleDBHelper extends OrmLiteSqliteOpenHelper {
    private Map<String, Dao> mDaos = new HashMap<>();
    private static BibleDBHelper mInstance;


    private BibleDBHelper(Context context) {
        super(context, Constants.DATA_BASE_DIR + Constants.DATA_BASE_NAME, null, BuildConfig.VERSION_CODE);
    }

    private static ThreadLocal<SQLiteDatabase> local = new ThreadLocal<SQLiteDatabase>() {
        @Override
        public SQLiteDatabase get() {
            return SQLiteDatabase.openDatabase(Constants.DATA_BASE_DIR + Constants.DATA_BASE_NAME, null,
                    SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        }
    };

    public static SQLiteDatabase get() {
        return local.get();
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
        try {
            TableUtils.createTable(connectionSource, Contacts.class);
            TableUtils.createTable(connectionSource, History.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
