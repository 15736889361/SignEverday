package com.ling.signeveryday;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 横向RecyclerView基类adapter
 * @param <ItemDataType> 数据实体类型
 */
public abstract class BaseRecyclerAdapter<ItemDataType> extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected ArrayList<ItemDataType> mItemDataList = new ArrayList<ItemDataType>();
  public Context mContext;
    public BaseRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 动态增加一条数据
     * @param itemDataType 数据实体类对象
     */
    public void append(ItemDataType itemDataType){
        if (itemDataType!=null) {
            mItemDataList.add(itemDataType);
            notifyDataSetChanged();
        }
    }

    /**
     * 动态增加一组数据集合
     * @param itemDataTypes 数据实体类集合
     */
    public void append(List<ItemDataType> itemDataTypes){
        if (itemDataTypes.size()>0) {
            for (ItemDataType itemDataType : itemDataTypes) {
                mItemDataList.add(itemDataType);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 替换全部数据
     * @param itemDataTypes 数据实体类集合
     */
    public void replace(List<ItemDataType> itemDataTypes){
        mItemDataList.clear();
        if (itemDataTypes.size()>0) {
            mItemDataList.addAll(itemDataTypes);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除一条数据集合
     * @param position
     */
    public void remove(int position){
        mItemDataList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 移除所有数据
     */
    public void removeAll(){
        mItemDataList.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mItemDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        showData(viewHolder, i, mItemDataList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=createView(viewGroup, i);
        RecyclerView.ViewHolder holder = createViewHolder(view);
        return holder;
    }

/*public static class ViewHolder extends RecyclerView.ViewHolder {

private ImageView img_product;
private TextView tv_title;

public ViewHolder(View itemView) {

super(itemView);

img_product=(ImageView) itemView.findViewById(R.id.img_product);
tv_title=(TextView) itemView.findViewById(R.id.tv_title);
}

}*/

    /**
     * 显示数据抽象函数
     * @param viewHolder 基类ViewHolder,需要向下转型为对应的ViewHolder（example:MainRecyclerViewHolder mainRecyclerViewHolder=(MainRecyclerViewHolder) viewHolder;）
     * @param i 位置
     * @param mItemDataList 数据集合
     */
    public abstract void showData(RecyclerView.ViewHolder viewHolder, int i,List<ItemDataType> mItemDataList );
    /**
     * 加载item的view,直接返回加载的view即可
     * @param viewGroup 如果需要Context,可以viewGroup.getContext()获取
     * @param i
     * @return item 的 view
     */
    public abstract View createView(ViewGroup viewGroup, int i) ;
    /**
     * 加载一个ViewHolder,为RecyclerViewHolderBase子类,直接返回子类的对象即可
     * @param view item 的view
     * @return  RecyclerViewHolderBase 基类ViewHolder
     */
    public abstract RecyclerView.ViewHolder  createViewHolder(View view) ;
}

