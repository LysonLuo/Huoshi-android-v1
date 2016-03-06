package im.huoshi.model;

/**
 * Created by Lyson on 16/2/28.
 */
public class SearchResult extends ApiObject {
    private int bookId;
    private String bookName;
    private int chapter;
    private int section;
    private String sectionText;

    public int getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public int getChapter() {
        return chapter;
    }

    public int getSection() {
        return section;
    }

    public String getSectionText() {
        return sectionText;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public void setSectionText(String sectionText) {
        this.sectionText = sectionText;
    }
}
