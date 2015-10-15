package com.ling.signeveryday.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ScorllView下的recyclerView,但是效果不好
 * Created by sks on 2015/8/1.
 */
public class XrecyclerView extends RecyclerView{
    public XrecyclerView(Context context) {
        super(context);
    }

    public XrecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }
}
