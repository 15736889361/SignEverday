package com.ling.signeveryday.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author merserker
 * 一些图片转换工具，注意不同方法注释中的使用场合
 * 一，图片初始化时获取真实尺寸并且动态更改尺寸
 */
public class ImageEffectTool {
	
	/**
	 * ImageView 根据系统提供的Size动态修改比例，达到在屏幕上完美显示效果，不会变形
	 * 比如ImageView X轴fill_parent那么可以根据系统提供给X轴的数值修改Y轴数值达到图片不变形显示
	 * @param percent 是Width(或Height)根据Height（或Width）的高度呈一定比例显示
	 * @param setWidth 表示是否改变Width的值， flase表示改变Height的值
	 */
	 public static  void ImageDynamicChageSize(final View iv,final Boolean setWidth,final float percent){
		
	  	ViewTreeObserver vto = iv.getViewTreeObserver();   
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override   
		    public void onGlobalLayout() { 
		        iv.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
		    int h =  iv.getHeight();
		     int w=iv.getWidth();
	
		     ViewGroup.LayoutParams lp=   iv.getLayoutParams();
		   
		     if(setWidth)
		     lp.width=(int) (h*percent);
		     else
		    	  lp.height=(int) (w*percent); 
		     iv.setLayoutParams(lp);
		    }   
		});  
	}
	 
	 
	 
	 /**
	  * @author merserker
	  * 模糊处理特效，返回模糊处理后的Bitmap
	  * @path 来自Assets文件夹的资源路径
	  * @ct 上下文
	  */
//	public static Bitmap Blurred(String path,Context ct){
//		StackBlurManager _stackBlurManager = new StackBlurManager(getBitmapFromAsset(ct,path));
//	      _stackBlurManager.process(50);
//	      return _stackBlurManager.returnBlurredImage();
//		
//		
//	}
	/**
	 * 从Assets文件夹中获取资源文件的Bitmap
	 * @strName 来自Assets文件夹的资源路径
	 * @context 上下文
	 */
	 public static  Bitmap getBitmapFromAsset(Context context, String strName) {
	        AssetManager assetManager = context.getAssets();
	        InputStream istr;
	        Bitmap bitmap = null;
	        try {
	            istr = assetManager.open(strName);
	            bitmap = BitmapFactory.decodeStream(istr);
	        } catch (IOException e) {
	            return null;
	        }
	        return bitmap;
	    }
	 
	 /**
	  * 把资源文件加载到ImageList里面
	  * @context 上下文
	  * @path 资源文件路径
	  * @mImageList 存储ImageView的集合
	  */
	 public static void AddImageIntoImageList(Context context,String path,List<ImageView> mImageList,BitmapDisplayConfig bc){

		    String cachePath=context.getCacheDir().toString();
				
				BitmapUtils bu=new 	BitmapUtils(context,cachePath,0.8f);
			//	bu.configThreadPoolSize();
	
				ImageView v2=new ImageView(context);
				v2.setDrawingCacheEnabled(true);
			    v2.setScaleType(ScaleType.FIT_XY);
				bu.display(v2,path, bc);
		        
		//		ImageEffectTool.ImageDynamicChageSize(v2, false, 1);
				
				mImageList.add(v2);
				
		}
	 
	 
	 /**
	  * 修正版异步加载图片
	  * @param context
	  * @param path
	  * @param v2  调用者提供ImageView
	  * @param bc
	  */
	 public static void AddImageIntoImageList(Context context,String path,ImageView v2,BitmapDisplayConfig bc){

		    String cachePath=context.getCacheDir().toString();
			
			BitmapUtils bu=new 	BitmapUtils(context);
//			bu.configThreadPoolSize(10);

			//ImageView v2=new ImageView(context);
			//v2.setDrawingCacheEnabled(true);
		//    v2.setScaleType(ScaleType.FIT_XY);
			bu.display(v2,path, bc);
	//		ImageEffectTool.ImageDynamicChageSize(v2, false, 1);
			
//			mImageList.add(v2);
			
	}

}
