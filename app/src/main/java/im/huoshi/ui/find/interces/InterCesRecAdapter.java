package im.huoshi.ui.find.interces;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import im.huoshi.R;
import im.huoshi.common.OnRecClickListener;
import im.huoshi.model.Intercession;
import im.huoshi.utils.CTextUtils;
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;
import im.huoshi.views.LoadMoreAdapter;

/**
 * Created by Lyson on 15/12/26.
 */
public class InterCesRecAdapter extends LoadMoreAdapter<Intercession> {
    private Context mContext;
    private OnRecClickListener<Intercession> mRecClickListener;

    public InterCesRecAdapter(Context mContext, List<Intercession> intercessionList) {
        super(mContext, intercessionList);
        this.mContext = mContext;

    }

    public void setmRecClickListener(OnRecClickListener<Intercession> recClickListener) {
        this.mRecClickListener = recClickListener;
    }


    @Override
    public RecyclerView.ViewHolder getNewItemViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_interces_item, parent, false);
        IntercesViewHolder holder = new IntercesViewHolder(contentView);
        return holder;
    }


    @Override
    public void bindNewViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Intercession intercession = mItemList.get(position);
        IntercesViewHolder intercesViewHolder = (IntercesViewHolder) holder;
        int borderColor = intercession.getGender() == 1 ? R.color.avatar_border_blue_color : R.color.avatar_border_red_color;
        intercesViewHolder.mGenderImageView.setBorderColor(ContextCompat.getColor(mContext, borderColor));
        if (!TextUtils.isEmpty(intercession.getPortrait())) {
            Glide.with(mContext).load(intercession.getPortrait()).into(intercesViewHolder.mAvatarImageView);
        }else {
            Glide.with(mContext).load(R.mipmap.img_default_avatar).into(intercesViewHolder.mAvatarImageView);
        }
        intercesViewHolder.mNameTextView.setText(intercession.getNickName());
        intercesViewHolder.mRelationTextView.setText(CTextUtils.getRelation(intercession.getRelationship()));
        intercesViewHolder.mContentTextView.setText(intercession.getContentList().get(intercession.getContentList().size() - 1).getContent());
        intercesViewHolder.mTimeTextView.setText(DateUtils.getDayBetweenString(intercession.getTime()));
        intercesViewHolder.mCountTextView.setText(intercession.getIntercessionNumber() + "人");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecClickListener != null) {
                    mRecClickListener.OnClick(intercession);
                }
            }
        });
    }

    @Override
    public long getNewItemId(int position) {
        return mItemList.get(position).getIntercessionId();
    }


    class IntercesViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.imageview_gender_bg)
        private CircleImageView mGenderImageView;
        @ViewInject(R.id.imageview_avatar)
        private CircleImageView mAvatarImageView;
        @ViewInject(R.id.textview_name)
        private TextView mNameTextView;
        @ViewInject(R.id.textview_relation)
        private TextView mRelationTextView;
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
