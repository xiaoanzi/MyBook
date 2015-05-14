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
import com.app.mybook.model.AuthorUser;
import com.app.mybook.model.BookListNote;
import com.app.mybook.util.MyImageLoader;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

/**
 * Created by 王海 on 2015/4/19.
 */
public class BookNoteInfoActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private RequestQueue mQueue;
    private MyImageLoader myImageLoader;

    private BookListNote bookListNote = new BookListNote();

    private ObservableScrollView scrollView;
    private FloatingActionButton addNoteFab;
    private ImageView note_info_user_image;
    private TextView note_info_user_name;
    private TextView note_info_time;
    private TextView note_info_user_page_no;
    private TextView note_info_user_chapter;
    private TextView note_info_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noteinfo_layout);
        mQueue = Volley.newRequestQueue(this);
        myImageLoader = new MyImageLoader();
        myImageLoader.getImageLoaderConfiguration();
        initView();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Api.BOOK_INFO_NOTE+bookListNote.getBookListNoteId()+Api.BOOK_INFO_NOTE_FIELDS,
                null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                jsonObjectNote(jsonObject);
                changceView();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.toString());
                Toast.makeText(BookNoteInfoActivity.this, "获取信息失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
        addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookListNote bookListNoteTemp = new Select()
                        .from(BookListNote.class)
                        .where("bookListNoteId = ?", bookListNote.getBookListNoteId())
                        .executeSingle();
                if(bookListNoteTemp == null){
                    AuthorUser authorUser = new AuthorUser();
                    authorUser = bookListNote.getAuthor_user();
                    ActiveAndroid.beginTransaction();
                    try {
                        authorUser.save();
                        bookListNote.save();
                        ActiveAndroid.setTransactionSuccessful();
                        Toast.makeText(BookNoteInfoActivity.this, "已加入评论收藏", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(BookNoteInfoActivity.this, "加入失败，错误信息:"+e.toString(), Toast.LENGTH_LONG).show();
                        Log.e("TAG",e.toString());
                    }
                    finally {
                        ActiveAndroid.endTransaction();
                    }
                }else{
                    Toast.makeText(BookNoteInfoActivity.this, "不能重复加入评论收藏哦~~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void jsonObjectNote(JSONObject jsonObject){
        try {
            bookListNote.setContent(jsonObject.getString("content"));
        }catch (Exception e){
            Log.e("tag",e.toString());
        }
    }

    public void initView(){
        Intent noteIntent = getIntent();
        bookListNote = (BookListNote)noteIntent.getSerializableExtra("noteInfo");

        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        toolbar.setTitle(bookListNote.getAuthor_user().getName());//设置Toolbar标题
        setSupportActionBar(toolbar);
        getSupportActionBar().getThemedContext();
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scrollView = (ObservableScrollView) findViewById(R.id.note_scroll_view);
        addNoteFab = (FloatingActionButton) findViewById(R.id.add_note_fab);
        note_info_user_image = (ImageView) findViewById(R.id.note_info_user_image);
        note_info_user_name = (TextView) findViewById(R.id.note_info_user_name);
        note_info_time = (TextView) findViewById(R.id.note_info_time);
        note_info_user_page_no = (TextView) findViewById(R.id.note_info_user_page_no);
        note_info_user_chapter = (TextView) findViewById(R.id.note_info_user_chapter);
        note_info_content = (TextView) findViewById(R.id.note_info_content);
        addNoteFab.attachToScrollView(scrollView);
    }

    public void changceView(){
        ImageLoader.getInstance().loadImage(bookListNote.getAuthor_user().getAvatar(), new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                note_info_user_image.setImageBitmap(loadedImage);
            }

        });
        note_info_user_name.setText(bookListNote.getAuthor_user().getName());
        note_info_time.setText(bookListNote.getTime());
        note_info_user_page_no.setText(bookListNote.getPage_no());
        note_info_user_chapter.setText(bookListNote.getChapter());
        note_info_content.setText(bookListNote.getContent());
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
