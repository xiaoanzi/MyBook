package com.app.mybook.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.mybook.R;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    //声明相关变量
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"收藏的图书", "收藏的评论", "关于"};
    private ArrayAdapter arrayAdapter;
    private ImageView ivMain;
    private Button buttonScanning;
    private Button buttonSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDrawerLayout();
    }

    //初始化控件
    private void initViews() {
        ivMain = (ImageView) findViewById(R.id.iv_main);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        buttonScanning = (Button) findViewById(R.id.drawerLayout_button_scanning);
        buttonSearch = (Button) findViewById(R.id.drawerLayout_button_search);
        buttonScanning.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        ivMain.setImageResource(R.drawable.zoumo);
        toolbar.setTitle("扫图书");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().getThemedContext();
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //抽屉菜单的相关设置
    private void initDrawerLayout(){
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);
        lvLeftMenu.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击扫描按钮的监听
            case R.id.drawerLayout_button_scanning : {
                Intent scanIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(scanIntent);
                break;
            }
            //点击搜索按钮的监听
            case R.id.drawerLayout_button_search : {
                Intent searchIntent = new Intent(MainActivity.this, BookSearchActivity.class);
                startActivity(searchIntent);
                break;
            }
            default:break;
        }
    }

    //抽屉菜单里面的ListView的监听方法
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0: {
                Intent intentBook = new Intent(MainActivity.this, BookCollect.class);
                startActivity(intentBook);
                break;
            }
            case 1 : {
                Intent intentNote = new Intent(MainActivity.this, NoteCollect.class);
                startActivity(intentNote);
                break;
            }
            case 2 : {
                Toast.makeText(MainActivity.this, "第三行", Toast.LENGTH_LONG).show();
                break;
            }
            default:break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
