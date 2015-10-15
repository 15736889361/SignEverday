package com.ling.signeveryday;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.ling.signeveryday.utils.DbTools;
import com.ling.signeveryday.utils.ImageEffectTool;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<CardBean> viewList=new ArrayList<CardBean>();
    private LinearLayout line; public RecyclerViewAdapter rmb;
 /*   private DbUtils db = DbUtils.create(this);*/

    /**
     * 从DB获取最近七天的数据
     */
    @Override
    protected void onResume() {
        rmb.mData=new DbTools(this).que7();
        rmb.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CoreUtil.addAppActivity(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("每日签到");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
//显示卡片视图
        RecyclerView mRecyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        rmb= new RecyclerViewAdapter(this) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new mViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_detail, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                mViewHolder mr=(mViewHolder)holder;
                mr.myclassmate.setText("时间: "+mData.get(position).getMonth()+"月"+mData.get(position).getDay()+"日 "+mData.get(position).getTime());
                mr.myclass.setText("备注: "+mData.get(position).getTip());
                mr.myclass.setSelected(true);
                //不同的签到状态，显示不同的颜色
                String sb=mData.get(position).getResult();
                mr.credit.setText(sb);

                switch(sb){
                    case"分秒不差":     mr.credit.setTextColor(Color.parseColor("#FFFF33")); break;
                    case"姗姗来迟":     mr.credit.setTextColor(Color.parseColor("#FF4081")); break;
                    case"闻鸡起舞":     mr.credit.setTextColor(Color.parseColor("#2EE247")); break;
                    case"披星戴月":     mr.credit.setTextColor(Color.parseColor("#336699")); break;
                    case"提前早退":     mr.credit.setTextColor(Color.parseColor("#2f7d6a")); break;
                }
                mr.row_title.setText(mData.get(position).getAddress());
                mr.row_title.setSelected(true);
               BitmapDisplayConfig bc= new BitmapDisplayConfig();
                bc.setBitmapConfig(Bitmap.Config.RGB_565);
                bc.setShowOriginal(false);
                //显示照片略缩图
                ImageEffectTool.AddImageIntoImageList(getApplicationContext(), mData.get(position).getPic(), mr.row_iv, bc);

                mr.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,InfoActivity.class); Bundle bd=new Bundle();
                        bd.putSerializable("cb",mData.get(position));
                        intent.putExtras(bd);
                        startActivity(intent);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(rmb);
//快速签到
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SignActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CoreUtil.removeAppActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧滑菜单栏
     * @param navigationView
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                break;
                            case R.id.nav_messages:
                                startActivity(new Intent(MainActivity.this, SignActivity.class));
                                break;
                            case R.id.nav_friends:
                                startActivity(new Intent(MainActivity.this, DetailActivity.class));
                                break;
                            case R.id.about:
                                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                                break;
                        }
                        //    menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    //  静态内部类，避免持有父类引用
   static class mViewHolder extends RecyclerView.ViewHolder{
        ImageView row_iv;
        TextView row_title;
        TextView myclassmate;
        TextView myclass;
        TextView credit;
        View mView;
        public mViewHolder(View v){
            super(v);
            this.mView=v;
            row_iv=(ImageView) v.findViewById(R.id.row_iv);row_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            row_title=(TextView) v.findViewById(R.id.row_title);
            myclassmate=(TextView) v.findViewById(R.id.myclass_classmateprogress_text);
            myclass=(TextView) v.findViewById(R.id.myclass_myprogress_text);
            credit=(TextView) v.findViewById(R.id.credit);
        }
    }

}
