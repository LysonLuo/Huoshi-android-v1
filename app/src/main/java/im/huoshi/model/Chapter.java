package im.huoshi.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lyson on 16/1/8.
 */
@DatabaseTable(tableName = "chapter")
public class Chapter extends ApiObject {
    @DatabaseField(id = true)
    private int seqId;
    @DatabaseField
    private int chapterNo;
    @DatabaseField
    private int chapterIndex;
    @DatabaseField
    private int bookId;

    public int getSeqId() {
        return seqId;
    }

    public int getChapterNo() {
        return chapterNo;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public int getBookId() {
        return bookId;
    }
}
