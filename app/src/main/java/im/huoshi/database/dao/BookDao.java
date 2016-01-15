package im.huoshi.database.dao;

import android.database.SQLException;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

import im.huoshi.database.helper.BibleDBHelper;
import im.huoshi.model.Book;

/**
 * Created by Lyson on 16/1/8.
 */
public class BookDao {
    private Dao<Book, Integer> mBookDao;
    private BibleDBHelper mHelper;

    public BookDao() {
        try {
            mHelper = BibleDBHelper.getInstance();
            mBookDao = mHelper.getDao(Book.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param isNew
     * @return
     */
    public List<Book> getList(boolean isNew) {
        List<Book> bookList = new ArrayList<>();
        try {
            bookList = mBookDao.queryForEq("isNew", isNew);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }
}
