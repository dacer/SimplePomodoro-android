package dacer.utils;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.dacer.simplepomodoro.R;

public class MyScreenLocker {
	private DevicePolicyManager policyManager;  
	private ComponentName componentName;  
	private Activity mActivity;
	
	public MyScreenLocker(Activity a) {
		// TODO Auto-generated constructor stub
		mActivity = a;
        policyManager = (DevicePolicyManager) a.getSystemService(Context.DEVICE_POLICY_SERVICE);  
        componentName = new ComponentName(a, AdminReceiver.class);  
//        mylock();  
	}
	
    public void myLockNow(){
    	boolean active = policyManager.isAdminActive(componentName);
    	if(!active){
    		activeManage();
    		policyManager.lockNow();
    	}
        if (active) {
        	//repeat 20 times for lock!
        	handler.post(runnable);
        }
    }
    public Boolean isActivited(){
    	boolean active = policyManager.isAdminActive(componentName);
    	return active;
    }
    public void activeManage() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, mActivity.getString(R.string.screen_locker_warnning));
        mActivity.startActivityForResult(intent, 0);
    }
    
    public void unlock(){
    	PowerManager pm = (PowerManager)mActivity.getSystemService(Context.POWER_SERVICE);
    	WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
    	mWakelock.acquire();
    	mWakelock.release();
    }
    public void removeManage(){
    	policyManager.removeActiveAdmin(componentName);
    }
    
    private Handler handler = new Handler( );

    private Runnable runnable = new Runnable( ) {
    	int i = 0;
    	
    	@Override
		public void run ( ) {
    		if(i<12){
    			policyManager.lockNow();
        		handler.postDelayed(this,50); 
        		i++;
    		}
    	}
    };

   
}
