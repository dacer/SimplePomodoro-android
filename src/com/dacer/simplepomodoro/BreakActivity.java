package com.dacer.simplepomodoro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import dacer.service.BreakFinishService;
import dacer.utils.MyNotification;
import dacer.utils.MyPomoRecorder;
import dacer.utils.MyScreenLocker;
import dacer.utils.MyUtils;
import dacer.utils.SetMyAlarmManager;

public class BreakActivity extends Activity {
	private ProgressBar mProgressBar;
	private TextView tvTime;
	private int breakDuration;
	private int longBreakDuration;
	TextView longBreakTV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//preference
		final Window win = getWindow();
		MyPomoRecorder record = new MyPomoRecorder(this);
		Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean isFastMode = sp.getBoolean("pref_fast_mode", false);
		Boolean isFullScreen = sp.getBoolean("pref_enable_fullscreen", false);
        Boolean isLightsOn = sp.getBoolean("pref_lights_on", false);
        breakDuration = Integer.parseInt(sp.getString("pref_break_duration", "5"));
        longBreakDuration = Integer.parseInt(sp.getString("pref_long_break_duration", "25"));
        if(isFastMode){
        	MyScreenLocker locker = new MyScreenLocker(this);
            locker.myLockNow();
        }
        if(isLightsOn){
        	win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        setTheme(isFullScreen ? R.style.MyFullScreenTheme_White : R.style.MyMainScreenTheme_White);
        
//        breakDuration = 1;//<For TEST-------------------------------------
		setContentView(R.layout.activity_break);
		
      if(MyUtils.getContinueTimes(this)%4 == 0){
    	TextView longBreakTV = (TextView)findViewById(R.id.tv_long_break);
    	longBreakTV.setTypeface(roboto);
    	longBreakTV.setVisibility(View.VISIBLE);
    	breakDuration = longBreakDuration; // Long break
    	MyUtils.deleteContinueTimes(this);
    }
		
		tvTime = (TextView)findViewById(R.id.tv_time);
		mProgressBar = (ProgressBar)findViewById(R.id.pb_time);
		
		tvTime.setTypeface(roboto);
		
		mProgressBar.setMax(breakDuration * 60);
		
		SetMyAlarmManager.schedulService(this, 
				breakDuration, 
				BreakFinishService.class);
		
		new CountDownTimer((long)breakDuration*60*1000+1000, 1000) {
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
 	            remainSec = (int) (breakDuration*60 - millisUntilFinished/1000);
 	            mProgressBar.setProgress(remainSec);
 		    	
 		    }

 		    @Override
			public void onFinish() {
 		    	finish();
 		    }
 		  }.start();
 		  
 		  
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
	protected void onDestroy(){
		super.onDestroy();
//		SetMyAlarmManager.stopschedulService(this, BreakFinishService.class);
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
}
