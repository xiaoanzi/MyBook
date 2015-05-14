package com.app.mybook.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.mybook.R;
import com.app.mybook.api.Api;
import com.app.mybook.model.BookListNote;
import com.app.mybook.util.MyJson;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王海 on 2015/4/15.
 */
public class BookListNoteActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView mRecyclerView;
    View mHeader;
    MyJson myJson = new MyJson();
    private boolean mHeaderIsShown;
    private MyListNoteAdapter mListAdapter;
    private ArrayList<String> mDataList;
    private int mFlexibleSpaceOffset;

    private List<BookListNote> bookListNoteList = new ArrayList<BookListNote>();
    private RequestQueue mQueue;
    private Toolbar toolbar;

    private String bookId = "";
    private int pageNmber = 0;//已经加载了的评论数量
    private int count = 20;//每次加载的评论条数
    private int pageTotal = 0;//笔记的总数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_notelayout);
        mHeaderIsShown = true;
        mFlexibleSpaceOffset = getResources().getDimensionPixelSize(R.dimen.header_height);
        mQueue = Volley.newRequestQueue(this);
        initViews();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideHeader();
                swipeRefreshLayout.setRefreshing(true);
                getListAnnotation(0,count);
            }
        }, 1000);
        setUpRecyclerView();
    }

    public void initViews(){
        Intent intent1 = getIntent();
        bookId = intent1.getStringExtra("bookId");

        mHeader = findViewById(R.id.tl_custom);
        mRecyclerView = (RecyclerView) findViewById(R.id.note_list);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        toolbar.setTitle(intent1.getStringExtra("title")+"的笔记");//设置Toolbar标题
        setSupportActionBar(toolbar);
        getSupportActionBar().getThemedContext();
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //下拉刷新部分
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_container);
        //设置卷内的颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置下拉刷新监听
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    //下拉刷新执行的方法
    @Override
    public void onRefresh() {
        pageNmber = 0;
        hideHeader();
        getListAnnotation(0,count);
    }

    //获取笔记信息
    public void getListAnnotation(int satar,int count){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Api.BOOK_INFO+bookId+Api.BOOK_LIST_NOTE+Api.BOOK_LIST_NOTE_FIELDS+"&start="+satar+"&count="+count,
                null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (pageNmber == 0)
                    bookListNoteList.clear();
                bookListNoteList.addAll(myJson.jsonObjectNotes(jsonObject));
                if(bookListNoteList.size() == 0){
                    Toast.makeText(BookListNoteActivity.this, "当前图书无读书笔记",Toast.LENGTH_SHORT).show();
                    BookListNoteActivity.this.finish();
                }
                getData();
                pageTotal = jsonObject.optInt("total");
                swipeRefreshLayout.setRefreshing(false);
                showHeader();
                pageNmber = bookListNoteList.size();
                if (pageNmber == pageTotal)
                    Toast.makeText(BookListNoteActivity.this, "没有更多的笔记了",Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.toString());
                swipeRefreshLayout.setRefreshing(false);
                showHeader();
                Toast.makeText(BookListNoteActivity.this, "获取信息失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void setUpRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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
                Intent noteIntent = new Intent(BookListNoteActivity.this, BookNoteInfoActivity.class);
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
                        //判断是否滚动到底部
                        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (linearLayoutManager.getItemCount() - 1)) {
                            if (pageNmber == pageTotal){
                                Toast.makeText(BookListNoteActivity.this, "没有更多的笔记了",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            swipeRefreshLayout.setRefreshing(true);
                            getListAnnotation(pageNmber,count);
                        }
                        return;
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
