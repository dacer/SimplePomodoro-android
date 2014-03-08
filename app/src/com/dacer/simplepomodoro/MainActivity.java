package com.dacer.simplepomodoro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dacer.androidcharts.TempLog;
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
		GlobalContext.setActivity(this);
		initNowRunningType();
		setTheme(SettingUtility.getTheme());
    	final Window win = getWindow();
		if(SettingUtility.isLightsOn()){
        	win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
		setContentView(R.layout.activity_main);
		
		CustomViewPager pager = (CustomViewPager)findViewById(R.id.pager);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		LinePageIndicator mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
		if(SettingUtility.isLightTheme()){
			mIndicator.setBackgroundColor(Color.WHITE);
		}
        mIndicator.setViewPager(pager);
        mIndicator.setCurrentItem(1);
        //for long break
        MyUtils.deleteContinueTimes(this);
    
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
    		    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
    		    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}

	@Override
	protected void onResume(){
		super.onResume();
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
	
	private void initNowRunningType(){
		long finishTime = SettingUtility.getFinishTimeInMills();
		long nowTime = MyUtils.getCurrentUTCInMIlls();
		int runningType = SettingUtility.getRunningType();
		if((finishTime > nowTime) &&(runningType != SettingUtility.NONE_RUNNING)){
			if(runningType == SettingUtility.POMO_RUNNING){
				startActivity(new Intent(MainActivity.this, PomoRunningActivity.class));
			}else if(runningType == SettingUtility.BREAK_RUNNING){
				startActivity(new Intent(MainActivity.this, BreakActivity.class));
			}
			finish();
		}
	}

}
