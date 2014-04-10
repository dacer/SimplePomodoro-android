package dacer.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import dacer.service.WakeLockService;
import dacer.settinghelper.SettingUtility;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class MyUtils {
	public final static String CONTINUE_TIMES = "ContinueTimes";
	public final static String INTENT_IS_FULLSCREEN = "is_fullscreen";
	public final static String MY_PACKAGE_NAME = "com.dacer.simplepomodoro";
	public enum ScreenState{
		MYAPP,OTHERAPP,LOCK
		}

	public static void controlNetwork(boolean enable, Context paramContext){
		
		//control mobile network
		if(SettingUtility.isMobileNetworkEnabled()){
			try {
		        ConnectivityManager connectivityManager = (ConnectivityManager) paramContext.getSystemService("connectivity");
		        Method setMobileDataEnabledMethod = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		        setMobileDataEnabledMethod.setAccessible(true);
		        setMobileDataEnabledMethod.invoke(connectivityManager, enable);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		
		//control wifi network
		if(SettingUtility.isWifiEnabled()){
			WifiManager wifi=(WifiManager) paramContext.getSystemService(Context.WIFI_SERVICE);
			wifi.setWifiEnabled(enable);
		}
	}
	
	/**
	 * @return null if unconfirmed
	 */
	public static Boolean isMobileDataEnabled(Context paramContext){
	    Object connectivityService = paramContext.getSystemService("connectivity"); 
	    ConnectivityManager cm = (ConnectivityManager) connectivityService;

	    try {
	        Class<?> c = Class.forName(cm.getClass().getName());
	        Method m = c.getDeclaredMethod("getMobileDataEnabled");
	        m.setAccessible(true);
	        return (Boolean)m.invoke(cm);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static boolean isWifiEnabled(Context c){
		WifiManager wifi = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		return wifi.isWifiEnabled();
	}
	
	public static ScreenState getScreenState(Context c){
		PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
		boolean isScreenOn = pm.isScreenOn();
		if(isScreenOn){
			ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
			RunningTaskInfo info = runningTaskInfos.get(0);
			String nowPackageName = info.baseActivity.getPackageName();
			if(nowPackageName.equals(MY_PACKAGE_NAME)){
				return ScreenState.MYAPP;
			}else{
				return ScreenState.OTHERAPP;
			}
//			Log.e("MyUtils",nowPackageName);
		}else{
//			Log.e("isScreenOn:",String.valueOf(isScreenOn));
			return ScreenState.LOCK;
		}
	}
	
	public static Boolean isInCir(float x, float y,float screenCenterX,
			float screenCenterY,float radius){
    	float trueX = x - screenCenterX;
        float trueY = y - screenCenterY;
        if((trueX * trueX + trueY*trueY) < radius * radius){
        	return true;
        }else{
        	return false;
        }
    }
	
	public static Boolean isOnCirBorder(float x, float y,float screenCenterX,
			float screenCenterY,float radius,Context c){
		float offset = dipToPixels(c, 10);
    	float trueX = x - screenCenterX;
        float trueY = y - screenCenterY;
        if((trueX * trueX + trueY*trueY) < (radius+offset) * (radius+offset) &&
        		(trueX * trueX + trueY*trueY) > (radius-offset) * (radius-offset)){
        	return true;
        }else{
        	return false;
        }
    }
	
	public static int getScreenWidth(){
		Activity mActivity = GlobalContext.getActivity();
		DisplayMetrics metrics = new DisplayMetrics();   
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);   
		int width = metrics.widthPixels;
		return width;
	}
	
	public static int getScreenHeight(){
		Activity mActivity = GlobalContext.getActivity();
		DisplayMetrics metrics = new DisplayMetrics();   
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);   
		int height = metrics.heightPixels; 
		//reduce statusBar height
		height -= getStatusBarHeight(mActivity);
		return height;
	}
	
	public static float getInchWidth(){
		Activity mActivity = GlobalContext.getActivity();
		DisplayMetrics dm = new DisplayMetrics();   
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		float inchWidth = dm.widthPixels/dm.xdpi;
		return inchWidth;
	}
	
	public static float getInchHeight(){
		Activity mActivity = GlobalContext.getActivity();
		DisplayMetrics dm = new DisplayMetrics();   
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		float inchHeight = dm.heightPixels/dm.ydpi;
		return inchHeight;
	}
//	qu ta ma de android different screen!!!
//	
//	public static float getXdpi(){
//		Activity mActivity = GlobalContext.getActivity();
//		DisplayMetrics dm = new DisplayMetrics();   
//		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//		return dm.xdpi;
//	}
//	public static float getYdpi(){
//		Activity mActivity = GlobalContext.getActivity();
//		DisplayMetrics dm = new DisplayMetrics();   
//		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//		return dm.ydpi;
//	}
//				
	public static float getBigCirRadius(Activity mActivity){
		float bigCirRadius;
		int width = getScreenWidth();
		int height = getScreenHeight();
		if(width < height){
			bigCirRadius = (float) (width * 0.4);
		}else{
			bigCirRadius = (float) (height * 0.4);
		}
		return bigCirRadius;
	}
	
	public static int getStatusBarHeight(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Boolean isFullScreen = sp.getBoolean("pref_enable_fullscreen", false);
		if(isFullScreen){
			return 0;
		}
	      Class<?> c = null;
	      Object obj = null;
	      Field field = null;
	      int x = 0, sbar = 0;
	try {
	        c = Class.forName("com.android.internal.R$dimen");
	        obj = c.newInstance();
	        field = c.getField("status_bar_height");
	        x = Integer.parseInt(field.get(obj).toString());
	        sbar = context.getResources().getDimensionPixelSize(x);
//	        Log.e("sbar", String.valueOf(sbar));
	} catch (Exception e1) {
//	        loge("get status bar height fail");
	        e1.printStackTrace();
	}  

		return sbar;
	}
	

	public static float dipToPixels(Context context, float dipValue) {
	    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
	}
	
	public static float spToPixels(Context context, Float sp) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return sp*scaledDensity;
	}
	
	public static float pixelsToSp(Context context, Float px) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return px/scaledDensity;
	}
	//record for longer break
	
	public static void addContinueTimes(Context c){
		SharedPreferences sp = c.getSharedPreferences("com.dacer.simplepomodoro_preferences", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		int nowTimes = sp.getInt(CONTINUE_TIMES, 0);
		nowTimes++;
		editor.putInt(CONTINUE_TIMES, nowTimes);
		editor.commit();
	}
	
	public static void deleteContinueTimes(Context c){
		SharedPreferences sp = c.getSharedPreferences("com.dacer.simplepomodoro_preferences", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		int nowTimes = 0;
		editor.putInt(CONTINUE_TIMES, nowTimes);
		editor.commit();
	}
	
	public static int getContinueTimes(Context c){
		SharedPreferences sp = c.getSharedPreferences("com.dacer.simplepomodoro_preferences", Context.MODE_PRIVATE);
		int nowTimes = sp.getInt(CONTINUE_TIMES, 0);
		return nowTimes;
	}
	
	//for longer break----------<
	
	public static int getAndroidSDKVersion() { 
		   int version; 
		   try { 
		     version = android.os.Build.VERSION.SDK_INT; 
		   } catch (NumberFormatException e) { 
//		     Log.e(e.toString());
			   version = 0;
		   } 
		   return version; 
		}
	
	public static Boolean isUnderHoneycomb(){
		if(getAndroidSDKVersion()<Build.VERSION_CODES.HONEYCOMB){
			return true;
		}else{
			return false;
		}
	} 
	
	public static String getAppVersion(){
		PackageInfo pInfo;
		String version = "Error";
		try {
			pInfo = GlobalContext.getActivity().
					getPackageManager().getPackageInfo(
							GlobalContext.getActivity().
							getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}
	
	public static long getCurrentUTCInMIlls(){
		//Current location time inMillis - timeZoneOffset = GMT time inMillis
		long utcTime = System.currentTimeMillis();
		return utcTime;
	}
	

	
	public static boolean isWakeLockServiceRunning() {
        ActivityManager manager = (ActivityManager)GlobalContext.getInstance().getSystemService(GlobalContext.getInstance().ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("dacer.service.WakeLockService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
	
	public static void autoStopWakelockService(Context c){
		if(isWakeLockServiceRunning()){
			c.stopService(new Intent(c, WakeLockService.class));
		}
	}
	
	
	public static boolean isMiPhone(){
		String[] mi = {"MI","2013022"};
		for(int i=0; i<2; i++){
			if(getDeviceName().toLowerCase().contains(mi[i].toLowerCase())){
				return true;
			}
		}
		
		return false;
	}
	
	public static String getDeviceName() {
		  String manufacturer = Build.MANUFACTURER;
		  String model = Build.MODEL;
		  if (model.startsWith(manufacturer)) {
		    return model;
		  } else {
		    return manufacturer + " " + model;
		  }
		}

		public static String getDeviceVersion(){
        return Build.VERSION.RELEASE+" "+Build.VERSION.INCREMENTAL+" "+Build.VERSION.CODENAME+" "+Build.VERSION.SDK_INT;
    }
}
