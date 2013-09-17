package com.dacer.simplepomodoro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import dacer.service.BreakFinishService;
import dacer.settinghelper.SettingUtility;
import dacer.utils.GlobalContext;
import dacer.utils.MyNotification;
import dacer.utils.MyScreenLocker;
import dacer.utils.MyUtils;
import dacer.utils.SetMyAlarmManager;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class BreakActivity extends Activity {
	private ProgressBar mProgressBar;
	private TextView tvTime;
	private int maxBreakDuration;
	private int longBreakDuration;
	TextView longBreakTV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalContext.setActivity(this);
		//preference
		final Window win = getWindow();
		Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		maxBreakDuration = SettingUtility.getBreakDuration();
        longBreakDuration = SettingUtility.LONG_BREAK_DURATION;
        if(SettingUtility.isFastMode()){
        	MyScreenLocker locker = new MyScreenLocker(this);
            locker.myLockNow();
        }
        if(SettingUtility.isLightsOn()){
        	win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        setTheme(SettingUtility.getTheme());
		setContentView(R.layout.activity_break);
		
      if(MyUtils.getContinueTimes(this)%4 == 0 && MyUtils.getContinueTimes(this) != 0){
    	TextView longBreakTV = (TextView)findViewById(R.id.tv_long_break);
    	longBreakTV.setTypeface(roboto);
    	longBreakTV.setVisibility(View.VISIBLE);
    	maxBreakDuration = longBreakDuration; // Long break
    }
        tvTime = (TextView)findViewById(R.id.tv_time);
		mProgressBar = (ProgressBar)findViewById(R.id.pb_time);
		
		tvTime.setTypeface(roboto);
		showContinueView();
 		  
	}

	@Override
	protected void onPause(){
		super.onPause();
		MyNotification mn = new MyNotification(BreakActivity.this);
        mn.showSimpleNotification(getString(R.string.break_time),
        		getString(R.string.click_to_return), true,
        		MainActivity.class);
        SettingUtility.setRunningType(SettingUtility.BREAK_RUNNING);
	}

	@Override
	protected void onResume(){
		super.onResume();
		MyNotification noti = new MyNotification(this);
		noti.cancelNotification();
//		SettingUtility.setRunningType(SettingUtility.NONE_RUNNING);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//exit confirm dialog
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        		AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.stop_pomodoro))
                .setMessage(getString(R.string.do_you_wish_to_stop))
                .setPositiveButton(R.string.running_in_background, new OnClickListener() {
        			
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
        				// TODO Auto-generated method stub
        				Intent intent = new Intent(Intent.ACTION_MAIN);
//        				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        				intent.addCategory(Intent.CATEGORY_HOME);
        				BreakActivity.this.startActivity(intent);
        			}
        		})
                .setNegativeButton(R.string.stop, new OnClickListener() {
        			
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
        				// TODO Auto-generated method stub
        				SetMyAlarmManager.stopschedulService(
        						BreakActivity.this, BreakFinishService.class);
        				startActivity(new Intent(BreakActivity.this,
        						MainActivity.class));
        				finish();
        			}
        		})
              	.create();
        		d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        		d.show();
        	return false;
        }
        return false;
    }
	
	
	private void showContinueView(){
		mProgressBar.setMax(maxBreakDuration * 60);
		int nowProcess = 0;
		final long leftTimeInMills;
		
		long finishTime = SettingUtility.getFinishTimeInMills();
		long nowTime = MyUtils.getCurrentGMTTimeInMIlls();
		int runningType = SettingUtility.getRunningType();
		if((finishTime > nowTime) &&(runningType == SettingUtility.BREAK_RUNNING)){
			leftTimeInMills = finishTime - nowTime + 1000;
			nowProcess = (int) (maxBreakDuration*60 - leftTimeInMills/1000);
			mProgressBar.setProgress(nowProcess);
		}else {
			SetMyAlarmManager.schedulService(this, 
					maxBreakDuration, 
					BreakFinishService.class);
			leftTimeInMills = (long)maxBreakDuration*60*1000+1000;
		}

		
		SettingUtility.setRunningType(SettingUtility.BREAK_RUNNING);
		new CountDownTimer(leftTimeInMills, 1000) {
       	 	int min, sec, remainSec;
	        String secStr; 
 		    @Override
			public void onTick(long millisUntilFinished) {
 	            min = (int) (millisUntilFinished / 60000);
 	            sec = (int) ((millisUntilFinished - min *60000)/1000);
 	            if (sec < 10){
 	            	secStr = "0"+String.valueOf(sec);
 	            }else{
 	            	secStr = String.valueOf(sec);
 	            }
 	            tvTime.setText(min+":"+secStr);
 	            remainSec = (int) (leftTimeInMills - millisUntilFinished)/1000;
 	            mProgressBar.setProgress(remainSec);
 		    	
 		    }

 		    @Override
			public void onFinish() {
 		    	finish();
 		    }
 		  }.start();
	}
}
