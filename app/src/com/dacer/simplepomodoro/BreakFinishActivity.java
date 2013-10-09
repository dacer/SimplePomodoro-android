package com.dacer.simplepomodoro;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import dacer.settinghelper.SettingUtility;
import dacer.utils.GlobalContext;
import dacer.utils.MyNotification;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class BreakFinishActivity extends Activity {
	private Button btn_continue;
	private TextView tvTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalContext.setActivity(this);
        setTheme(SettingUtility.getThemeWhite());
        
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
		ImageView iv = (ImageView)findViewById(R.id.btn_coffee);
		iv.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
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
		  
		  if(SettingUtility.isLightsOn()){
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
}
