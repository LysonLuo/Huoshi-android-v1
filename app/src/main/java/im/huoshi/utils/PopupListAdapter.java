package im.huoshi.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import im.huoshi.R;

/**
 * Created by Lyson on 16/3/17.
 */
public class PopupListAdapter extends RecyclerView.Adapter<PopupListAdapter.ViewHolder> {
    private List<String> mDatas;
    private OnItemClickListener onItemClickListener;
    //弹出菜单的上下文，即要弹出菜单的列表项
    private View contextView;
    //点击的上下文列表项位置
    private int contextPosition;

    //为每个数据项提供视图引用
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public ViewHolder(View v) {
            super(v);
        }
    }

    //根据数据集的类型书写合适的构造器
    public PopupListAdapter(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    //创建新的view - 由LayoutManager布局管理器调用
    @Override
    public PopupListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_context_menu_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mTextView = (TextView) v.findViewById(R.id.textview_menu_name);
        //设置view的大小、内边距、外边距、布局参数
        return vh;
    }

    //替换view的内容，以避免每次都创建不必要的view或者执行findViewById()的昂贵开销 - 由LayoutManager布局管理器调用
    @Override
    public void onBindViewHolder(final PopupListAdapter.ViewHolder holder, final int position) {
        //获取数据集中position位置的数据并用其替换view的内容
        holder.mTextView.setText(mDatas.get(position));
        //如果设置了回调，则设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(contextView, contextPosition, holder.itemView, position);
                }
            }
        });

    }

    //数据集的大小 - 由LayoutManager布局管理器调用
    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View contextView, int contextPosition, View view, int position);
    }


    public void setContextView(View contextView) {
        this.contextView = contextView;
    }

    public void setContextPosition(int contextPosition) {
        this.contextPosition = contextPosition;
    }
}
