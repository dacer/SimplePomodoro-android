package com.dacer.simplepomodoro;

import dacer.service.BreakFinishService;
import dacer.utils.MyNotification;
import dacer.utils.SetMyAlarmManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class BreakFinishActivity extends Activity {
	private Button btn_continue;
	private TextView tvTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean isFullScreen = sp.getBoolean("pref_enable_fullscreen", false);
        Boolean isLightsOn = sp.getBoolean("pref_lights_on", false);
        setTheme(isFullScreen ? R.style.MyFullScreenTheme_White : R.style.MyMainScreenTheme_White);
        
        setContentView(R.layout.activity_break_finish);
		btn_continue = (Button)findViewById(R.id.btn_continu);
		tvTime = (TextView)findViewById(R.id.tv_time_up);
		
		Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		tvTime.setTypeface(roboto);
		btn_continue.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
       		 	intent.setClass(BreakFinishActivity.this,PomoRunningActivity.class);
       		 	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
       		 	startActivity(intent);
       		 	finish();
       		 	overridePendingTransition(0, 0);
			}
		});
		
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
