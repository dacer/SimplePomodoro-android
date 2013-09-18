package dacer.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.dacer.simplepomodoro.R;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class MyNotification {
	
	private Context mContext;
	public MyNotification(Context c) {
		// TODO Auto-generated constructor stub
		mContext = c;
	}
	
	public	void showSimpleNotification(String title,String text,boolean ongoing,Class<?> cls){
		
		int i = MyUtils.getContinueTimes(mContext);
		
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(mContext)
		        .setSmallIcon(R.drawable.ic_notification_icon)
//		        .setTicker(title)
		        .setOngoing(ongoing)
		        .setContentTitle(title)
		        .setContentText(text)
		        .setAutoCancel(true)
		        .setContentInfo(String.valueOf(i));
		
//		mBuilder.setLights(argb, onMs, offMs)
//		Intent resultIntent = new Intent(mContext,cls);
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
//		stackBuilder.addParentStack(cls);
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent =
//		        stackBuilder.getPendingIntent(
//		            0,
//		            PendingIntent.FLAG_UPDATE_CURRENT
//		        );
		Intent intent = new Intent(Intent.ACTION_MAIN); 
		intent.addCategory(Intent.CATEGORY_LAUNCHER); 
		intent.setClass(mContext, cls); 
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(9527, mBuilder.getNotification());
	}
	
	public void cancelNotification(){
		NotificationManager manager=(NotificationManager)mContext.
				getSystemService(Context.NOTIFICATION_SERVICE);
		try {
			manager.cancelAll();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
