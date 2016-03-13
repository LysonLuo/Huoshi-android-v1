package im.huoshi.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.ContactsRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.database.dao.ContactsDao;
import im.huoshi.model.Contacts;
import im.huoshi.utils.LogUtils;

/**
 * Created by Lyson on 16/2/24.
 */
public class AsynDataService extends Service {
    private ContactsBinder mBinder = new ContactsBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("lyson", "service create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("lyson", "service onstartcommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtils.d("lyson", "service destory");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class ContactsBinder extends Binder {
        private ContactsDao mContactsDao = new ContactsDao();

        public void asynContacts(final BaseActivity activity, final int userId) {
            LogUtils.d("lyson", "get contacts from phone");
            List<Contacts> contactsList = ContactsDao.getRawContacts();
            mContactsDao.saveContactsList(contactsList);
            String contacts = new Gson().toJson(contactsList);
            ContactsRequest.asynContacts(activity, userId, contacts, new RestApiCallback() {
                @Override
                public void onSuccess(String responseString) {
                    LogUtils.d("lyson", "asyn contacts success");
                    List<Contacts> contactsList = new Gson().fromJson(responseString, new TypeToken<Contacts>() {
                    }.getType());
                    mContactsDao.saveContactsList(contactsList);
                    stopSelf();
                }

                @Override
                public void onFailure() {
                    LogUtils.d("lyson", "asyn contacts fail");
                }
            });
        }
    }
}
