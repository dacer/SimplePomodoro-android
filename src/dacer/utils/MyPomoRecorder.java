package dacer.utils;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyPomoRecorder {
	private TimeZone tz;  // ALL the date convert to GMT timeZone!
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
		tz = TimeZone.getTimeZone("GMT");
	}
	
	public void putPomodoro(String title, String notes,PomoType mType,int durationMin){
		db = mSQLHelper.getWritableDatabase();
		cv.put(KEY_TITLE, title);
		cv.put(KEY_NOTES, notes);
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(tz);
		long nowTime = cal.getTimeInMillis()/1000;
//		Log.e(TAG+"nowTime:",String.valueOf(nowTime));
		cv.put(KEY_STARTTIME, nowTime - durationMin * 60);
		cv.put(KEY_FINISHTIME, nowTime);
		cv.put(KEY_TYPE, mType.equals(PomoType.POMODORO)? 0:1);
		
		long res = db.insert(RECORDER_TABLE_NAME, null, cv);// ²åÈëÊý¾Ý
		// res = -1:failed
		// res = 0:success
	}
	
	public int getTodayPomo(){
		db = mSQLHelper.getReadableDatabase();
		Cursor cursor;
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(tz);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long todayZeroClockTimeInMillis = cal.getTimeInMillis()/1000;
        String selection = KEY_STARTTIME + ">=?";
		String[] selectionArgs = { 
				String.valueOf(todayZeroClockTimeInMillis) };
		cursor = db.query(RECORDER_TABLE_NAME, null, selection,
				selectionArgs, null, null, null);
		return cursor.getCount();
	}
	
	
	
	public int getTotalPomo(){
		db = mSQLHelper.getReadableDatabase();
		Cursor cursor;
		cursor = db.query(RECORDER_TABLE_NAME, null, null,
				null, null, null, null);
		return cursor.getCount();
	}
	
//	public String getToday(){
//		
//	}

	public Boolean caidan(){
		// if you finished 3 Pompdoros since 3 days ago.
		//there will be no ADs and return true
		db = mSQLHelper.getReadableDatabase();
		Cursor cursor;
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(tz);
		int day  = cal.get(Calendar.DAY_OF_YEAR);
		cal.set(Calendar.DAY_OF_YEAR, day - 3);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long threedaysAgoTimeInMillis = cal.getTimeInMillis()/1000;
        
        String selection = KEY_STARTTIME + ">=?";
		String[] selectionArgs = { 
				String.valueOf(threedaysAgoTimeInMillis) };
		cursor = db.query(RECORDER_TABLE_NAME, null, selection,
				selectionArgs, null, null, null);
		int num = cursor.getCount();
		Log.e("caidan", String.valueOf(num));
		if(num>=3){
			return true;
		}else{
			return false;
		}
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
