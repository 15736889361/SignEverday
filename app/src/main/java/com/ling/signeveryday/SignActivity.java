package com.ling.signeveryday;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ling.signeveryday.utils.DbTools;

import java.io.File;

public class SignActivity extends AppCompatActivity {
    public Button b1;
    private static final int PHOTO_REQUEST_CAMERA = 10;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private  String PHOTO_FILE_NAME;
    public TextView tosign;
    public CardBean cb;
    public ProgressDialog progressDialog;

    private File tempFile;
    public TextView youadr;
    public TextView youpic;
    public TextView youtip;
    @ViewInject(R.id.textView)
    public TextView t1;
    FloatingActionButton fab1; FloatingActionButton fab2;FloatingActionButton fab3;
    FloatingActionButton fab11; FloatingActionButton fab22;FloatingActionButton fab33;
    @ViewInject(R.id.textView2)
    public TextView t2;  boolean isok=false;boolean islocation=false;
    @ViewInject(R.id.textView3)
    public TextView t3;
    private LocationClient mLocationClient; public int a[];

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //当后台都被杀掉，就剩前台的你时
        tempFile=null;
        progressDialog=null;
        //简单释放一下内存
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("--Des--", "run");
        CoreUtil.removeAppActivity(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tosign);
        CoreUtil.addAppActivity(this);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("我要签到");
        cb=new CardBean();
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("我要签到");
        //实例化各种控件，早就不用这种方法了，现在都用注解IOC了
        fab1= (FloatingActionButton) findViewById(R.id.fab1);fab11= (FloatingActionButton) findViewById(R.id.fab11);
        fab2=(FloatingActionButton) findViewById(R.id.fab2);fab22=(FloatingActionButton) findViewById(R.id.fab22);
        fab3=(FloatingActionButton) findViewById(R.id.fab3);fab33=(FloatingActionButton) findViewById(R.id.fab33);
        fab2.setClickable(false); fab2.setClickable(false); tosign= (TextView) findViewById(R.id.tosign);
        t1= (TextView) findViewById(R.id.textView);t2= (TextView) findViewById(R.id.textView2);t3= (TextView) findViewById(R.id.textView3);
        //百度地图定位引擎初始化，适当的释放有利与回收内存
        mLocationClient = new LocationClient(this.getApplicationContext());
        youadr= (TextView) findViewById(R.id.youadr);
        //定位回调哦
        MyLocationListener mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        youtip= (TextView) findViewById(R.id.youtip);  youpic= (TextView) findViewById(R.id.youpic);

        tosign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否可以提交签到信息
                if (isok && islocation) {
                    new DbTools(SignActivity.this).insertData(cb);
                    Toast.makeText(SignActivity.this, "签到成功！", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(SignActivity.this, "请先完成1,2步", Toast.LENGTH_SHORT).show();
            }
        });
        //进入ACITIVITY后自动进行一次定位
        checkin(fab1);
    }

    /**
     * 启动定位，签到第一步
     * @param view
     */
    public void checkin(View view){
        initLocation();
        mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
        progressDialog= ProgressDialog.show(SignActivity.this, null,"全球定位中...");
        progressDialog.setCancelable(true);
        //如果中断定位，显示提示
        youadr.setText("定位被中断");
        progressDialog.show();
    }
/**
 * 启动拍照，签到第二步
 */
    public void checkin2(View view){
        File fd = new File(Environment
                .getExternalStorageDirectory() + "/signphoto");
        if (!fd.exists())
            fd.mkdirs();
        PHOTO_FILE_NAME = System.currentTimeMillis() + ".jpg";
        camera(t1);
    }

    public void checkin3(View view){
        showDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化定位
     */
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系，
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    /**
     * 监听回调事件
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
        //解析定位时间，存入CardBean
            String date1=location.getTime();
            String a[]=date1.split("-");
            cb.setYear(Integer.parseInt(a[0]));
            cb.setMonth(Integer.parseInt(a[1]));
            String b[]=a[2].split(" ");
            cb.setDay(Integer.parseInt(b[0]));
            cb.setTime(b[1]);

            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                cb.setAddress(location.getAddrStr().substring(2,location.getAddrStr().length())+","+location.getLocationDescribe().substring(1, location.getLocationDescribe().length()));

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                cb.setAddress(location.getAddrStr().substring(2,location.getAddrStr().length())+","+location.getLocationDescribe().substring(1, location.getLocationDescribe().length()));
            }
            else{
                cb.setAddress("获取地理位置失败");
            }

         if(cb.getAddress().contentEquals("获取地理位置失败"))
             youadr.setText("获取地理位置失败, 请检查网络是否开启");

            else {
             //定位成功了
             youadr.setText(cb.getAddress());islocation=true;

             Snackbar.make(fab1, "当前定位成功!", Snackbar.LENGTH_LONG).setAction("去拍照", new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     File fd = new File(Environment
                             .getExternalStorageDirectory() + "/signphoto");
                     if (!fd.exists())
                         fd.mkdirs();
                     PHOTO_FILE_NAME = System.currentTimeMillis() + ".jpg";
                     camera(t1);
                 }
             }).show();
             FabchangeState(fab1); fab2.setClickable(true);
         }
            mLocationClient.stop();
            progressDialog.dismiss();
        }

    }

    public void camera(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory()+"/signphoto", PHOTO_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         if (requestCode == PHOTO_REQUEST_CAMERA) {
            if(resultCode==RESULT_OK){
            if (hasSdcard()) {
                tempFile = new  File(Environment.getExternalStorageDirectory()+"/signphoto", PHOTO_FILE_NAME);
                cb.setPic(tempFile.getAbsolutePath());
                FabchangeState(fab2);
                fab3.setClickable(true);
                isok=true;
                youpic.setText("已获取您的照片");
                //showDialog();
            } else {
                Toast.makeText(SignActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
            }}
            else
                Toast.makeText(SignActivity.this, "未获取照片", Toast.LENGTH_LONG).show();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /*
         * SD卡是否可读
         */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 签到备注~~~
     */
    public void showDialog(){
       LinearLayout lb=(LinearLayout) getLayoutInflater().inflate(R.layout.tipdialog, null);
        final EditText et= (EditText) lb.findViewById(R.id.editText);
               AlertDialog.Builder builder = new AlertDialog.Builder(SignActivity.this)
                       .setTitle("签到备注").setView(lb).setPositiveButton("签到", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               if(!et.getText().toString().contentEquals(""))
                               cb.setTip(et.getText().toString());
                               youtip.setText("我的备注: "+et.getText().toString());
                               FabchangeState(fab3);
                           }
                       }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                           }
                       });
        builder.show();
    }

    /**
     *按照提示完成步骤后，会变色打勾
     * @param fab
     */
    public void FabchangeState(final FloatingActionButton fab){
        fab.setClickable(false);
        fab.setImageResource(R.drawable.ic_done);
        fab.setBackgroundTintList(createSelector());
    }
//改变FAB的颜色
    private ColorStateList createSelector() {
        int statePressed = android.R.attr.state_pressed;
        int stateFocesed = android.R.attr.state_focused;
        int[][] state = {{statePressed},{-statePressed},{stateFocesed},{-stateFocesed}};
        int color1 = getResources().getColor(R.color.material_blue_grey_800);
        int color2 = getResources().getColor(R.color.material_green);
        int color3 = getResources().getColor(R.color.material_blue_grey_800);
        int color4 = getResources().getColor(R.color.cardview_shadow_end_color);
        int[] color = {color1, color2, color3, color4};
        ColorStateList colorStateList = new ColorStateList(state,color);
        return colorStateList;
    }
}
