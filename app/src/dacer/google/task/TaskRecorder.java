package dacer.google.task;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.api.client.util.DateTime;

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
	public static String KEY_UPDATE_TIME = "update_time";
	public static String KEY_DELETED = "deleted";
	public static String KEY_GOOGLE_TASK_IDENTIFIER = "google_task_identifier";
	public static String KEY_COMPLETED = "completed";//0 => false 1=> true
	public static final String TASKS_TABLE_NAME = "tasks";
	
	public TaskRecorder(Context c) {
		mContext = c;
		mSQLHelper = new SQLHelper(mContext);
	}
	
	public void putTask(String title, String notes, DateTime updateTime, String identifier,
			boolean deleted, boolean completed){
		db = mSQLHelper.getWritableDatabase();
		cv.put(KEY_TITLE, title);
		cv.put(KEY_NOTES, notes);
		cv.put(KEY_COMPLETED, completed? 1:0);
		cv.put(KEY_DELETED, deleted? 1:0);
		cv.put(KEY_UPDATE_TIME, updateTime.toStringRfc3339());
		cv.put(KEY_GOOGLE_TASK_IDENTIFIER, identifier);
		long res = db.insert(TASKS_TABLE_NAME, null, cv);
		// res = -1:failed
		// res = 0 :success
	}
	
	public boolean setUpdateTime(String time, String identifier){
		int id = getIdByIdentifier(identifier);
		db = mSQLHelper.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(KEY_UPDATE_TIME, time);
		Boolean resultBoolean = db.update(TASKS_TABLE_NAME,
				args, KEY_ID + "=" + id, null) > 0;	
		return resultBoolean;
	}
	
	public boolean setUpdateTimeByID(String time, int id){
		db = mSQLHelper.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(KEY_UPDATE_TIME, time);
		Boolean resultBoolean = db.update(TASKS_TABLE_NAME,
				args, KEY_ID + "=" + id, null) > 0;	
		return resultBoolean;
	}
	
	public boolean setTaskCompleted(boolean completed, String identifier){
		int id = getIdByIdentifier(identifier);
		db = mSQLHelper.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(KEY_COMPLETED, completed? 1:0);
		Boolean resultBoolean = db.update(TASKS_TABLE_NAME,
				args, KEY_ID + "=" + id, null) > 0;   //-----------------------
		return resultBoolean;
	}

	
	public boolean setTaskDeletedFlag(boolean deleted, String identifier){
		int id = getIdByIdentifier(identifier);
		db = mSQLHelper.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(KEY_DELETED, deleted? 1:0);
		Boolean resultBoolean = db.update(TASKS_TABLE_NAME,
				args, KEY_ID + "=" + id, null) > 0;
				
		return resultBoolean;
	}
	
	public boolean setTaskTitle(String title, String identifier){
		int id = getIdByIdentifier(identifier);
		db = mSQLHelper.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		Boolean resultBoolean = db.update(TASKS_TABLE_NAME,
				args, KEY_ID + "=" + id, null) > 0;
		
		return resultBoolean;
	}
	
	public boolean setTaskTitleByID(String title, int id){
		db = mSQLHelper.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		Boolean resultBoolean = db.update(TASKS_TABLE_NAME,
				args, KEY_ID + "=" + id, null) > 0;
				
		return resultBoolean;
	}
	
	public String getUpdateTime(String identifier){
		return getInfo(identifier, KEY_UPDATE_TIME);
	}
	public String getTaskIdentifier(String identifier){
		return getInfo(identifier, KEY_GOOGLE_TASK_IDENTIFIER);
	}
	public String getTaskTitle(String identifier){
		return getInfo(identifier, KEY_TITLE);
	}
	public boolean getTaskCompleted(String identifier){
		return getInfo(identifier, KEY_COMPLETED).equals("1");
	}
	public boolean getTaskDelete(String identifier){
		return getInfo(identifier, KEY_DELETED).equals("1");
	}
	
	
	public String getUpdateTimeByID(int id){
		return getInfoByID(id, KEY_UPDATE_TIME);
	}
	public String getTaskIdentifierByID(int id){
		return getInfoByID(id, KEY_GOOGLE_TASK_IDENTIFIER);
	}
	public String getTaskTitleByID(int id){
		return getInfoByID(id, KEY_TITLE);
	}
	public boolean getTaskCompletedByID(int id){
		return getInfoByID(id, KEY_COMPLETED).equals("1");
	}
	public boolean getTaskDeleteByID(int id){
		return getInfoByID(id, KEY_DELETED).equals("1");
	}
	
	
	public ArrayList<Integer> getAllId(){
		db = mSQLHelper.getReadableDatabase();
		Cursor cursor;
		cursor = db.query(TASKS_TABLE_NAME, null, null,
					null, null, null, null);

		int idIndex = cursor.getColumnIndex(KEY_ID);
		ArrayList<Integer> mPosition_id = new ArrayList<Integer>();
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			int tempInt = cursor.getInt(idIndex);
			Integer integer = tempInt;
			mPosition_id.add(integer);
		}
		return mPosition_id;
	}
	
	private String getInfo(String identifier, String KEY){
		db = mSQLHelper.getReadableDatabase();
		String selection = KEY_GOOGLE_TASK_IDENTIFIER + "=?";
		String[] selectionArgs = { identifier };
		Cursor cursor = db.query(TASKS_TABLE_NAME, null,
				selection, selectionArgs, null, null, null);
		int index = cursor.getColumnIndex(KEY);
		if(cursor.isNull(index)){
			return "0";
		}
		ArrayList<String> list = new ArrayList<String>();
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			if(KEY.equals(KEY_COMPLETED) || KEY.equals(KEY_DELETED)){
				list.add(String.valueOf(cursor.getInt(index)));
			}else{
				list.add(cursor.getString(index));
			}
		}
		
		return list.get(0);
	}
	
	private String getInfoByID(int id, String KEY){
		db = mSQLHelper.getReadableDatabase();
		String selection = KEY_ID + "=?";
		String[] selectionArgs = { String.valueOf(id) };
		Cursor cursor = db.query(TASKS_TABLE_NAME, null,
				selection, selectionArgs, null, null, null);
		int index = cursor.getColumnIndex(KEY);
		if(cursor.getCount() == 0){
			return "0";
		}
		ArrayList<String> list = new ArrayList<String>();
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			if(KEY.equals(KEY_COMPLETED) || KEY.equals(KEY_DELETED)){
				list.add(String.valueOf(cursor.getInt(index)));
			}else{
				list.add(cursor.getString(index));
			}
		}
		
		return list.get(0);
	}
	
	public boolean identifierExist(String identifier){
		db = mSQLHelper.getReadableDatabase();
		String selection = KEY_GOOGLE_TASK_IDENTIFIER + "=?";
		String[] selectionArgs = { identifier };
		Cursor cursor = db.query(TASKS_TABLE_NAME, null,
				selection, selectionArgs, null, null, null);
		if(cursor.getCount() == 0){
			return false;
		}else {
			return true;
		}
	}
	
	public int getIdByIdentifier(String identifier){
		db = mSQLHelper.getReadableDatabase();
		String selection = KEY_GOOGLE_TASK_IDENTIFIER + "=?";
		String[] selectionArgs = { identifier };
		Cursor cursor = db.query(TASKS_TABLE_NAME, null,
				selection, selectionArgs, null, null, null);
		int index = cursor.getColumnIndex(KEY_ID);
		if(cursor.getCount() == 0){
			return 0000;
		}else {
			cursor.moveToFirst(); 
			return cursor.getInt(index);
		}
	}
