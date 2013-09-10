package dacer.google.task;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author:dacer
 * Date  :Sep 10, 2013
 */

public class TaskRecorder {
	private SQLiteDatabase db;
	private final ContentValues cv = new ContentValues();
	private Context mContext;
	private SQLHelper mSQLHelper;
	public static String KEY_ID = "_id";
	public static String KEY_TITLE = "title";
	public static String KEY_NOTES = "notes";
	public static String KEY_GOOGLE_TASK_IDENTIFIER = "google_task_identifier";
	public static String KEY_COMPLETED = "completed";//0 => false 1=> true
	public static final String RECORDER_TABLE_NAME = "tasks";
	
	public TaskRecorder(Context c) {
		mContext = c;
		mSQLHelper = new SQLHelper(mContext);
	}
	
	public void putTask(String title, String notes){
		db = mSQLHelper.getWritableDatabase();
		cv.put(KEY_TITLE, title);
		cv.put(KEY_NOTES, notes);
		cv.put(KEY_COMPLETED, 0);
		long res = db.insert(RECORDER_TABLE_NAME, null, cv);
		// res = -1:failed
		// res = 0 :success
	}
	
	public void setTaskStatus(boolean completed, int id){
		
	}
	
	public List<String> getAllTasks(){
		db = mSQLHelper.getReadableDatabase();
		Cursor cursor;
		cursor = db.query(RECORDER_TABLE_NAME, null, null,
					null, null, null, null);
		int idIndex = cursor.getColumnIndex(KEY_TITLE);
		List<String> mTitles = new ArrayList<String>();
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			String tempS = cursor.getString(idIndex);
			String s = tempS;
			mTitles.add(s);
		}
		return mTitles;
	}
	
	public int deleteTask(int db_id) {
		db = mSQLHelper.getWritableDatabase();
		String[] args = { String.valueOf(db_id) };
		return db.delete(RECORDER_TABLE_NAME, KEY_ID
				+ "=?", args);
	}
	
	public void deleteAllTask(){
		db = mSQLHelper.getWritableDatabase();
		db.execSQL("DELETE FROM "+ RECORDER_TABLE_NAME);
		db.execSQL("DELETE FROM sqlite_sequence WHERE name='"+ RECORDER_TABLE_NAME+"'");		
	}
	
	private class SQLHelper extends SQLiteOpenHelper {
		
		private static final int DATABASE_VERSION = 2;
		private static final String DATABASE_NAME = "tasks.db";
		private SQLiteDatabase mDatabase;
		
		private final String TODOLIST_TABLE_CREATE = "CREATE TABLE "
				+ RECORDER_TABLE_NAME + " (" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TITLE + " TEXT, "
				+ KEY_NOTES + " TEXT, "+ KEY_GOOGLE_TASK_IDENTIFIER + " TEXT, " 
				+ KEY_COMPLETED + " NUMERIC);";
		
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
