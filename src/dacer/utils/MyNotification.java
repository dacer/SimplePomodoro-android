package dacer.utils;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.dacer.simplepomodoro.R;


public class MyNotification {
	
	private Context mContext;
	public MyNotification(Context c) {
		// TODO Auto-generated constructor stub
		mContext = c;
	}
	
	public	void showSimpleNotification(String title,String text,Class<?> cls){
		
		int i = MyUtils.getContinueTimes(mContext);
		
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(mContext)
		        .setSmallIcon(R.drawable.ic_notification_icon)
		        .setContentTitle(title)
		        .setContentText(text)
		        .setAutoCancel(true)
		        .setContentInfo(String.valueOf(i));
		
		Intent resultIntent = new Intent(mContext,cls);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
		stackBuilder.addParentStack(cls);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(9527, mBuilder.build());
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
