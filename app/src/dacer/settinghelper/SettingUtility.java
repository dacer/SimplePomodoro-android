package dacer.settinghelper;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.dacer.simplepomodoro.R;
import com.google.api.services.tasks.Tasks;

import dacer.google.task.TaskWebUtils;
import dacer.utils.GlobalContext;
import dacer.utils.MyUtils;

/**
 * Author:qii
 * Date  :Jul 17, 2013
 */
public class SettingUtility {
	public static final String LONG_BREAK_DURATION = "pref_long_break_duration_new";
	
	private static final String FIRST_START = "pref_first_start";
	public static final String POMODORO_DURATION = "pref_pomodoro_duration_new";
	private static final String THEME_TYPE = "pref_theme_type";
	private static final String ENABLE_VIBRATIONS = "pref_enable_vibrations";
	private static final String	ENABLE_FASTMODE = "pref_fast_mode";
	private static final String ENABLE_FULLSCREEN = "pref_enable_fullscreen";
	private static final String ENABLE_LIGHTS_ON = "pref_lights_on";
	public static final String BREAK_DURATION = "pref_break_duration_new";
	private static final String FIRST_DAY = "pref_first_day";
	private static final String DAILY_GOAL = "pref_daily_goal_new";
	private static final String ENABLE_GOOGLE_TASK = "pref_sync_with_google_task";

    private static final String ENABLE_TICKING = "pref_enable_ticking";
    private static final String IS_XIAOMI_MODE = "pref_is_xiaomi_mode";
	private static final String GOOGLE_TASK_API_LISTID = "list_id";
	private static final String AUTH_TOKEN = "auth_token";
	private static final String AUTH_ACCOUNT_NAME = "auth_account_name";
	private static final String DEBUG_MODE = "debug_mode";
	private static final String PRE_ADREMOVED = "adremoved";
	private static final String BIG_FONT = "big_font";
	
	//Anti be killed Test
	private static final String FINISH_TIME = "finish_time";
	private static final String NOW_RUNNING_TYPE = "now_running_type";
	public static final int NONE_RUNNING = 0;
	public static final int POMO_RUNNING = 1;
	public static final int BREAK_RUNNING = 2;
	
	
    private SettingUtility() {

    }

    private static Context getContext() {
        return GlobalContext.getInstance();
    }
    
//    public static void autoSetFinishTime(int runningType){
//    	long nowMills = MyUtils.getCurrentGMTTimeInMIlls();
//    	long finishMills = nowMills;
//    	switch (runningType) {
//		case POMO_RUNNING:
//			finishMills += getPomodoroDuration()*60000;
//			break;
//
//		case BREAK_RUNNING:
//			finishMills += getBreakDuration()*60000;
//			break;
//			
//		case NONE_RUNNING:
//			finishMills = 0;
//			break;
//		default:
//			break;
//		}
//    	setFinishTimeInMills(finishMills);
//    	setRunningType(runningType);
//    }

    public static int getLongBreakDuration(){
		int value = SettingHelper.getSharedPreferences(getContext(), LONG_BREAK_DURATION, 25);
        return value;
    }

    public static boolean isBigFont(){
        boolean type = SettingHelper.
                getSharedPreferences(getContext(), BIG_FONT, false);
        return type;
    }
    
    public static void setBigFont(boolean isBig){
        SettingHelper.setEditor(getContext(), BIG_FONT, isBig);
    }
    
    public static boolean isTick(){
        boolean type = SettingHelper.
                getSharedPreferences(getContext(), ENABLE_TICKING, false);
        return type;
    }

    public static void setTick(boolean isTick){
        SettingHelper.setEditor(getContext(), ENABLE_TICKING, isTick);
    }

    public static boolean isXiaomiMode(){
        boolean type = SettingHelper.
                getSharedPreferences(getContext(), IS_XIAOMI_MODE, false);
        return type;
    }

    public static void setXiaoMiMode(boolean a){

        SettingHelper.setEditor(getContext(), IS_XIAOMI_MODE, a);
    }
    
    public static long getFinishTimeInMills() {
    	long type = SettingHelper.
    			getSharedPreferences(getContext(), FINISH_TIME, 0L);
    	return type;
    }
    
    public static void setFinishTimeInMills(long inMill){
    	SettingHelper.setEditor(getContext(), FINISH_TIME, inMill);
    }
    
    public static int getRunningType() {
    	int type = SettingHelper.
    			getSharedPreferences(getContext(), NOW_RUNNING_TYPE, 0);
    	return type;
    }
    
    public static void setRunningType(int runningType) {
    	SettingHelper.setEditor(getContext(), NOW_RUNNING_TYPE, runningType);
    }
    
    public static boolean isFirstStart(){
   	 boolean value = SettingHelper.getSharedPreferences(getContext(), FIRST_START, true);
        return value;
    }
    
    public static void setFirstStart(boolean firstStart){
    	SettingHelper.setEditor(getContext(), FIRST_START, firstStart);
    }
    
    
    public static boolean enableGTask(){
    	boolean enable = SettingHelper.getSharedPreferences(getContext(), ENABLE_GOOGLE_TASK, false);
    	return enable;
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
    public static String getTaskListId(){
    	String name = SettingHelper.getSharedPreferences(getContext(), GOOGLE_TASK_API_LISTID, "0");
    	return name;
    }
    public static void setTaskListId(String id){
    	SettingHelper.setEditor(getContext(), GOOGLE_TASK_API_LISTID, id);
    }
    public static String getAuthToken(){
    	String token = SettingHelper.getSharedPreferences(getContext(), AUTH_TOKEN, "0");
    	return token;
    }
    
    public static void setAuthToken(String token){
    	SettingHelper.setEditor(getContext(), AUTH_TOKEN, token);
    }
    
    public static int getPomodoroDuration(){
    	int value = SettingHelper.getSharedPreferences(getContext(), POMODORO_DURATION, 25);
    	if(SettingHelper.getSharedPreferences(getContext(), DEBUG_MODE, false)){
    		value = 1;
    	}
    	return value;
    }
    
    public static int getBreakDuration(){
		int value = SettingHelper.getSharedPreferences(getContext(), BREAK_DURATION, 5);
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
    	int value = SettingHelper.getSharedPreferences(getContext(), DAILY_GOAL, 6);
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
