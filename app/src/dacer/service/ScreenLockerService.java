package dacer.service;

import dacer.utils.LockScreenReeiver;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * Author:dacer
 * Date  :Mar 8, 2014
 */
public class ScreenLockerService extends Service{
	LockScreenReeiver receive;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		IntentFilter filter = new IntentFilter();
    	filter.addAction(Intent.ACTION_SCREEN_ON);
    	filter.addAction(Intent.ACTION_SCREEN_OFF);
    	receive = new LockScreenReeiver();
    	registerReceiver(receive, filter);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(receive);
		super.onDestroy();
	}
}
