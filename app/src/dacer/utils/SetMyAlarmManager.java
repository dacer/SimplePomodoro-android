package dacer.utils;

import java.util.Calendar;

import dacer.settinghelper.SettingUtility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class SetMyAlarmManager {
	
	
	//Set a alarm to start service at the time after "min" minute.
	public static void schedulService(Context mContext,int min, Class<?> cls){  
		PendingIntent mAlarmSender = PendingIntent.getService(mContext,  
                0, new Intent(mContext, cls), 0);  
        final Calendar c = Calendar.getInstance();
		int nowMin= c.get(Calendar.MINUTE);	

		Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.MINUTE, nowMin+min);
        // Schedule the alarm!  
        AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);  
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mAlarmSender);
		
        SettingUtility.setFinishTimeInMills(MyUtils.getCurrentGMTTimeInMIlls()+min*60000);
		
    }  
	
	public static void stopschedulService(Context mContext, Class<?> cls){  
	        PendingIntent mAlarmSender = PendingIntent.getService(mContext,  
	                0, new Intent(mContext, cls), 0);  
	        // cancel the alarm.  
	        AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);  
	        am.cancel(mAlarmSender);  
	        SettingUtility.setRunningType(SettingUtility.NONE_RUNNING);
	        SettingUtility.setFinishTimeInMills(0);
	    }  
}
