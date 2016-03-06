package im.huoshi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lyson on 16/1/8.
 */
@DatabaseTable(tableName = "chapter")
public class Chapter implements Parcelable {
    @DatabaseField(id = true)
    private int seqId;
    @DatabaseField
    private int chapterNo;
    @DatabaseField
    private int chapterIndex;
    @DatabaseField
    private int bookId;

    public Chapter() {
    //getdao needs it
    }

    public Chapter(int seqId, int chapterNo, int chapterIndex, int bookId) {
        super();
        this.seqId = seqId;
        this.chapterNo = chapterNo;
        this.chapterIndex = chapterIndex;
        this.bookId = bookId;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 序列化过程：必须按成员变量声明的顺序进行封装
        dest.writeInt(seqId);
        dest.writeInt(chapterNo);
        dest.writeInt(chapterIndex);
        dest.writeInt(bookId);
    }

    // 反序列过程：必须实现Parcelable.Creator接口，并且对象名必须为CREATOR
    // 读取Parcel里面数据时必须按照成员变量声明的顺序，Parcel数据来源上面writeToParcel方法，读出来的数据供逻辑层使用
    public static final Parcelable.Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel source) {
            return new Chapter(source.readInt(), source.readInt(), source.readInt(), source.readInt());
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
}
