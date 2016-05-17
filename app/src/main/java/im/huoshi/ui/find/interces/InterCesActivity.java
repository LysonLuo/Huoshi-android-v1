package im.huoshi.ui.find.interces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.ContactsRequest;
import im.huoshi.asynapi.request.InterCesRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.database.dao.ContactsDao;
import im.huoshi.model.Contacts;
import im.huoshi.model.Permission;
import im.huoshi.ui.me.MyPrayerActivity;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ShareUtils;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/26.
 */
public class InterCesActivity extends BaseActivity {
    private InterCesFragment mInterCesFragment;
    private AuthDialog mAuthDialog;
    private Permission mPermission;
    private ContactsDao mContactsDao = new ContactsDao();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interces);
        ViewUtils.inject(this);

        setupViews();
        verifyPermission();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mToolbarUtils.setRightViewIcon(R.mipmap.icon_edite_white);
        mToolbarUtils.setMiddleRightVIewIcon(R.mipmap.icon_prayer_white);
    }

    @Override
    public void onToolBarMiddleRightViewClick(View v) {
        super.onToolBarMiddleRightViewClick(v);
        MyPrayerActivity.launch(InterCesActivity.this);
    }

    @Override
    public void onToolBarRightViewClick(View v) {
        super.onToolBarRightViewClick(v);
        PubInterCesActivity.launch(InterCesActivity.this, false, -1);
    }

    private void setupViews() {
        mToolbarUtils.setTitleText(getString(R.string.text_interces));
        mAuthDialog = new AuthDialog(this);
        mAuthDialog.setAuthListener(new AuthDialog.AuthListener() {
            @Override
            public void OnAuth() {
                //同意授权，读取通讯录
                showProgressDialog("正在授权...");
                List<Contacts> contactsList = ContactsDao.getRawContacts();
                mContactsDao.saveContactsList(contactsList);
                String contacts = new Gson().toJson(contactsList);
                ContactsRequest.asynContacts(InterCesActivity.this, mUser.getUserId(), contacts, new RestApiCallback() {
                    @Override
                    public void onSuccess(String responseString) {
                        dismissProgressDialog();
                        showShortToast("授权成功！");
                        LogUtils.d("lyson", "asyn contacts success");
                        List<Contacts> contactsList = new Gson().fromJson(responseString, new TypeToken<List<Contacts>>() {
                        }.getType());
                        mContactsDao.saveContactsList(contactsList);
                        mAuthDialog.updateUI();
                    }

                    @Override
                    public void onFailure() {
                        dismissProgressDialog();
                        showShortToast("授权失败，请重试！");
                        mAuthDialog.show();
                        LogUtils.d("lyson", "asyn contacts fail");
                    }
                });
            }

            @Override
            public void OnMsg() {
                mAuthDialog.dismiss();
                ShareUtils.smsShare(InterCesActivity.this);
                finish();
            }
        });
    }

    private void verifyPermission() {
        showProgressDialog("正在验证...");
        InterCesRequest.verifyPermission(InterCesActivity.this, mUser.getUserId(), new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                dismissProgressDialog();
                mPermission = new Gson().fromJson(responseString, new TypeToken<Permission>() {
                }.getType());
                showDialogByPermission();
            }

            @Override
            public void onFailure() {
                dismissProgressDialog();
                showShortToast("验证失败，请重试！");
                finish();
            }
        });
    }

    private void showDialogByPermission() {
        if (mPermission.isAuth() == 1 && mPermission.getPermission() == 1) {
            initFragment();
            return;
        }
        if (mPermission.isAuth() == 1 && mPermission.getPermission() != 1) {
            mAuthDialog.updateUI();

        }
        mAuthDialog.show();
    }


    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mInterCesFragment == null) {
            mInterCesFragment = InterCesFragment.getInstance(InterCesFragment.INTECES_TYPE_ALL);
        }
        if (!mInterCesFragment.isAdded()) {
            transaction.add(R.id.layout_content, mInterCesFragment);
        } else {
            transaction.show(mInterCesFragment);
        }
        transaction.commit();
    }

    public static void launch(BaseActivity act) {
        act.startActivity(new Intent(act, InterCesActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PubInterCesActivity.ACTION_PUB_OR_UPDATE_INTERCES) {
            //重新加载数据
            mInterCesFragment.dataNofify();
        }
    }
}
