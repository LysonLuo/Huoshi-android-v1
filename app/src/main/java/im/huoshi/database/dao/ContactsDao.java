package im.huoshi.database.dao;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.ContactsContract;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import im.huoshi.HuoshiApplication;
import im.huoshi.database.helper.BibleDBHelper;
import im.huoshi.model.Contacts;

/**
 * Created by Lyson on 16/2/25.
 */
public class ContactsDao {
    private Dao<Contacts, Integer> mContactsDao;
    private BibleDBHelper mHelper;

    public ContactsDao() {
        try {
            mHelper = BibleDBHelper.getInstance();
            mContactsDao = mHelper.getDao(Contacts.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从通讯录获取联系人列表
     *
     * @return
     */
    public static List<Contacts> getRawContacts() {
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        ContentResolver contentResolver = HuoshiApplication.getInstance().getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        List<Contacts> contacts = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Contacts contact = new Contacts();
                String contactsId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactsName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactsId, null, null);

                StringBuilder stringBuilder = new StringBuilder();
                while (phones.moveToNext()) {
                    int phoneFieldIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phoneNumber = phones.getString(phoneFieldIndex).replaceAll(" ", "");
                    stringBuilder.append(phoneNumber + ",");
                }
                contact.setContactsId(contactsId);
                contact.setContactsName(contactsName);
                contact.setPhones(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
                contacts.add(contact);
                if (phones != null) {
                    phones.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.close();
        }
        return contacts;
    }

    /**
     * 保存联系人列表
     *
     * @param contactsList
     */
    public void saveContactsList(final List<Contacts> contactsList) {
        try {
            mContactsDao.delete(getContacts());
            TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (Contacts contact : contactsList) {
                        mContactsDao.createOrUpdate(contact);
                    }
                    return null;
                }
            });
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库获取联系人列表
     *
     * @return
     */
    public List<Contacts> getContacts() {
        List<Contacts> contactsList = new ArrayList<>();
        try {
            contactsList = mContactsDao.queryForAll();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return contactsList;
    }
}
