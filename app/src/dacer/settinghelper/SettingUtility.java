package dacer.settinghelper;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.dacer.simplepomodoro.R;

import dacer.utils.GlobalContext;

/**
 * Author:qii
 * Date  :Jul 17, 2013
 */
public class SettingUtility {
	public static final int LONG_BREAK_DURATION = 25;
	
	private static final String POMODORO_DURATION = "pref_pomodoro_duration";
	private static final String THEME_TYPE = "pref_theme_type";
	private static final String ENABLE_VIBRATIONS = "pref_enable_vibrations";
	private static final String	ENABLE_FASTMODE = "pref_fast_mode";
	private static final String ENABLE_FULLSCREEN = "pref_enable_fullscreen";
	private static final String ENABLE_LIGHTS_ON = "pref_lights_on";
	private static final String BREAK_DURATION = "pref_break_duration";
	private static final String FIRST_DAY = "pref_first_day";
	private static final String DAILY_GOAL = "pref_daily_goal";
	
	private static final String AUTH_TOKEN = "auth_token";
	private static final String AUTH_ACCOUNT_NAME = "auth_account_name";
	private static final String DEBUG_MODE = "debug_mode";
	private static final String PRE_ADREMOVED = "adremoved";
    private SettingUtility() {

    }

    private static Context getContext() {
        return GlobalContext.getInstance();
    }
    
    public static boolean isAuthorized(){
    	return getAuthToken()=="0"? false:true;
    }
    
    public static String getAccountName(){
    	String name = SettingHelper.getSharedPreferences(getContext(), AUTH_ACCOUNT_NAME, "0");
    	return name;
    }
    public static void setAccountName(String name){
    	SettingHelper.setEditor(getContext(), AUTH_ACCOUNT_NAME, name);
    }
    
    public static String getAuthToken(){
    	String token = SettingHelper.getSharedPreferences(getContext(), AUTH_TOKEN, "0");
    	return token;
    }
    
    public static void setAuthToken(String token){
    	SettingHelper.setEditor(getContext(), AUTH_TOKEN, token);
    }
    
    public static int getPomodoroDuration(){
    	int value = Integer.parseInt(SettingHelper.getSharedPreferences(getContext(), POMODORO_DURATION, "25"));
    	if(SettingHelper.getSharedPreferences(getContext(), DEBUG_MODE, false)){
    		value = 1;
    	}
    	return value;
    }
    
    public static int getBreakDuration(){
		int value = Integer.parseInt(SettingHelper.getSharedPreferences(getContext(), BREAK_DURATION, "5"));
		if(SettingHelper.getSharedPreferences(getContext(), DEBUG_MODE, false)){
    		value = 1;
    	}
		return value;
	}
    
    public static void removeAD(boolean remove){
    	SettingHelper.setEditor(getContext(), PRE_ADREMOVED, remove);
    }
    
    public static boolean isADRemoved(){
    	 boolean value = SettingHelper.getSharedPreferences(getContext(), PRE_ADREMOVED, false);
         return value;
    }
    public static boolean firstDaySun(){
        boolean value = SettingHelper.getSharedPreferences(getContext(), FIRST_DAY, "sunday").equals("sunday");
        return value;
    }
    
    public static int getDailyGoal(){
    	int value = 6;
    	try {
    		value = Integer.parseInt(SettingHelper.getSharedPreferences(getContext(), DAILY_GOAL, "6"));
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getContext(), R.string.daily_goal_error, Toast.LENGTH_LONG).show();
		}
    	return value;
    }
    
    
    
	public static boolean isFastMode(){
        boolean value = SettingHelper.getSharedPreferences(getContext(), ENABLE_FASTMODE, false);
        return value;
    }
	
	public static boolean isFullScreen(){
        boolean value = SettingHelper.getSharedPreferences(getContext(), ENABLE_FULLSCREEN, false);
        return value;
    }
	
	public static boolean isLightsOn(){
        boolean value = SettingHelper.getSharedPreferences(getContext(), ENABLE_LIGHTS_ON, false);
        return value;
    }
	
	
	
    public static boolean isLightTheme(){
        boolean value = SettingHelper.getSharedPreferences(getContext(), THEME_TYPE, "black").equals("white");
        return value;
    }
    
    public static boolean isVibrator() {
        boolean value = SettingHelper.getSharedPreferences(getContext(), ENABLE_VIBRATIONS, false);
        return value;
    }
    
    public static int getThemeWhite(){
    	return isFullScreen() ? R.style.MyFullScreenTheme_White : R.style.MyMainScreenTheme_White;
    }
    
    public static int getTheme(){
    	if(isLightTheme()){
			return getThemeWhite();
		}else{
			return isFullScreen() ? R.style.MyFullScreenTheme_Black : R.style.MyMainScreenTheme_Black;
		}
    
    }
    
    public static int getBackgroundColor(){
    	if (isLightTheme()){
    		return Color.WHITE;
    	}else {
    		return Color.BLACK;
    	}
    }
    
    public static int getBigCirColor(){
    	if (isLightTheme()){
    		return Color.parseColor("#FF4444");
    	}else {
    		return Color.parseColor("#CC0000");
    	}
    }
    
    public static int[] getWaitShaderColor(){
    	if (isLightTheme()){
    		return new int[] { 
        			Color.parseColor("#FFBB33"),
    				Color.parseColor("#FF4444"),
    				Color.parseColor("#FF4444"),
    				Color.parseColor("#FF4444"),
    				Color.parseColor("#FF4444"),
    				Color.parseColor("#FF4444"),
    				Color.parseColor("#FFBB33") };
    	}else {
    		return new int[] { 
        			Color.parseColor("#90d7ec"),
    				Color.parseColor("#145b7d"),
    				Color.parseColor("#145b7d"),
    				Color.parseColor("#145b7d"),
    				Color.parseColor("#145b7d"),
    				Color.parseColor("#145b7d"),
    				Color.parseColor("#90d7ec") };
    	}
    }
    
    public static int getButtonTextColor(){
    	return Color.parseColor(isLightTheme() ? "#FF4444":"#145b7d");
    }
}
