package dacer.utils;

import java.lang.reflect.Field;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;


public class MyUtils {
	public final static String CONTINUE_TIMES = "ContinueTimes";
	public final static String INTENT_IS_FULLSCREEN = "is_fullscreen";
	public final static String MY_PACKAGE_NAME = "com.dacer.simplepomodoro";
	public enum ScreenState{
		MYAPP,OTHERAPP,LOCK
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
	
	public static int getScreenWidth(Activity mActivity){
		DisplayMetrics metrics = new DisplayMetrics();   
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);   
		int width = metrics.widthPixels;   
		return width;
	}
	
	public static int getScreenHeight(Activity mActivity){
		DisplayMetrics metrics = new DisplayMetrics();   
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);   
		int height = metrics.heightPixels; 
		//reduce statusBar height
		height -= getStatusBarHeight(mActivity);
		return height;
	}
	
	public static float getBigCirRadius(Activity mActivity){
		float bigCirRadius;
		int width = getScreenWidth(mActivity);
		int height = getScreenHeight(mActivity);
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
}
