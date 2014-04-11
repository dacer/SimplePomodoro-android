package com.dacer.simplepomodoro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
import dacer.utils.MyScreenLocker;
import dacer.utils.MyUtils;
import dacer.utils.SetMyAlarmManager;
import dacer.views.CircleView;
import dacer.views.CircleView.RunMode;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class PomoRunningActivity extends Activity implements OnClickCircleListener {
	private CircleView mView;
	private int width;
	private int height;
	private float bigCirRadius;

	private enum CenterSize{NONE,MIDDLE,BIG};
	private CenterSize centerSize = CenterSize.MIDDLE;
	private boolean isDisplay = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GlobalContext.setActivity(this);
        if(SettingUtility.isFastMode()){
        	MyScreenLocker locker = new MyScreenLocker(this);
            locker.myLockNow();
        }
    	final Window win = getWindow();
        if(SettingUtility.isLightsOn()){
        	win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        setTheme(SettingUtility.getTheme());
		super.onCreate(savedInstanceState);
		
		//get Screen Width, Height
		DisplayMetrics metrics = new DisplayMetrics();   
		getWindowManager().getDefaultDisplay().getMetrics(metrics);   
		width = metrics.widthPixels;   
		height = metrics.heightPixels; 
		
		if(width < height){
			bigCirRadius = (float) (width * 0.4);
		}else{
			bigCirRadius = (float) (height * 0.4);
		}
		mView = new CircleView(this,width/2, 
				height/2, bigCirRadius, 
				getString(R.string.start), 0, this,RunMode.MODE_ONE);
		
		showContinueView();
		setContentView(mView);
		
        
		  win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//		    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

//    	startService(new Intent(this,ScreenLockerService.class));
		  
			if(SettingUtility.isSilentMode()){
				SettingUtility.setMobileNetworkEnabled(MyUtils.isMobileDataEnabled(this));
				SettingUtility.setWifiEnabled(MyUtils.isWifiEnabled(this));
				MyUtils.controlNetwork(false, this);
			}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
//		stopService(new Intent(this,ScreenLockerService.class));
	}
	
	private void startCountDown(final int leftTimeInSec, final int totalTimeInMin, Context context){
//		SetMyAlarmManager.schedulService(context, totalTime,CDService.class);
		SettingUtility.setRunningType(SettingUtility.POMO_RUNNING);
		final int leftTimeInMin = leftTimeInSec/60;
        new CountDownTimer((long)leftTimeInSec*1000+1000, 1000) {
        	 int min, sec;
	         String secStr;
	         float sweepInc = 360f/(totalTimeInMin*60f);
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
  	            mSweep = 360*(leftTimeInMin/totalTimeInMin) - sweepInc * (millisUntilFinished / 1000);
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
        	AlertDialog d = new AlertDialog.Builder(this)
            .setTitle(getString(R.string.stop_pomodoro))
            .setMessage(getString(R.string.do_you_wish_to_stop))
            .setPositiveButton(R.string.running_in_background, new OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				Intent intent = new Intent(Intent.ACTION_MAIN);
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
    			 	//退出时复原
    			 	if(SettingUtility.isSilentMode()){
    					MyUtils.controlNetwork(true, getApplicationContext());
    				}
    			 	finish();
    			 	overridePendingTransition(0, 0);
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
        SettingUtility.setRunningType(SettingUtility.POMO_RUNNING);
	}

	@Override
	public void onClickCircle() {
		// TODO Auto-generated method stub
		isDisplay = !isDisplay;
		mView.setTextAlpha(isDisplay? 255:0);
//		switch (centerSize) {
//		case NONE:
//			mView.setTextSize(MyUtils.dipToPixels(this,45f));
//			centerSize = CenterSize.MIDDLE;
//			break;
//		case MIDDLE:
//			mView.setTextSize(MyUtils.dipToPixels(this,90f));	
//			centerSize = CenterSize.BIG;
//			break;
//		case BIG:
//			mView.setTextSize(0);
//			centerSize = CenterSize.NONE;
//			break;
//		default:
//			break;
//		}
	}
	
	
	private void showContinueView(){
		
		final int leftTimeInSec;
		long finishTime = SettingUtility.getFinishTimeInMills();
		long nowTime = MyUtils.getCurrentUTCInMIlls();
//		Log.e("finishTime", String.valueOf(finishTime));
//		Log.e("nowTime", String.valueOf(nowTime));
		int runningType = SettingUtility.getRunningType();
//		Log.e("runningtype", String.valueOf(runningType));
		
		if((finishTime > nowTime) &&(runningType == SettingUtility.POMO_RUNNING)){
			leftTimeInSec = (int) ((finishTime - nowTime + 1000)/1000);
		}else {
	        SettingUtility.setRunningType(SettingUtility.POMO_RUNNING);
			SetMyAlarmManager.schedulService(this, 
					SettingUtility.getPomodoroDuration(), 
					CDService.class);
			leftTimeInSec = SettingUtility.getPomodoroDuration()*60;
		}
		startCountDown(leftTimeInSec, SettingUtility.getPomodoroDuration(), this);

	}

//	private void hideSystemUI() {
//	    // Set the IMMERSIVE flag.
//	    // Set the content to appear under the system bars so that the content
//	    // doesn't resize when the system bars hide and show.
//		getWindow().getDecorView().setSystemUiVisibility(
//	            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//	            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//	            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//	            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//	            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//	            | View.SYSTEM_UI_FLAG_IMMERSIVE);
//	}
}
