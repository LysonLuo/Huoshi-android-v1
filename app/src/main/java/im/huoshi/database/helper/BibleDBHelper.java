package im.huoshi.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import im.huoshi.HuoshiApplication;

/**
 * Created by Lyson on 16/1/8.
 */
public class BibleDBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATA_BASE_NAME = "huoshi.db";
    private static final String DATA_BASE_PATH = "/data/data/im.huoshi/databases";
    private Map<String, Dao> mDaos = new HashMap<>();
    private static BibleDBHelper mInstance;


    private BibleDBHelper(Context context) {
        super(context, DATA_BASE_PATH + DATA_BASE_NAME, null, 1);
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            try {
                File dir = new File(DATA_BASE_PATH);
                dir.mkdirs();
                InputStream myinput = context.getAssets().open(DATA_BASE_NAME);
                String outfilename = DATA_BASE_PATH + DATA_BASE_NAME;
                OutputStream myoutput = new FileOutputStream(outfilename);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myinput.read(buffer)) > 0) {
                    myoutput.write(buffer, 0, length);
                }
                myoutput.flush();
                myoutput.close();
                myinput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    private boolean checkDatabase() {
        String dbPath = DATA_BASE_PATH + DATA_BASE_NAME;
        File dbFile = new File(dbPath);
        return dbFile.exists();
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