//	public List<String> getAllTasksTitle(){
//		db = mSQLHelper.getReadableDatabase();
//		Cursor cursor;
//		cursor = db.query(TASKS_TABLE_NAME, null, null,
//					null, null, null, null);
//		int idIndex = cursor.getColumnIndex(KEY_TITLE);
//		List<String> mTitles = new ArrayList<String>();
//		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
//			String tempS = cursor.getString(idIndex);
//			String s = tempS;
//			mTitles.add(s);
//		}
//		return mTitles;
//	}
	
	public void closeDB(){
		db.close();
	}
	private SQLiteDatabase getWritableDB(){
		SQLiteDatabase db;
		return db = mSQLHelper.getWritableDatabase();
	}
	
	private SQLiteDatabase getReadableDB(){
		SQLiteDatabase db;
		return db = mSQLHelper.getReadableDatabase();
	}
	
	public Cursor getCursor() {
		// Log.e("getCursor", "start");
		db = mSQLHelper.getReadableDatabase();
		Cursor cursor;
		cursor = db.query(TASKS_TABLE_NAME, null, null,
					null, null, null, null);
		return cursor;
	}
	
	public int deleteTaskTrue(String identifier) {
		db = mSQLHelper.getWritableDatabase();
		String[] args = { identifier };
		return db.delete(TASKS_TABLE_NAME, KEY_GOOGLE_TASK_IDENTIFIER
				+ "=?", args);
	}
	
	
	public void deleteList(){
		db = mSQLHelper.getWritableDatabase();
		db.execSQL("DELETE FROM "+ TASKS_TABLE_NAME);
		db.execSQL("DELETE FROM sqlite_sequence WHERE name='"+ TASKS_TABLE_NAME+"'");		
	}
	
	private class SQLHelper extends SQLiteOpenHelper {
		
		private static final int DATABASE_VERSION = 2;
		private static final String DATABASE_NAME = "tasks.db";
		private SQLiteDatabase mDatabase;
		
		private final String TODOLIST_TABLE_CREATE = "CREATE TABLE "
				+ TASKS_TABLE_NAME + " (" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TITLE + " TEXT, "
				+ KEY_NOTES + " TEXT, "+ KEY_GOOGLE_TASK_IDENTIFIER + " TEXT, " 
				+ KEY_UPDATE_TIME + " TEXT, "+ KEY_DELETED +" NUMERIC, "
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
			db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE_NAME);
			onCreate(db);
		}

	}
}
