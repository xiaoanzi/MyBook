package com.app.mybook.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mybook.R;
import com.app.mybook.model.BookListNote;
import com.app.mybook.util.MyImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 王海 on 2015/4/16.
 */
public class MyListNoteAdapter extends RecyclerView.Adapter<MyListNoteAdapter.ViewHolder> {
    private List<BookListNote> mList;
    private View mView;
    private final int TYPE_HEADER = 0;
    private final int TYPE_CHILD = 1;
    MyImageLoader myImageLoader = new MyImageLoader();
    /**
     * Item的回调接口
     *
     */
    public interface OnItemClickListener {
        void onItemClickListener(BookListNote parent,View view, int position);
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
    public MyListNoteAdapter(List<BookListNote> list) {
        mList = list;
        myImageLoader.getImageLoaderConfiguration();
    }

    @Override
    public MyListNoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mView != null && viewType == TYPE_HEADER) {
            return new ViewHolder(mView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_list, parent, false);
            ViewHolder holder = new ViewHolder(v);
            holder.note_user_name = (TextView) v.findViewById(R.id.note_user_name);
            holder.note_user_page_no = (TextView) v.findViewById(R.id.note_user_page_no);
            holder.note_user_chapter = (TextView) v.findViewById(R.id.note_user_chapter);
            holder.note_a_abstract = (TextView) v.findViewById(R.id.note_a_abstract);
            holder.note_user_image = (ImageView) v.findViewById(R.id.note_user_image);
            holder.note_time = (TextView) v.findViewById(R.id.note_time);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(final MyListNoteAdapter.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER) return;
        if (mView != null) {
            position = position - 1;
        }
        final int i = position;
        BookListNote bookListNote = mList.get(position);
        final String note_user_name = bookListNote.getAuthor_user().getName();
        final String note_user_page_no = bookListNote.getPage_no();
        final String note_user_chapter = bookListNote.getChapter();
        final String note_a_abstract = bookListNote.getA_abstract();
        final String note_time = bookListNote.getTime();
        holder.note_user_name.setText(note_user_name);
        holder.note_user_page_no.setText(note_user_page_no);
        holder.note_user_chapter.setText(note_user_chapter);
        holder.note_a_abstract.setText(note_a_abstract);
        holder.note_time.setText(note_time);
        ImageLoader.getInstance().displayImage(bookListNote.getAuthor_user().getAvatar(), holder.note_user_image, myImageLoader.getDisplayImageOptions());
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

        ImageView note_user_image;
        TextView note_user_name;
        TextView note_user_page_no;
        TextView note_user_chapter;
        TextView note_a_abstract;
        TextView note_time;
    }

    public void addHeaderView(View view) {
        this.mView = view;
    }
}
