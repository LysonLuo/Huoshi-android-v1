package im.huoshi.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lyson on 16/2/25.
 */
@DatabaseTable(tableName = "contacts")
public class Contacts extends ApiObject {
    @DatabaseField(id = true)
    @SerializedName("contacts_id")
    private String contactsId;
    @DatabaseField
    @SerializedName("contacts_name")
    private String contactsName;
    @DatabaseField
    private String phones;
    @SerializedName("contacts_type")
    @DatabaseField
    private int contactsType;//2未注册,1好友

    public String getContactsId() {
        return contactsId;
    }

    public String getContactsName() {
        return contactsName;
    }

    public String getPhones() {
        return phones;
    }

    public int getContactsType() {
        return contactsType;
    }

    public void setContactsId(String contactsId) {
        this.contactsId = contactsId;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public void setContactsType(int contactsType) {
        this.contactsType = contactsType;
    }
}
