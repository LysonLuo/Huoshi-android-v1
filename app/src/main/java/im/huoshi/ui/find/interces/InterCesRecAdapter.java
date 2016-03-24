package im.huoshi.ui.find.interces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import im.huoshi.R;
import im.huoshi.common.OnRecClickListener;
import im.huoshi.model.Intercession;
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 15/12/26.
 */
public class InterCesRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnRecClickListener<List<String>> mRecClickListener;
    private List<Intercession> mIntercessionList = new ArrayList<>();

    public InterCesRecAdapter(Context mContext, List<Intercession> intercessionList) {
        this.mContext = mContext;
        this.mIntercessionList = intercessionList;
    }

    public void setmRecClickListener(OnRecClickListener<List<String>> recClickListener) {
        this.mRecClickListener = recClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_interces_item, parent, false);
        IntercesViewHolder holder = new IntercesViewHolder(contentView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Intercession intercession = mIntercessionList.get(position);
        IntercesViewHolder intercesViewHolder = (IntercesViewHolder) holder;
        if (!TextUtils.isEmpty(intercession.getPortrait())) {
            Glide.with(mContext).load(intercession.getPortrait()).into(intercesViewHolder.mAvatarImageView);
        }
        intercesViewHolder.mNameTextView.setText(intercession.getNickName());
        intercesViewHolder.mContentTextView.setText(intercession.getContent());
        intercesViewHolder.mTimeTextView.setText(DateUtils.formatToString(intercession.getTime(), "yyyy-MM-dd"));
        intercesViewHolder.mCountTextView.setText(intercession.getIntercessionNumber() + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecClickListener != null) {
                    mRecClickListener.OnClick(new ArrayList<String>());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIntercessionList.size();
    }

    public void resetData(List<Intercession> intercessionList) {
        mIntercessionList = intercessionList;
        notifyDataSetChanged();
    }

    class IntercesViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.imageview_avatar)
        private CircleImageView mAvatarImageView;
        @ViewInject(R.id.textview_name)
        private TextView mNameTextView;
        @ViewInject(R.id.textview_interces_content)
        private TextView mContentTextView;
        @ViewInject(R.id.textview_interces_time)
        private TextView mTimeTextView;
        @ViewInject(R.id.textview_interces_count)
        private TextView mCountTextView;

        public IntercesViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }
    }
}
