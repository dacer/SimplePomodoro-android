package com.dacer.simplepomodoro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

import dacer.interfaces.OnClickCircleListener;
import dacer.utils.MyNotification;
import dacer.utils.MyPomoRecorder;
import dacer.utils.MyPomoRecorder.PomoType;
import dacer.utils.MyUtils;
import dacer.views.CircleView;

public class FinishScreenActivity extends Activity implements OnClickCircleListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Boolean isFullScreen = intent.getBooleanExtra(
				MyUtils.INTENT_IS_FULLSCREEN, false);
		//load full screen preference
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean isFullScreen_sp = sp.getBoolean("pref_enable_fullscreen", false);
		Boolean isLightsOn = sp.getBoolean("pref_lights_on", false);
		
		if(isFullScreen || isFullScreen_sp){
			setTheme(R.style.MyFullScreenTheme_Black);
		}
		
		DisplayMetrics metrics = new DisplayMetrics();   
		getWindowManager().getDefaultDisplay().getMetrics(metrics);   
		int width = metrics.widthPixels;   
		int height = metrics.heightPixels; 
		float bigCirRadius;
		if(width < height){
        	bigCirRadius = (float) (width * 0.4);
        }else{
        	bigCirRadius = (float) (height * 0.4);
        }
		View mView = new CircleView(this, width/2, 
				height/2, bigCirRadius, 
				10, "Tap to Break", 88, 360, this,CircleView.RunMode.MODE_TWO);
		setContentView(mView);
		final Window win = getWindow();
		  win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//		    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		  
		  if(isLightsOn){
	        	win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        }
		  
		//nofitication
		  MyNotification mn = new MyNotification(this);
		  mn.cancelNotification();
	}


	@Override
	public void onClickCircle() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		 	intent.setClass(this,BreakActivity.class);
//		 	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		 	startActivity(intent);
		 	finish();
//		 	overridePendingTransition(0, 0);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	startActivity(new Intent(this,
					MainActivity.class));
    		finish();
        	return false;
        }
        return false;
    }
}
