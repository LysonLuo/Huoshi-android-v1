package im.huoshi.ui.find.interces;

import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import im.huoshi.R;
import im.huoshi.utils.ScreenUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/1/13.
 */
public class InterCesDialog extends AppCompatDialog {
    //代祷前准备的布局
    @ViewInject(R.id.layout_pre_interces)
    private LinearLayout mPreLayout;
    @ViewInject(R.id.textview_total_interce_times)
    private TextView mTotalTimesTextView;
    @ViewInject(R.id.textview_interces_count_down)
    private TextView mCountDownTextView;
    //正在代祷布局
    @ViewInject(R.id.layout_intercesing)
    private LinearLayout mIntercesLayout;
    @ViewInject(R.id.textview_intercesing)
    private TextView mIntercesingTextView;
    //完成代祷布局
    @ViewInject(R.id.layout_interces_finished)
    private LinearLayout mIntercesFinishedLayout;
    @ViewInject(R.id.textview_pub_bless)
    private TextView mPubBlessTextView;
    @ViewInject(R.id.textview_share)
    private TextView mShareTextView;
    @ViewInject(R.id.textview_close)
    private TextView mCloseTextView;

    private CountDownTimer mPreCountDown;
    private CountDownTimer mIntercesCountDown;
    private IntercesListener mListener;

    public InterCesDialog(Context context, int totalTimes) {
        super(context, R.style.CustomPopup);
        View rootView = LayoutInflater.from(context).inflate(R.layout.widget_interces_dialog, null);
        final int dialogWidth = ScreenUtils.getScreenWidth(getWindow().getWindowManager().getDefaultDisplay());
        rootView.setMinimumWidth(dialogWidth - ScreenUtils.dip2px(context, 40));
        setContentView(rootView);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        ViewUtils.inject(this);
        setupViews(totalTimes);
        startPreCountDown();
    }

    public void setIntercesListener(IntercesListener listener) {
        this.mListener = listener;
    }

    private void setupViews(int totalTimes) {
        mTotalTimesTextView.setText("这是你第" + totalTimes + "次代祷");
        mPubBlessTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBless();
                dismiss();
            }
        });
        mShareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShare();
                dismiss();
            }
        });
        mCloseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClose();
                dismiss();
            }
        });
        mIntercesingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntercesingTextView.setVisibility(View.GONE);
                mIntercesFinishedLayout.setVisibility(View.VISIBLE);
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mPreCountDown = null;
                mIntercesCountDown = null;
            }
        });
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mIntercesCountDown != null) {
                    changeTipText();
                    return true;
                }
                return false;
            }
        });
    }

    private void startPreCountDown() {
        mPreCountDown = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCountDownTextView.setText((int) millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                mPreLayout.setVisibility(View.GONE);
                mIntercesLayout.setVisibility(View.VISIBLE);
                startIntercesCountDoun();
            }
        }.start();
    }

    private void startIntercesCountDoun() {
        mIntercesCountDown = new CountDownTimer(30 * 1000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mIntercesingTextView.setEnabled(true);
                mIntercesingTextView.setText("代祷完成");
            }
        }.start();
    }

    public void changeTipText() {
        mIntercesingTextView.setText("请至少静心祷告30秒");
    }

    public interface IntercesListener {
        void onBless();

        void onShare();

        void onClose();
    }
}
