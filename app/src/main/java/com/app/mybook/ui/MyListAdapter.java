package com.app.mybook.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mybook.R;
import com.app.mybook.model.BookInfo;
import com.app.mybook.util.MyImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by drakeet on 2/14/15.
 */
public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    private List<BookInfo> mList;
    private View mView;
    private final int TYPE_HEADER = 0;
    private final int TYPE_CHILD = 1;
    MyImageLoader myImageLoader = new MyImageLoader();

    /**
     * Item的回调接口
     *
     */
    public interface OnItemClickListener {
        void onItemClickListener(BookInfo parent,View view, int position);
    }

    private OnItemClickListener listener; // 点击Item的回调对象

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public MyListAdapter(List<BookInfo> list) {
        mList = list;
        myImageLoader.getImageLoaderConfiguration();
    }

    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mView != null && viewType == TYPE_HEADER) {
            return new ViewHolder(mView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_list, parent, false);
            ViewHolder holder = new ViewHolder(v);
            holder.imageView = (ImageView) v.findViewById(R.id.search_image);
            holder.title = (TextView) v.findViewById(R.id.search_title);
            holder.content = (TextView) v.findViewById(R.id.search_content);
            holder.score = (TextView) v.findViewById(R.id.search_score);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(final MyListAdapter.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER) return;
        if (mView != null) {
            position = position - 1;
        }
        final int i = position ;
        final String title = mList.get(position).getTitle();
        final String content = mList.get(position).getAuthor()+"/"+""+
                mList.get(position).getPublisher()+"/"+mList.get(position).getPubdate()+"/"+
                mList.get(position).getPrice();
        final String score = mList.get(position).getRating().getAverage()+"分("+mList.get(position).getRating().getNumRaters()+"人评论)";
        final String image = mList.get(position).getImage();
        holder.title.setText(title);
        holder.content.setText(content);
        holder.score.setText(score);
        ImageLoader.getInstance().displayImage(image, holder.imageView, myImageLoader.getDisplayImageOptions());
        //如果设置了回调，则设置点击事件
        if (listener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onItemClickListener(mList.get(i),holder.itemView, i);
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_CHILD;
    }

    @Override
    public int getItemCount() {
        return mView != null ? mList.size() + 1 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        ImageView imageView;
        TextView title;
        TextView content;
        TextView score;
    }

    public void addHeaderView(View view) {
        this.mView = view;
    }

}
