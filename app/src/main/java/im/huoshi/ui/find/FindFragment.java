package im.huoshi.ui.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.base.BaseFragment;
import im.huoshi.ui.find.interces.InterCesActivity;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/24.
 */
public class FindFragment extends BaseFragment {
    @ViewInject(R.id.layout_interces)
    private LinearLayout mLayoutInterces;
    @ViewInject(R.id.layout_five_two)
    private LinearLayout mLayoutFiveTwo;
    @ViewInject(R.id.layout_share)
    private LinearLayout mShareLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_find, container, false);
        ViewUtils.inject(this, contentView);

        setupViews();
        return contentView;
    }

    private void setupViews() {
        mLayoutInterces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotLogin()) {
                    return;
                }
                InterCesActivity.launch((BaseActivity) getActivity());
            }
        });

        mLayoutFiveTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiveTwoActivity.launch((BaseActivity) getActivity());
            }
        });

        mShareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareHuoshiActivity.launch((BaseActivity) getActivity());
            }
        });
    }
}
