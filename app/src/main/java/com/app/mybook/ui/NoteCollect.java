package com.app.mybook.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.app.mybook.R;
import com.app.mybook.model.BookListNote;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王海 on 2015/5/13.
 */
public class NoteCollect extends ActionBarActivity {
    RecyclerView mRecyclerView;
    View mHeader;
    private boolean mHeaderIsShown;
    private MyListNoteAdapter mListAdapter;
    private ArrayList<String> mDataList;
    private int mFlexibleSpaceOffset;

    private List<BookListNote> bookListNoteList = new ArrayList<BookListNote>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_notelayout);
        mHeaderIsShown = true;
        mFlexibleSpaceOffset = getResources().getDimensionPixelSize(R.dimen.header_height);
        initView();
        getListAnnotation();
        setUpRecyclerView();
    }

    public void initView(){
        mHeader = findViewById(R.id.tl_custom);
        mRecyclerView = (RecyclerView) findViewById(R.id.note_list);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        toolbar.setTitle("收藏的评论");//设置Toolbar标题
        setSupportActionBar(toolbar);
        getSupportActionBar().getThemedContext();
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void getListAnnotation(){
        bookListNoteList = new Select().from(BookListNote.class).execute();
        if(bookListNoteList.size() == 0) {
            Toast.makeText(NoteCollect.this, "没有收藏的评论", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mDataList = new ArrayList<>();

        LinearLayout linearLayout = new LinearLayout(this);
        // 设置线性布局的宽，高：根据内容自动调整
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, mFlexibleSpaceOffset));
        linearLayout.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
        TextView tv = new TextView(this);
        tv.setText("Hi,嘿嘿嘿");
        linearLayout.addView(tv);
        mListAdapter = new MyListNoteAdapter(bookListNoteList);
        mListAdapter.addHeaderView(linearLayout);
        mListAdapter.setOnItemClickListener(new MyListNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(BookListNote parent, View view, int position) {
                Intent noteIntent = new Intent(NoteCollect.this, BookNoteInfoActivity.class);
                noteIntent.putExtra("noteInfo", parent);
                startActivity(noteIntent);
            }
        });
        mRecyclerView.setAdapter(mListAdapter);

        mRecyclerView.setOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    boolean isIdle;
                    int mScrollY;

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        isIdle = newState == RecyclerView.SCROLL_STATE_IDLE;
                        if (isIdle) {
                            mScrollY = 0;
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        mScrollY += dy;
                        // show or hide header view
                        if (mScrollY > 12) {
                            hideHeader();
                        } else {
                            showHeader();
                        }
                    }
                }
        );
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private void getData() {
        mListAdapter.notifyDataSetChanged();
    }

    private void showHeader() {
        if (!mHeaderIsShown) {
            ViewPropertyAnimator.animate(mHeader).cancel();
            ViewPropertyAnimator.animate(mHeader).translationY(0).setDuration(200).start();
            mHeaderIsShown = true;
        }
    }

    private void hideHeader() {
        if (mHeaderIsShown) {
            ViewPropertyAnimator.animate(mHeader).cancel();
            ViewPropertyAnimator.animate(mHeader).translationY(-mFlexibleSpaceOffset).setDuration(200).start();
            mHeaderIsShown = false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
