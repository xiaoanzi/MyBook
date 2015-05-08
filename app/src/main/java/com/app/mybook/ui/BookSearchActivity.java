package com.app.mybook.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.mybook.R;
import com.app.mybook.api.Api;
import com.app.mybook.model.BookInfo;
import com.app.mybook.model.Rating;
import com.app.mybook.util.MyImageLoader;
import com.app.mybook.util.MyJson;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王海 on 2015/4/14.
 */
public class BookSearchActivity extends ActionBarActivity implements SearchView.OnQueryTextListener{
    RecyclerView mRecyclerView;
    View mHeader;
    MyImageLoader myImageLoader = new MyImageLoader();
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private List<String> books = new ArrayList<String>();
    private List<BookInfo> bookSearchList = new ArrayList<BookInfo>();
    private RequestQueue mQueue;
    private int mFlexibleSpaceOffset;
    private MyListAdapter mListAdapter;
    private ArrayList<String> mDataList;
    private boolean mHeaderIsShown;
    private SearchView searchView;
    private Toolbar toolbar;
    MyJson myJson = new MyJson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_searchlayout);
        mFlexibleSpaceOffset = getResources().getDimensionPixelSize(R.dimen.header_height);
        initView();
        mQueue = Volley.newRequestQueue(this);
        setUpRecyclerView();
    }

    //用户输入字符时激发该方法
    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    //单击搜索按钮时激发该方法
    @Override
    public boolean onQueryTextSubmit(String query){

        Log.e("tag", Api.BOOK_SEARCH+query+Api.BOOK_SEARCH_FIELDS);
        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(
                    Api.BOOK_SEARCH+URLEncoder.encode(query, "utf-8")+Api.BOOK_SEARCH_FIELDS,
                    null,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.e("TAGgggg", jsonObject.toString());
                    bookSearchList.clear();
     //               bookSearchList = myJson.jsonObjectBooks(jsonObject);
                    jsonObjectBooks(jsonObject);
                    getData();
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("TAG", volleyError.toString());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mQueue.add(jsonObjectRequest);
        return true;
    }

    public List<BookInfo> jsonObjectBooks(JSONObject jsonObject){
        try {
            JSONArray jsonArray = (jsonObject.getJSONArray("books"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                BookInfo bookSearch = new BookInfo();
                bookSearch.setBookSearchId(temp.getString("id"));
                bookSearch.setTitle(temp.getString("title"));

                String acthor = "";
                JSONArray jsonArrayTemp = (temp.getJSONArray("author"));
                for (int j = 0; j < jsonArrayTemp.length(); j++) {
                    acthor += jsonArrayTemp.get(j).toString();
                    if(j != jsonArrayTemp.length() - 1){
                        acthor += "、";
                    }
                }
                bookSearch.setAuthor(acthor);
                bookSearch.setPublisher(temp.getString("publisher"));
                bookSearch.setPubdate(temp.getString("pubdate"));
                bookSearch.setPrice(temp.getString("price"));
                bookSearch.setImage(temp.getString("image"));
                String translator = "";
                JSONArray translatorTemp = (temp.getJSONArray("translator"));
                for (int k = 0; k < translatorTemp.length(); k++) {
                    acthor += translatorTemp.get(k).toString();
                    if(k != translatorTemp.length() - 1){
                        translator += "、";
                    }
                }
                bookSearch.setTranslator(translator);
                JSONObject rating = temp.getJSONObject("rating");
                Rating rating1 = new Rating();
                rating1.setNumRaters(rating.getString("numRaters"));
                rating1.setAverage(rating.getString("average"));
                bookSearch.setRating(rating1);
                bookSearchList.add(bookSearch);
            }
        }catch (Exception e){
            Log.e("jsonObjectBooks",e.toString());
            e.printStackTrace();
        }
        return bookSearchList;
    }

    private void initView() {
        mHeader = findViewById(R.id.tl_custom);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_list);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);

        searchView = (SearchView) findViewById(R.id.searchlayout_searchview);
        searchView.setIconifiedByDefault(false);//设置该搜索框默认是否自动缩小为图标。
        searchView.setSubmitButtonEnabled(true);//设置是否显示搜索按钮。
        searchView.setQueryHint("请输入图书名");//设置搜索框内默认显示的提示文本。
        searchView.setOnQueryTextListener(this);
        searchView.setVisibility(View.VISIBLE);

        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().getThemedContext();
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mDataList = new ArrayList<>();
        View paddingView = new View(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, mFlexibleSpaceOffset
        );
        paddingView.setLayoutParams(params);
        paddingView.setBackgroundColor(Color.WHITE);
        mListAdapter = new MyListAdapter(bookSearchList);
        mListAdapter.addHeaderView(paddingView);
        mListAdapter.setOnItemClickListener(new MyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(BookInfo parent,View view, int position) {
                Intent intent1 = new Intent(BookSearchActivity.this, BookInfoActivity.class);
                intent1.putExtra("bookInfo",parent);
                intent1.putExtra("isIsbn",false);
                startActivity(intent1);
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
        getData();
    }

    private void getData() {
        // should create a new Thread
        // post
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
