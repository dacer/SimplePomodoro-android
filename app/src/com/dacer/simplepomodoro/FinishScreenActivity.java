package com.dacer.simplepomodoro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import dacer.interfaces.OnClickCircleListener;
import dacer.settinghelper.SettingUtility;
import dacer.utils.GlobalContext;
import dacer.utils.MyNotification;
import dacer.utils.MyUtils;
import dacer.views.CircleView;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class FinishScreenActivity extends Activity implements OnClickCircleListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalContext.setActivity(this);
		Intent intent = getIntent();
		Boolean isFullScreen = intent.getBooleanExtra(
				MyUtils.INTENT_IS_FULLSCREEN, false);
		Boolean isFullScreen_sp = SettingUtility.isFullScreen();
		
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
				getString(R.string.tap_to_break), 360, this,CircleView.RunMode.MODE_TWO,this);
		setContentView(mView);
		final Window win = getWindow();
		  win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//		    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		  
		  if(SettingUtility.isLightsOn()){
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
	protected void onResume(){
		super.onResume();
		MyNotification noti = new MyNotification(this);
		noti.cancelNotification();
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
