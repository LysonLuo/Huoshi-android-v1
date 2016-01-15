package im.huoshi.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lyson on 16/1/8.
 */
@DatabaseTable(tableName = "section")
public class Section extends ApiObject {
    @DatabaseField(id = true)
    private int seqId;
    @DatabaseField
    private int sectionNo;
    @DatabaseField
    private int sectionIndex;
    @DatabaseField
    private String sectionText;
    @DatabaseField
    private String noteText;
    @DatabaseField
    private int chapterNo;
    @DatabaseField
    private int bookId;

    public int getSeqId() {
        return seqId;
    }

    public int getSectionNo() {
        return sectionNo;
    }

    public int getSectionIndex() {
        return sectionIndex;
    }

    public String getSectionText() {
        return sectionText;
    }

    public String getNoteText() {
        return noteText;
    }

    public int getChapterNo() {
        return chapterNo;
    }

    public int getBookId() {
        return bookId;
    }
}
