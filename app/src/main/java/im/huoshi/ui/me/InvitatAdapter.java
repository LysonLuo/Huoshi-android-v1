package im.huoshi.ui.me;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.Contacts;
import im.huoshi.utils.ShareUtils;
import im.huoshi.utils.ViewInject;
import im.huoshi.utils.ViewUtils;

/**
 * Created by Lyson on 16/4/30.
 */
public class InvitatAdapter extends RecyclerView.Adapter<InvitatAdapter.InvitatViewHolder> {
    private List<Contacts> mContactsList;
    private Context mContext;

    public InvitatAdapter(List<Contacts> contactsList, Context context) {
        this.mContactsList = contactsList;
        this.mContext = context;
    }

    @Override
    public InvitatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_invitat, parent, false);
        return new InvitatViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(final InvitatViewHolder holder, int position) {
        Contacts contacts = mContactsList.get(position);
        holder.tvContactName.setText(contacts.getContactsName());
        if (contacts.getContactsType() == 1) {
            holder.tvContactStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_invitat));
            if (Build.VERSION.SDK_INT > 16) {
                holder.tvContactStatus.setBackground(null);
            } else {
                holder.tvContactStatus.setBackgroundDrawable(null);
            }
            holder.tvContactStatus.setText("已邀请");
            holder.tvContactStatus.setOnClickListener(null);
        } else {
            holder.tvContactStatus.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_light));
            if (Build.VERSION.SDK_INT > 16) {
                holder.tvContactStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_blue_rec_whilte_solid));
            } else {
                holder.tvContactStatus.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_blue_rec_whilte_solid));
            }
            holder.tvContactStatus.setText("邀请");
            holder.tvContactStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.smsShare((BaseActivity) mContext, new ShareUtils.UmengShareListener() {
                        @Override
                        public void onSuccess() {
                            holder.tvContactStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_invitat));
                            if (Build.VERSION.SDK_INT > 16) {
                                holder.tvContactStatus.setBackground(null);
                            } else {
                                holder.tvContactStatus.setBackgroundDrawable(null);
                            }
                            holder.tvContactStatus.setText("已邀请");
                            holder.tvContactStatus.setOnClickListener(null);
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mContactsList.size();
    }

    class InvitatViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.tv_contact_name)
        private TextView tvContactName;
        @ViewInject(R.id.tv_contact_status)
        private TextView tvContactStatus;

        public InvitatViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }
    }
}
