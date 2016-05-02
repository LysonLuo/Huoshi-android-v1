package im.huoshi.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 鼓励语
 * <p>
 * Created by Lyson on 16/5/2.
 */
@DatabaseTable(tableName = "courage")
public class CourageWord extends ApiObject {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String word;
    @DatabaseField
    private int type;//类别：小于一分钟1，1分钟到60分钟2，大于等于60分钟3，连续阅读天数大于30||今日阅读时间大于120分钟4
    @DatabaseField(columnName = "is_selected")
    private boolean isSelected;//是否上次显示

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public int getType() {
        return type;
    }

    public boolean isSelected() {
        return isSelected;
    }


    public void setWord(String word) {
        this.word = word;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
