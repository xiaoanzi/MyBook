package com.app.mybook.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.mybook.R;
import com.app.mybook.api.Api;
import com.app.mybook.model.BookInfo;
import com.app.mybook.model.BookSearch;
import com.app.mybook.model.Rating;
import com.app.mybook.util.MyImageLoader;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 王海 on 2015/4/16.
 */
public class BookInfoActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private BookInfo bookInfo = new BookInfo();
    private BookSearch bookSearch = new BookSearch();
    private RequestQueue mQueue;
    private MyImageLoader myImageLoader;

    private FloatingActionButton addBookFab;
    private ObservableScrollView scrollView;
    private TextView title;
    private TextView author;
    private TextView publisher;
    private TextView pubdate;
    private TextView price;
    private TextView pages;
    private TextView summary;
    private TextView author_intro;
    private TextView catalog;
    private TextView tags;
    private TextView average;
    private TextView isbn13;
    private String isbn;
    private boolean isIsbn = false;

    private Button note_button;

    private ImageView book_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookinfo_layout);
        mQueue = Volley.newRequestQueue(this);
        myImageLoader = new MyImageLoader();
        myImageLoader.getImageLoaderConfiguration();
        isIsbn = getIntent().getBooleanExtra("isIsbn",false);
        initView();
        initIntent();
        note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noteIntent = new Intent(BookInfoActivity.this, BookListNoteActivity.class);
                noteIntent.putExtra("bookId",bookInfo.getBookSearchId());
                noteIntent.putExtra("title",bookInfo.getTitle());
                startActivity(noteIntent);
            }
        });
        addBookFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookInfo bookInfoTemp = new Select()
                                        .from(BookInfo.class)
                                        .where("bookSearchId = ?", bookInfo.getBookSearchId())
                                        .executeSingle();
                if(bookInfoTemp == null){
                    Rating rating = new Rating();
                    rating = bookInfo.getRating();
                    ActiveAndroid.beginTransaction();
                    try {
                        rating.save();
                        bookInfo.save();
                        ActiveAndroid.setTransactionSuccessful();
                        Toast.makeText(BookInfoActivity.this, "已加入收藏", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(BookInfoActivity.this, "加入失败，错误信息:"+e.toString(), Toast.LENGTH_LONG).show();
                        Log.e("TAG",e.toString());
                    }
                    finally {
                        ActiveAndroid.endTransaction();
                    }
                }else{
                    Toast.makeText(BookInfoActivity.this, "不能重复加入收藏哦~~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色

        setSupportActionBar(toolbar);
        getSupportActionBar().getThemedContext();
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        addBookFab = (FloatingActionButton) findViewById(R.id.add_fab);
        book_image = (ImageView) findViewById(R.id.card_bookImage);
        author = (TextView) findViewById(R.id.card_author);
        publisher = (TextView) findViewById(R.id.card_publisher);
        pubdate = (TextView) findViewById(R.id.card_pubdate);
        price = (TextView) findViewById(R.id.card_price);
        pages = (TextView) findViewById(R.id.card_pages);
        summary = (TextView) findViewById(R.id.card_summary);
        author_intro = (TextView) findViewById(R.id.card_author_intro);
        catalog = (TextView) findViewById(R.id.card_catalog);
        tags = (TextView) findViewById(R.id.card_tags);
        average = (TextView) findViewById(R.id.card_average);
        isbn13 = (TextView) findViewById(R.id.card_isbn13);
        note_button = (Button) findViewById(R.id.card_button_note);
        addBookFab.attachToScrollView(scrollView);
    }

    public void initIntent(){
        if(!isIsbn){
            bookInfo = (BookInfo)getIntent().getSerializableExtra("bookInfo");
/*            bookInfo.setBookSearchId(bookSearch.getBookSearchId());
            bookInfo.setTitle(bookSearch.getTitle());
            bookInfo.setAuthor(bookSearch.getAuthor());
            bookInfo.setPublisher(bookSearch.getPublisher());
            bookInfo.setPubdate(bookSearch.getPubdate());
            bookInfo.setPrice(bookSearch.getPrice());
            bookInfo.setRating(bookSearch.getRating());
            bookInfo.setImage(bookSearch.getImage());*/
            loadBookInfo(Api.BOOK_INFO+bookInfo.getBookSearchId()+Api.BOOK_INFO_FIELDS);
        }else{
            isbn = (String)getIntent().getSerializableExtra("isbn");
            loadBookInfo(Api.BOOK_INFO_ISBN+isbn+Api.BOOK_INFO_FIELDS_ISBN);
        }
    }

    public void loadBookInfo(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(isIsbn){
                    jsonObjectBookIsbn(jsonObject);
                }
                jsonObjectBook(jsonObject);
                toolbar.setTitle(bookInfo.getTitle());//设置Toolbar标题
                changceView();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.toString());
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    public void jsonObjectBook(JSONObject jsonObject){
        try {
            bookInfo.setIsbn13(jsonObject.get("isbn13").toString());
            String tags = "";
            JSONArray jsonArrayTemp = (jsonObject.getJSONArray("tags"));
            for (int j = 0; j < jsonArrayTemp.length(); j++) {
                JSONObject temp = jsonArrayTemp.getJSONObject(j);
                tags += temp.getString("name");
                if(j != jsonArrayTemp.length() - 1){
                    tags += "、";
                }
            }
            bookInfo.setTags(tags);
            bookInfo.setPages(jsonObject.getString("pages"));
            bookInfo.setAuthor_intro(jsonObject.getString("author_intro"));
            bookInfo.setSummary(jsonObject.getString("summary"));
            bookInfo.setCatalog(jsonObject.getString("catalog"));

        }catch (Exception e){

            Log.e("tag",e.toString());
        }
//        return bookInfo;
    }

    public void jsonObjectBookIsbn(JSONObject jsonObject){
        try {
            bookInfo.setBookSearchId(jsonObject.get("id").toString());
            bookInfo.setTitle(jsonObject.get("title").toString());
            String acthor = "";
            JSONArray jsonArrayTemp = (jsonObject.getJSONArray("author"));
            for (int j = 0; j < jsonArrayTemp.length(); j++) {
                acthor += jsonArrayTemp.get(j).toString();
                if(j != jsonArrayTemp.length() - 1){
                    acthor += "、";
                }
            }
            bookInfo.setAuthor(acthor);
            bookInfo.setPublisher(jsonObject.get("publisher").toString());
            bookInfo.setPubdate(jsonObject.get("pubdate").toString());
            bookInfo.setPrice(jsonObject.get("price").toString());
            bookInfo.setImage(jsonObject.get("image").toString());

            JSONObject rating = jsonObject.getJSONObject("rating");
            Rating rating1 = new Rating();
            rating1.setNumRaters(rating.getString("numRaters"));
            rating1.setAverage(rating.getString("average"));
            bookInfo.setRating(rating1);

        }catch (Exception e){
            Log.e("tag",e.toString());
        }
//        return bookInfo;
    }

    public void changceView(){
        ImageLoader.getInstance().loadImage(bookInfo.getImage(), new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                book_image.setImageBitmap(loadedImage);
            }

        });
        author.setText(bookInfo.getAuthor());
        publisher.setText(bookInfo.getPublisher());
        pubdate.setText(bookInfo.getPubdate());
        price.setText(bookInfo.getPrice());
        pages.setText(bookInfo.getPages()+"页");
        summary.setText(bookInfo.getSummary());
        author_intro.setText(bookInfo.getAuthor_intro());
        catalog.setText(bookInfo.getCatalog());
        tags.setText(bookInfo.getTags());
        average.setText(bookInfo.getRating().getAverage()+"分");
        isbn13.setText(bookInfo.getIsbn13());
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
