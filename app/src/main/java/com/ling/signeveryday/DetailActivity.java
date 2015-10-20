package com.ling.signeveryday;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.ling.signeveryday.utils.DbTools;
import com.ling.signeveryday.utils.ImageEffectTool;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    public Button b1;
    DatePicker dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calenderview);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("日期查询");
//初始化DatePicker
        dp= (DatePicker) findViewById(R.id.datePicker);
        Date dt=new Date();

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("日期查询");


        RecyclerView mRecyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        final RecyclerViewAdapter rmb=  new RecyclerViewAdapter(this) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new mViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_detail, parent, false));
            }
//生成卡片视图
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                mViewHolder mr=(mViewHolder)holder;
                mr.myclassmate.setText("时间："+mData.get(position).getMonth()+"月"+mData.get(position).getDay()+"日 "+mData.get(position).getTime());
                mr.myclass.setText("备注: "+mData.get(position).getTip());
                mr.myclass.setSelected(true);

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
                //配置Bitmap的压缩格式，现在我早已不用这种写法了
                BitmapDisplayConfig bc= new BitmapDisplayConfig();
                bc.setBitmapConfig(Bitmap.Config.RGB_565);
                bc.setShowOriginal(false);
                ImageEffectTool.AddImageIntoImageList(getApplicationContext(), mData.get(position).getPic(), mr.row_iv, bc);
                //点击卡片，进入照片浏览模式
                mr.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DetailActivity.this, InfoActivity.class); Bundle bd=new Bundle();
                        bd.putSerializable("cb", mData.get(position));
                        intent.putExtras(bd);
                        startActivity(intent);
                    }
                });
            }
        };
        //设置DatePicker为当前日期
        dp.init(dt.getYear()+1900,dt.getMonth(), dt.getDate(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                rmb.mData=new DbTools(DetailActivity.this).queForDate(monthOfYear+1,dayOfMonth,year);
                rmb.notifyDataSetChanged();
            }
        });
        //查询当前日期的签到信息
        rmb.mData=new DbTools(DetailActivity.this).queForDate(dt.getMonth()+1,dt.getDate(),dt.getYear()+1900);
        mRecyclerView.setAdapter(rmb);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
                break;
            case R.id.action_datepicker:
                if(dp.getVisibility()!=View.VISIBLE)
                    dp.setVisibility(View.VISIBLE);
                else
                    dp.setVisibility(View.INVISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_date, menu);
        return true;
    }

    //使用静态内部类，避免引用父类
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
            row_iv=(ImageView) v.findViewById(R.id.row_iv); row_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            row_title=(TextView) v.findViewById(R.id.row_title);
            myclassmate=(TextView) v.findViewById(R.id.myclass_classmateprogress_text);
            myclass=(TextView) v.findViewById(R.id.myclass_myprogress_text);
            credit=(TextView) v.findViewById(R.id.credit);
        }
    }


}
