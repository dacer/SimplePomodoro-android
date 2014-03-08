package com.dacer.simplepomodoro;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Author:dacer
 * Date  :Mar 8, 2014
 */
public class CustomViewPager extends ViewPager{

	public CustomViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        Log.e("ddd", "MyLayout onTouchEvent.");  
        Log.e("dddd","MyLayout onTouchEvent default return "   
        + super.onTouchEvent(event));  
        return super.onTouchEvent(event);  
    } 
}
