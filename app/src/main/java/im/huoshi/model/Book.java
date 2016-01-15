package im.huoshi.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lyson on 16/1/8.
 */
@DatabaseTable(tableName = "book")
public class Book extends ApiObject {
    @DatabaseField(id = true)
    private int seqId;
    @DatabaseField
    private int bookNo;
    @DatabaseField
    private String bookName;
    @DatabaseField
    private String shortBookName;
    @DatabaseField
    private boolean isNew;
    @DatabaseField
    private int chapterCount;
    @DatabaseField
    private int bookSetId;
    @DatabaseField
    private String bookSetName;

    public int getSeqId() {
        return seqId;
    }

    public int getBookNo() {
        return bookNo;
    }

    public String getBookName() {
        return bookName;
    }

    public String getShortBookName() {
        return shortBookName;
    }

    public boolean isNew() {
        return isNew;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public int getBookSetId() {
        return bookSetId;
    }

    public String getBookSetName() {
        return bookSetName;
    }
}
