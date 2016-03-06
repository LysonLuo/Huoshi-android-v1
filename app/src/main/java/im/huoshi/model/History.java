package im.huoshi.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lyson on 16/2/28.
 */
@DatabaseTable(tableName = "histroy")
public class History extends ApiObject {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "key_word", unique = true)
    private String keyWord;

    public int getId() {
        return id;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
