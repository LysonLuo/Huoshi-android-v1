package im.huoshi.ui.find.interces;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.InterCesRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.data.UserPreference;
import im.huoshi.model.Comment;
import im.huoshi.model.IntercesContent;
import im.huoshi.model.Intercession;
import im.huoshi.model.Intercessors;
import im.huoshi.utils.CTextUtils;
import im.huoshi.utils.DateUtils;
import im.huoshi.utils.LogUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;
import im.huoshi.views.LoadMoreAdapter;

/**
 * Created by Lyson on 15/12/26.
 */
public class InterCesDelRecAdapter extends LoadMoreAdapter<Comment> {
    private Context mContext;
    private Intercession mIntercession;
    private int ITEM_TYPE_TOP = 1;
    private int ITEM_TYPE_NORMAL = 2;

    public InterCesDelRecAdapter(Context mContext, Intercession intercession, List<Comment> commentList) {
        super(mContext, commentList);
        this.mContext = mContext;
        this.mIntercession = intercession;
    }

    public void resetData(Intercession intercession, List<Comment> commentList) {
        this.mIntercession = intercession;
        resetData(commentList);
    }


    @Override
    public RecyclerView.ViewHolder getNewItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_TOP) {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_interces_details_top_item, parent, false);
            TopViewHolder topViewHolder = new TopViewHolder(contentView);
            return topViewHolder;
        } else {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_interces_details_normal_item, parent, false);
            DetailViewHolder detailViewHolder = new DetailViewHolder(contentView);
            return detailViewHolder;
        }
    }

    @Override
    public int getNewItemViewtype(int position) {
        if (position == 0) {
            return ITEM_TYPE_TOP;
        }
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public void bindNewViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_TOP) {
            TopViewHolder viewHolder = (TopViewHolder) holder;
            if (!TextUtils.isEmpty(mIntercession.getPortrait())) {
                Glide.with(mContext).load(mIntercession.getPortrait()).into(viewHolder.mAvatarImageView);
            } else {
                Glide.with(mContext).load(R.mipmap.img_default_avatar).into(viewHolder.mAvatarImageView);
            }
            viewHolder.mNickNameTextView.setText(mIntercession.getNickName());
            viewHolder.mUpdateLayout.removeAllViews();
            int borderColor = mIntercession.getGender() == 1 ? R.color.avatar_border_blue_color : R.color.avatar_border_red_color;
            viewHolder.mGengerImageView.setBorderColor(ContextCompat.getColor(mContext, borderColor));
            for (int i = 0; i < mIntercession.getContentList().size(); i++) {
                IntercesContent content = mIntercession.getContentList().get(i);
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_interces_update_item, null);
                TextView mTimeTextView = (TextView) contentView.findViewById(R.id.textview_interces_time);
                TextView mContentTextView = (TextView) contentView.findViewById(R.id.textview_interces_content);
                mContentTextView.setText(content.getContent());
                if (i == 0) {
                    mContentTextView.setEnabled(true);
                    mTimeTextView.setEnabled(true);
                } else {
                    mContentTextView.setEnabled(false);
                    mTimeTextView.setEnabled(false);
                }
                if (mIntercession.getContentList().size() == 1 || i == mIntercession.getContentList().size() - 1) {
                    viewHolder.mCreateTimeTextView.setText(DateUtils.getDayBetweenString(content.getCreateTime()) + " 发布");
                }
                mTimeTextView.setVisibility((mIntercession.getContentList().size() == 1 || i == mIntercession.getContentList().size() - 1) ? View.GONE : View.VISIBLE);
                mTimeTextView.setText(DateUtils.getDayBetweenString(content.getCreateTime()) + " 更新");
                viewHolder.mUpdateLayout.addView(contentView);
            }
            viewHolder.mRelationTextView.setText(CTextUtils.getRelation(mIntercession.getRelationship()));
            StringBuilder stringBuilder = new StringBuilder();
            String intercessor = "";
            if (mIntercession.getIntercessorsList().size() > 0) {
                for (Intercessors intercessors : mIntercession.getIntercessorsList()) {
                    stringBuilder.append(intercessors.getNickName() + "、");
                }
            }
            if (!TextUtils.isEmpty(stringBuilder.toString())) {
                intercessor = stringBuilder.toString();
                intercessor = intercessor.substring(0, intercessor.length() - 1);
            }
            if (!TextUtils.isEmpty(intercessor)) {
                viewHolder.mIntercessorTextView.setVisibility(View.VISIBLE);
                intercessor = " 代祷勇士：" + intercessor;
                SpannableString spannableString = new SpannableString(intercessor);
                ImageSpan imageSpan = new ImageSpan(mContext, R.mipmap.icon_interces_small);
                spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.mIntercessorTextView.setText(spannableString);
            } else {
                viewHolder.mIntercessorTextView.setVisibility(View.GONE);
            }
            viewHolder.mBlessTipTextView.setText((mItemList == null || mItemList.size() == 0) ? "快来写点祝福鼓励下兄弟姐妹" : "祝福");
        } else {
            final DetailViewHolder viewHolder = (DetailViewHolder) holder;
            final Comment comment = mItemList.get(position - 1);
            if (!TextUtils.isEmpty(comment.getAvatar())) {
                Glide.with(mContext).load(comment.getAvatar()).into(viewHolder.mAvatarImageView);
            } else {
                Glide.with(mContext).load(R.mipmap.img_default_avatar).into(viewHolder.mAvatarImageView);
            }

            int borderColor = comment.getGender() == 1 ? R.color.avatar_border_blue_color : R.color.avatar_border_red_color;
            viewHolder.mGengerImageView.setBorderColor(ContextCompat.getColor(mContext, borderColor));

            viewHolder.mNickNameTextView.setText(comment.getNickName());
            viewHolder.mContentTextView.setText(comment.getContent());
            viewHolder.mCreateTimeTextView.setText(DateUtils.getDayBetweenString(mIntercession.getTime()));
            viewHolder.mCountCheckBox.setText(comment.getPraiseNumber() + "");
            viewHolder.mCountCheckBox.setChecked(comment.getIsPraised() == 1 ? true : false);
            viewHolder.mCountCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InterCesRequest.praiseComment((BaseActivity) mContext, UserPreference.getInstance().getUser().getUserId(), comment.getCommentId(), new RestApiCallback() {
                        @Override
                        public void onSuccess(String responseString) {
                            if (comment.getIsPraised() == 1) {
                                comment.setPraised(0);
                                viewHolder.mCountCheckBox.setChecked(false);
                                comment.setPraiseNumber(comment.getPraiseNumber() - 1);
                                viewHolder.mCountCheckBox.setText(comment.getPraiseNumber() + "");
                            } else {
                                comment.setPraised(1);
                                viewHolder.mCountCheckBox.setChecked(true);
                                comment.setPraiseNumber(comment.getPraiseNumber() + 1);
                                viewHolder.mCountCheckBox.setText(comment.getPraiseNumber() + "");
                            }
                        }

                        @Override
                        public void onFailure() {
                            LogUtils.d("alpha", "praise fail");
                        }
                    });
                }
            });
        }
    }


    @Override
    public long getNewItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mIntercession == null ? 0 : (mItemList == null || mItemList.size() == 0 ? 1 : mItemList.size() + 2);
    }


    class TopViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.imageview_gender_bg)
        private CircleImageView mGengerImageView;
        @ViewInject(R.id.imageview_avatar)
        private CircleImageView mAvatarImageView;
        @ViewInject(R.id.textview_nick_name)
        private TextView mNickNameTextView;
        @ViewInject(R.id.layout_interces_update)
        private LinearLayout mUpdateLayout;
        @ViewInject(R.id.textview_relation)
        private TextView mRelationTextView;
        @ViewInject(R.id.textview_create_time)
        private TextView mCreateTimeTextView;
        @ViewInject(R.id.textview_intercessor)
        private TextView mIntercessorTextView;
        @ViewInject(R.id.textview_bless_tip)
        private TextView mBlessTipTextView;

        public TopViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.imageview_gender_bg)
        private CircleImageView mGengerImageView;
        @ViewInject(R.id.imageview_avatar)
        private CircleImageView mAvatarImageView;
        @ViewInject(R.id.textview_nick_name)
        private TextView mNickNameTextView;
        @ViewInject(R.id.textview_content)
        private TextView mContentTextView;
        @ViewInject(R.id.textview_create_time)
        private TextView mCreateTimeTextView;
        @ViewInject(R.id.textview_like_count)
        private CheckBox mCountCheckBox;

        public DetailViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }
    }
}
