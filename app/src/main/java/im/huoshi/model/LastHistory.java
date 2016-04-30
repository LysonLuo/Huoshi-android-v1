package im.huoshi.model;

/**
 * Created by Lyson on 16/4/28.
 */
public class LastHistory extends ApiObject {
    private long time;
    private int bookId;
    private String bookName;
    private int chapterNo;
    private int sectionNo;
    private int lastPosition;

    public long getTime() {
        return time;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public int getChapterNo() {
        return chapterNo;
    }

    public int getSectionNo() {
        return sectionNo;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setChapterNo(int chapterNo) {
        this.chapterNo = chapterNo;
    }

    public void setSectionNo(int sectionNo) {
        this.sectionNo = sectionNo;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }
}
