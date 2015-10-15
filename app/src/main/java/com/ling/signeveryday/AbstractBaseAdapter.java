package com.ling.signeveryday;
import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单封装的BaseAdapter
 * @param <T> 数组内容的类型
 */
public abstract class AbstractBaseAdapter <T> extends BaseAdapter{
		public ArrayList<T>data = new ArrayList<T>();
		public Context mContext;

		/** 构造函数 */
		public AbstractBaseAdapter(Context context) {
			this.mContext = context;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

public void addAll(List l){
	data.addAll(l);
}
public void addItem(T t1){
	data.add(t1);
}
public void clear(){
	data.clear();
}
public void removeItem(T t1){
	data.remove(t1);
}
public void removeAll(List l){
	data.removeAll(l);
}

}
