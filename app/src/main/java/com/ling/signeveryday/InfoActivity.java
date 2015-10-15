package com.ling.signeveryday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.ling.signeveryday.utils.ImageEffectTool;

public class InfoActivity extends AppCompatActivity {

    public BitmapDisplayConfig bc=new BitmapDisplayConfig();
    public ImageView iv; public TextView info_time; public TextView info_date;
    public TextView info_location;  public TextView info_tip; public CardBean cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
       cb= (CardBean) getIntent().getSerializableExtra("cb");
      iv= (ImageView) findViewById(R.id.imageView);
        info_date= (TextView) findViewById(R.id.info_date);
        info_location= (TextView) findViewById(R.id.info_loaction);
        info_time= (TextView) findViewById(R.id.info_time);
        info_tip=(TextView) findViewById(R.id.info_tip);
        //加载照片
        ImageEffectTool.AddImageIntoImageList(InfoActivity.this, cb.getPic(), iv, bc);
        //显示签到信息
        info_tip.setText("备注: "+cb.getTip()); info_location.setText(cb.getAddress());
        info_time.setText(cb.getTime().substring(0,5));
        info_date.setText(cb.getYear()+"年"+cb.getMonth()+"月"+cb.getDay()+"日");

    }



}
