package com.dacer.simplepomodoro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import dacer.interfaces.OnClickCircleListener;
import dacer.service.CDService;
import dacer.settinghelper.SettingUtility;
import dacer.utils.GlobalContext;
import dacer.utils.MyNotification;
import dacer.utils.MyScreenLocker;
import dacer.utils.SetMyAlarmManager;
import dacer.views.CircleView;
import dacer.views.CircleView.RunMode;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class PomoRunningActivity extends Activity implements OnClickCircleListener {
	private CircleView mView;
	private Boolean isDisplay = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GlobalContext.setActivity(this);
	    
        if(SettingUtility.isFastMode()){
        	MyScreenLocker locker = new MyScreenLocker(this);
            locker.myLockNow();
        }
        if(SettingUtility.isLightsOn()){
        	final Window win = getWindow();
        	win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        setTheme(SettingUtility.getTheme());
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
				getString(R.string.start), 0, this,RunMode.MODE_ONE,this);
		startCountDown(SettingUtility.getPomodoroDuration(), this);
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
	protected void onPause(){
		super.onPause();
		MyNotification mn = new MyNotification(PomoRunningActivity.this);
        mn.showSimpleNotification(getString(R.string.sp_is_running),
        		getString(R.string.click_to_return), true,
        		PomoRunningActivity.class);
	}

	@Override
	protected void onResume(){
		super.onResume();
		MyNotification noti = new MyNotification(this);
		noti.cancelNotification();
	}
	
	@Override
	public void onClickCircle() {
		// TODO Auto-generated method stub
		//show/hide time text
		mView.setTextAlpha(isDisplay? 0 : 255);
		isDisplay = !isDisplay;
	}

}
