package im.huoshi.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.huoshi.R;

/**
 * 显示加载进度的页面
 * <p/>
 * Created by Lyson on 15/12/24.
 */
public class ProgressFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        return view;
    }
}
