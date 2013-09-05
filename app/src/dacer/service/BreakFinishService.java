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

import com.dacer.simplepomodoro.BreakActivity;
import com.dacer.simplepomodoro.BreakFinishActivity;
import com.dacer.simplepomodoro.R;

import dacer.utils.MyNotification;
import dacer.utils.MyUtils;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class BreakFinishService extends Service {
	
	public Handler mHandler=new Handler()  
    {  
        @Override
		public void handleMessage(Message msg)  
        {  
            switch(msg.what)  
            {  
            case 1:  
            	//notification
      	        MyNotification mn = new MyNotification(BreakFinishService.this);
      	        mn.showSimpleNotification(getString(R.string.break_time_up),
      	        		getString(R.string.tap_to_continue_your_pomodoro), false,
      	        		BreakFinishActivity.class);
      	        
      	        initiService();
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
		MyUtils.ScreenState screenState = MyUtils.getScreenState(this);
		Intent intent = new Intent(BreakFinishService.this,BreakFinishActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		switch (screenState) {
		case LOCK:
			//prevent
			//if BreakActivity behind the lockScreen, the screen will not be waked up
//			Log.e("FinishService","LOCKing");
			startActivity(intent);
			break;
		case MYAPP:
			startActivity(intent);
			break;
		case OTHERAPP:
			showAlertDialog();
			break;
		default:
			break;
		}
		BreakFinishService.this.stopSelf();
	}
	@Override  
    public void onDestroy() {  
        super.onDestroy();  
    }  
    
	private void showAlertDialog(){
		AlertDialog d = new AlertDialog.Builder(this)
        .setTitle(getString(R.string.break_time_up))
        .setMessage(getString(R.string.now_you_can_continue_your_pomodoro))
        .setPositiveButton(R.string.continue_pomodoro, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BreakFinishService.this,BreakFinishActivity.class);
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
