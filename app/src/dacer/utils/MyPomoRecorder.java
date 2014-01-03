package dacer.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import dacer.settinghelper.SettingUtility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class MyPomoRecorder {
	private final String TAG = "MyRocorder"; 
	private SQLiteDatabase db;
	private final ContentValues cv = new ContentValues();
	private Context mContext;
	private SQLHelper mSQLHelper;
	public static String KEY_ID = "_id";
	public static String KEY_TITLE = "title";
	public static String KEY_NOTES = "notes";
	public static String KEY_STARTTIME = "start_time";
	public static String KEY_FINISHTIME = "finish_time";
	public static String KEY_TYPE = "type";
	public static final String RECORDER_TABLE_NAME = "recorder";
	public enum PomoType{
		POMODORO,BREAK
	}
	
	public MyPomoRecorder(Context c) {
		mContext = c;
		mSQLHelper = new SQLHelper(mContext);
	}
	
	public void putPomodoro(String title, String notes,PomoType mType,int durationMin){
		db = mSQLHelper.getWritableDatabase();
		cv.put(KEY_TITLE, title);
		cv.put(KEY_NOTES, notes);
		long nowTime = MyUtils.getCurrentUTCInMIlls()/1000;
		cv.put(KEY_STARTTIME, nowTime - durationMin * 60);
		cv.put(KEY_FINISHTIME, nowTime);
		cv.put(KEY_TYPE, mType.equals(PomoType.POMODORO)? 0:1);
		long res = db.insert(RECORDER_TABLE_NAME, null, cv);
		// res = -1:failed
		// res = 0 :success
	}
	
	public void putPomodoro(String title, String notes,PomoType mType,long startTime, long finishTime){
		db = mSQLHelper.getWritableDatabase();
		cv.put(KEY_TITLE, title);
		cv.put(KEY_NOTES, notes);
		cv.put(KEY_STARTTIME, startTime);
		cv.put(KEY_FINISHTIME, finishTime);
		cv.put(KEY_TYPE, mType.equals(PomoType.POMODORO)? 0:1);
		long res = db.insert(RECORDER_TABLE_NAME, null, cv);
		// res = -1:failed
		// res = 0 :success
	}
	
	public int getTodayPomo(){
		db = mSQLHelper.getReadableDatabase();
		Cursor cursor;
        long todayZeroClockTimeInMillis = getTodayZeroOclockInUTCMills()/1000;
        String selection = KEY_STARTTIME + ">=?";
		String[] selectionArgs = { 
				String.valueOf(todayZeroClockTimeInMillis) };
		cursor = db.query(RECORDER_TABLE_NAME, null, selection,
				selectionArgs, null, null, null);
		int result = cursor.getCount();
//		cursor.close();
		return result;
	}
	
	public ArrayList<Integer> getWeekend(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(6);
		if(SettingUtility.firstDaySun()){
			result.add(0);
		}else{
			result.add(5);
		}
		return result;
	}
	
	public ArrayList<Integer> getWeekNumReachGoal(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i=0; i<7; i++){
			if(getPomoOfThisWeek()[i]>=SettingUtility.getDailyGoal()){
				result.add(i);
			}
		}
		return result;
	}
	
	public int[] getPomoOfThisWeek(){
		int[] result = new int[7];
		db = mSQLHelper.getReadableDatabase();
		Cursor cursor;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
        int theDayOfWeekBeginWithZero;
        if(SettingUtility.firstDaySun()){
        	theDayOfWeekBeginWithZero = cal.get(Calendar.DAY_OF_WEEK)-Calendar.SUNDAY;
        }else{
        	if(cal.get(Calendar.DAY_OF_WEEK) == 1){
        		theDayOfWeekBeginWithZero = 6;
        	}else{
        		theDayOfWeekBeginWithZero = cal.get(Calendar.DAY_OF_WEEK)-Calendar.MONDAY;
        	}
        	
        }
//        fill the week record
        int temp = theDayOfWeekBeginWithZero;
        while(temp<6){
        	result[temp] = 0;
        	temp++;
        }
        while(theDayOfWeekBeginWithZero>=0){
        	long todayZeroClockTimeInMillis = cal.getTimeInMillis();
    		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+1);
            long tomorrowZeroTimeInMillis = cal.getTimeInMillis();
            
            String selection = KEY_STARTTIME + " BETWEEN ? AND ? ";
//            todayZeroClockTimeInMillis -= TimeZone.getDefault().getRawOffset(); 
//            tomorrowZeroTimeInMillis -= TimeZone.getDefault().getRawOffset(); 
    		String[] selectionArgs = { 
    				String.valueOf(todayZeroClockTimeInMillis/1000),
    				String.valueOf(tomorrowZeroTimeInMillis/1000)};
    		
    		cursor = db.query(RECORDER_TABLE_NAME, null, selection,
    				selectionArgs, null, null, null);
    		result[theDayOfWeekBeginWithZero] = cursor.getCount();
    		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)-2);
    		theDayOfWeekBeginWithZero--;
//    		cursor.close();
        }	
		return result;
	}
	
	public int getTotalPomo(){
		db = mSQLHelper.getReadableDatabase();
		Cursor cursor;
		cursor = db.query(RECORDER_TABLE_NAME, null, null,
				null, null, null, null);
		return cursor.getCount();
	}

	
	
	
	private long getTodayZeroOclockInUTCMills(){
		//Current location time inMillis - timeZoneOffset = GMT time inMillis
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
//		long unixTimeGMT = calendar.getTimeInMillis() - TimeZone.getDefault().getRawOffset();

		return calendar.getTimeInMillis();
	}
	
	
	private class SQLHelper extends SQLiteOpenHelper {
		
		private static final int DATABASE_VERSION = 2;
		private static final String DATABASE_NAME = "recorder.db";
		private SQLiteDatabase mDatabase;
		
		private final String TODOLIST_TABLE_CREATE = "CREATE TABLE "
				+ RECORDER_TABLE_NAME + " (" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TITLE + " TEXT, "
				 + KEY_NOTES + " TEXT, "+ KEY_STARTTIME + " NUMERIC, " 
				 + KEY_FINISHTIME + " NUMERIC, " + KEY_TYPE + " NUMERIC);";
		
		public SQLHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// mContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			mDatabase = db;
			mDatabase.execSQL(TODOLIST_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
//			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
//					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + RECORDER_TABLE_NAME);
			onCreate(db);
		}

	}
}
