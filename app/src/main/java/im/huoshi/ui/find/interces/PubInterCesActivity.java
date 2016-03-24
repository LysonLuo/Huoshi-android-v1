package im.huoshi.ui.find.interces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.InterCesRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.utils.AmapUtils;
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/28.
 */
public class PubInterCesActivity extends BaseActivity {
    public static final int ACTION_PUB_INTERCES = 1;
    @ViewInject(R.id.edit_interces_content)
    private EditText mContentEdit;
    @ViewInject(R.id.checkbox_interces_loc)
    private CheckBox mLocCheckBox;
    @ViewInject(R.id.checkbox_interces_pre)
    private CheckBox mPreCheckBox;
    @ViewInject(R.id.textview_interces_year)
    private TextView mYearTextView;
    @ViewInject(R.id.textview_interces_month)
    private TextView mMonthTextView;
    @ViewInject(R.id.textview_interces_day)
    private TextView mDayTextView;
    @ViewInject(R.id.edit_interces_hour)
    private EditText mHourEdit;
    private boolean mLocSuccess = true;

    private TimePickerView mTimerPickerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_interces);
        ViewUtils.inject(this);

        setupViews();
        getLocation();
    }

    private void setupViews() {
        mTimerPickerView = new TimePickerView(PubInterCesActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        mTimerPickerView.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
        mTimerPickerView.setTime(new Date());
        mTimerPickerView.setCyclic(false);
        mTimerPickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                setPreUpdateTime(true, date.getTime());
            }
        });
        mLocCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerPickerView.show();
            }
        };
        mYearTextView.setOnClickListener(onClickListener);
        mMonthTextView.setOnClickListener(onClickListener);
        mDayTextView.setOnClickListener(onClickListener);
        mPreCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPreUpdateTime(isChecked, 0);
            }
        });

    }

    private void setPreUpdateTime(boolean isChecked, long time) {
        mYearTextView.setEnabled(isChecked);
        mMonthTextView.setEnabled(isChecked);
        mDayTextView.setEnabled(isChecked);
        mHourEdit.setEnabled(isChecked);
        if (isChecked) {
            String currentTime = DateUtils.formatToString(time == 0 ? (System.currentTimeMillis()) : time, "yyyy,MM,dd,HH");
            String[] times = currentTime.split(",");
            mYearTextView.setText(times[0]);
            mMonthTextView.setText(times[1]);
            mDayTextView.setText(times[2]);
            if (time == 0) {
                mHourEdit.setText((Integer.parseInt(times[3]) + 1) + "");
            }
            return;
        }
        mYearTextView.setText("");
        mMonthTextView.setText("");
        mDayTextView.setText("");
        mHourEdit.setText("");
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        mToolbarUtils.setTitleText("发布代祷");
        mToolbarUtils.setRightText("发布");
    }

    @Override
    public void onToolBarRightViewClick(View v) {
        pubInterces();
    }

    private void pubInterces() {
        String content = mContentEdit.getText().toString();
        long preTime = 0;
        String location = "";
        if (TextUtils.isEmpty(content)) {
            showShortToast("代祷内容不能为空！");
            return;
        }
        if (mPreCheckBox.isChecked()) {
            try {
                preTime = DateUtils.formatToLong(mYearTextView.getText().toString() + "-" + mMonthTextView.getText().toString() + "-" + mDayTextView.getText().toString() + ":" + mHourEdit.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (mLocSuccess) {
            location = mLocCheckBox.getText().toString();
        }
        if (preTime != 0 && preTime < System.currentTimeMillis()) {
            showShortToast("更新代祷时间不能小于当前时间");
            return;
        }
        InterCesRequest.pubInterces(PubInterCesActivity.this, mUser.getUserId(), content, true, preTime, location, new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                showShortToast("发布成功！");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure() {
                showShortToast("发布失败！");
            }
        });
    }

    private void getLocation() {
        new AmapUtils(this, new AmapUtils.GetLoc() {
            @Override
            public void onLoc(final String provinceName, final String cityName, final double latitude, final double longitude) {
                String province = provinceName.length() > 2 ? provinceName.replace("省", "") : provinceName;
                String city = cityName.length() > 2 ? cityName.replaceAll("市", "") : cityName;
                mLocCheckBox.setText(province + city);
                mLocSuccess = true;
                mLocCheckBox.setEnabled(true);
            }

            @Override
            public void onLocFailure() {
                mLocCheckBox.setText("点我获取位置信息");
                mLocCheckBox.setEnabled(false);
                mLocSuccess = false;
            }
        });
    }

    public static void launch(BaseActivity activity) {
        Intent intent = new Intent(activity, PubInterCesActivity.class);
        activity.startActivityForResult(intent, ACTION_PUB_INTERCES);
    }
}
