package im.huoshi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import im.huoshi.R;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * 重试页面，点击可以重新加载
 * <p/>
 * Created by Lyson on 15/12/24.
 */
public class RetryFragment extends BaseFragment {
    @ViewInject(R.id.textview_retry)
    TextView mRetryTextView;
    private OnRetryListener mRetryListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retry, container, false);
        ViewUtils.inject(this, view);

        mRetryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRetryListener != null) {
                    mRetryListener.onRetry();
                    //点击重试之后移除当前页面
                    getFragmentManager().beginTransaction().remove(RetryFragment.this).commit();
                }
            }
        });
        return view;

    }

    public void setOnRetryListener(OnRetryListener listener) {
        mRetryListener = listener;
    }


    public interface OnRetryListener {
        void onRetry();
    }
}
