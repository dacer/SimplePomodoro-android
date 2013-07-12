package com.dacer.simplepomodoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.LinePageIndicator;

import dacer.adapters.MyPagerAdapter;
import dacer.utils.MyNotification;
import dacer.utils.MyPomoRecorder;
import dacer.utils.MyUtils;
import dacer.utils.MyPomoRecorder.PomoType;


public class MainActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//load full screen preference
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean isFullScreen = sp.getBoolean("pref_enable_fullscreen", false);
        Boolean isLightTheme = (sp.getString("pref_theme_type", "black").equals("white"));
		if(isLightTheme){
			setTheme(isFullScreen ? R.style.MyFullScreenTheme_White : R.style.MyMainScreenTheme_White);
		}else{
			setTheme(isFullScreen ? R.style.MyFullScreenTheme_Black : R.style.MyMainScreenTheme_Black);
		}
		setContentView(R.layout.activity_main);
		
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
//		pager.setCurrentItem(1);
		LinePageIndicator mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
		if(isLightTheme){
			mIndicator.setBackgroundColor(Color.WHITE);
		}
        mIndicator.setViewPager(pager);
//        mIndicator.setCurrentItem(1);
        
        //for long break
        MyUtils.deleteContinueTimes(this);
        
        
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
	
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
}
