package com.dacer.simplepomodoro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.LinePageIndicator;

import dacer.adapters.MyPagerAdapter;
import dacer.settinghelper.SettingUtility;
import dacer.utils.GlobalContext;
import dacer.utils.MyNotification;
import dacer.utils.MyUtils;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class MainActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(SettingUtility.getTheme());
		GlobalContext.setActivity(this);
		setContentView(R.layout.activity_main);
		
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		LinePageIndicator mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
		if(SettingUtility.isLightTheme()){
			mIndicator.setBackgroundColor(Color.WHITE);
		}
        mIndicator.setViewPager(pager);
        if (SettingUtility.enableGTask()){
            mIndicator.setCurrentItem(1);
        }
        
        //for long break
        MyUtils.deleteContinueTimes(this);
        
        
	}

	@Override
	protected void onResume(){
		super.onResume();
		MyNotification noti = new MyNotification(this);
		noti.cancelNotification();
		MobclickAgent.onResume(this);
	}
	

	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
        	return false;
        }else if (keyCode == KeyEvent.KEYCODE_MENU) { 
    	   	startActivity(new Intent(MainActivity.this,SettingActivity.class));
    	   	finish();
    	   	return false;
    	} 
        return false;
    }

}
