package im.huoshi.ui.huoshi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.huoshi.R;
import im.huoshi.base.BaseFragment;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/1/14.
 */
public class HuoshiFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_huoshi, container, false);
        ViewUtils.inject(this, contentView);
        return contentView;
    }
}
