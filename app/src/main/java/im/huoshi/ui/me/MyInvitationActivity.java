package im.huoshi.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.database.dao.ContactsDao;
import im.huoshi.model.Contacts;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/4/30.
 */
public class MyInvitationActivity extends BaseActivity {
    @ViewInject(R.id.rv_invitation)
    private RecyclerView mRvInvitation;
    private InvitatAdapter mAdapter;
    private ContactsDao mContactsDao;
    private List<Contacts> mContactsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invitation);
        ViewUtils.inject(this);
        setupViews();
        loadContacts();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mToolbarUtils.setTitleText(getResources().getString(R.string.text_my_invitation));
    }

    private void loadContacts() {
        mContactsList = mContactsDao.getContacts();
        mAdapter = new InvitatAdapter(mContactsList, this);
        mRvInvitation.setAdapter(mAdapter);
    }

    private void setupViews() {
        mContactsDao = new ContactsDao();
        mRvInvitation.setLayoutManager(new LinearLayoutManager(this));
    }

    public static void launch(BaseActivity activity) {
        Intent intent = new Intent(activity, MyInvitationActivity.class);
        activity.startActivity(intent);
    }
}
