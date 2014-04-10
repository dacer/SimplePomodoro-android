package dacer.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import com.dacer.simplepomodoro.FinishScreenActivity;
import com.dacer.simplepomodoro.R;

import dacer.settinghelper.SettingUtility;
import dacer.utils.MyNotification;
import dacer.utils.MyPomoRecorder;
import dacer.utils.MyUtils;
import dacer.utils.MyPomoRecorder.PomoType;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 * 在运行番茄时判断wifi mobile 网络是否开启，结束时智能的关闭
 */
public class CDService extends Service {

	
	public Handler mHandler=new Handler()  
    {  
        @Override
		public void handleMessage(Message msg)  
        {  
            switch(msg.what)  
            {  
            case 1:  
            	MyUtils.autoStopWakelockService(CDService.this);
            	initiService();
            	//add pomo record
      		  	MyPomoRecorder pRecorder = new MyPomoRecorder(CDService.this);
      		  	pRecorder.putPomodoro("title", "notes", 
      				  PomoType.POMODORO, SettingUtility.getPomodoroDuration());
      		  	//for long break
      		  	MyUtils.addContinueTimes(CDService.this);
      		  	//notification
      	        MyNotification mn = new MyNotification(CDService.this);
      	        mn.showSimpleNotification(getString(R.string.pomodoro_time_up),
      	        		getString(R.string.tap_to_break), false,
      	        		FinishScreenActivity.class);
                break;  
            default:  
                break;        
            }  
            super.handleMessage(msg);  
        }  
    };  

  @Override
  public void onCreate() {
	  Thread thread=new Thread(new Runnable()  
        {  
            @Override  
            public void run()  
            {  
                // TODO Auto-generated method stub  
                Message message=new Message();  
                message.what=1;  
                mHandler.sendMessage(message);  
                 
            }  
        });  
        thread.start();  
     
  }
  
  
  private void initiService(){
	  SettingUtility.setRunningType(SettingUtility.POMO_FINISHED);
	  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String strRingtonePreference = sp.getString("pref_notification_sound", "");
		if(!strRingtonePreference.equals("")){
			Uri ringtoneUri = Uri.parse(strRingtonePreference);
			Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
			ringtone.play();
//			String name = ringtone.getTitle(context);
		}
		
		
		Boolean isVibrator = sp.getBoolean("pref_enable_vibrations", false);
		if(isVibrator){
			Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			mVibrator.vibrate(new long[]{50,100,50,100}, -1);
		}
		
		if(SettingUtility.isSilentMode()){
			MyUtils.controlNetwork(true, getApplicationContext());
		}
		
		MyUtils.ScreenState screenState = MyUtils.getScreenState(this);
		Intent intent = new Intent(CDService.this,FinishScreenActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(MyUtils.INTENT_IS_FULLSCREEN, false);
		switch (screenState) {
		case LOCK:
//			intent.putExtra(MyUtils.INTENT_IS_FULLSCREEN, true);
			startActivity(intent);
			break;
		case MYAPP:
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			break;
		case OTHERAPP:
			showAlertDialog();
			break;
		default:
			break;
		}
		CDService.this.stopSelf();
	  
  }
	private void showAlertDialog(){
		AlertDialog d = new AlertDialog.Builder(this)
        .setTitle(getString(R.string.pomodoro_time_up))
        .setMessage(getString(R.string.now_you_can_take_a_break))
        .setPositiveButton(R.string.take_a_break, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CDService.this,FinishScreenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
				
			}
		})
        .setNegativeButton(R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
      	.create();
		d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		d.show();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
