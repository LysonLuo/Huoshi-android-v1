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
 * 没有数据的时候显示的页面
 * <p/>
 * Created by Lyson on 15/12/24.
 */
public class NoDataFragment extends BaseFragment {
    @ViewInject(R.id.textview_nodata)
    TextView mNoDataTextView;
    private String mNoDataText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nodata, container, false);
        ViewUtils.inject(this, view);
        mNoDataTextView.setText(mNoDataText);
        return view;
    }

    public void setText(String text) {
        this.mNoDataText = text;
        if (mNoDataTextView != null) {
            mNoDataTextView.setText(text);
        }
    }
}
