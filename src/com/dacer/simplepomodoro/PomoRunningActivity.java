package com.dacer.simplepomodoro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

import dacer.interfaces.OnClickCircleListener;
import dacer.service.CDService;
import dacer.utils.MyScreenLocker;
import dacer.utils.SetMyAlarmManager;
import dacer.views.CircleView;
import dacer.views.CircleView.RunMode;

public class PomoRunningActivity extends Activity implements OnClickCircleListener {
	CircleView mView;
	Boolean isFastMode;
	private static final int POMODORO_TIME = 25;
	private Boolean isDisplay = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//load full screen preference
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean isFullScreen = sp.getBoolean("pref_enable_fullscreen", false);
        Boolean isLightTheme = (sp.getString("pref_theme_type", "black").equals("white"));
        Boolean isLightsOn = sp.getBoolean("pref_lights_on", false);
        isFastMode = sp.getBoolean("pref_fast_mode", false);
        if(isFastMode){
        	MyScreenLocker locker = new MyScreenLocker(this);
            locker.myLockNow();
        }
        if(isLightsOn){
        	final Window win = getWindow();
        	win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
		if(isLightTheme){
			setTheme(isFullScreen ? R.style.MyFullScreenTheme_White : R.style.MyMainScreenTheme_White);
		}else{
			setTheme(isFullScreen ? R.style.MyFullScreenTheme_Black : R.style.MyMainScreenTheme_Black);
		}
		super.onCreate(savedInstanceState);
		//get Screen Width, Height
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
		mView = new CircleView(this,width/2, 
				height/2, bigCirRadius, 
				10, getString(R.string.start), 88, 0, this,RunMode.MODE_ONE);
		startCountDown(POMODORO_TIME, this);
		setContentView(mView);
	}

	private void startCountDown(final int totalTime,Context context){
		SetMyAlarmManager.schedulService(context, totalTime,CDService.class);//set alarm after 25min
        new CountDownTimer((long)totalTime*60*1000+1000, 1000) {
        	 int min, sec;
	         String secStr;
	         float sweepInc = 360f/(totalTime*60f);
	         float mSweep;
	         
  		     @Override
			public void onTick(long millisUntilFinished) {
  	            min = (int) (millisUntilFinished / 60000);
  	            sec = (int) ((millisUntilFinished - min *60000)/1000);
  	            if (sec < 10){
  	            	secStr = "0"+String.valueOf(sec);
  	            }else{
  	            	secStr = String.valueOf(sec);
  	            }
  	            mSweep = 360 - sweepInc * (millisUntilFinished / 1000);
  		    	mView.setMySweep(mSweep);
  		    	mView.setMyText(String.valueOf(min)+":"+ secStr);
  		    	mView.postInvalidate();
  		     }

  		     @Override
			public void onFinish() {
  		    	 finish();
  		     }
  		  }.start();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//        	Log.e("pomoAct","Back key");
        	AlertDialog d = new AlertDialog.Builder(this)
            .setTitle(getString(R.string.stop_pomodoro))
            .setMessage(getString(R.string.do_you_wish_to_stop))
            .setPositiveButton(R.string.running_in_background, new OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				Intent intent = new Intent(Intent.ACTION_MAIN);
//    				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				intent.addCategory(Intent.CATEGORY_HOME);
    				PomoRunningActivity.this.startActivity(intent);
    			}
    		})
            .setNegativeButton(R.string.stop, new OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				SetMyAlarmManager.stopschedulService(
    						PomoRunningActivity.this,CDService.class);
    				Intent intent = new Intent();
    			 	intent.setClass(PomoRunningActivity.this,
    						MainActivity.class);
    			 	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    			 	startActivity(intent);
    			 	finish();
    			 	overridePendingTransition(0, 0);
//    				startActivity(new Intent(PomoRunningActivity.this,
//    						MainActivity.class));
//    				finish();
    			}
    		})
          	.create();
    		d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    		d.show();
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

	@Override
	public void onClickCircle() {
		// TODO Auto-generated method stub
		//show/hide time text
		mView.setTextAlpha(isDisplay? 0 : 255);
		isDisplay = !isDisplay;
	}

}
