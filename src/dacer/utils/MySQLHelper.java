package dacer.utils;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLHelper {
	
	private Context mContext;
	
	private static final String TAG = "MySQLHelper";
	

//	public static int BOX_ID_UNFINISHED = 0;
//	public static int BOX_ID_FINISHED = 1;
//	public static int BOX_ID_TOP = 2;
//	
//
//	
//
//	
//
//	public long addTodo(String title, String details) {
//		// Log.e("addTodo", "start");
//		db = mSQLHelper.getWritableDatabase();
//		cv.put(MySQLHelper.KEY_TITLE, title);
//		cv.put(MySQLHelper.KEY_DETAILS, details);
//		cv.put(MySQLHelper.KEY_BOXNUM, BOX_ID_UNFINISHED);
//		// cv.put(MySQLHelper.KEY_ID, id);
//
//		long res = db.insert(MySQLHelper.TODOLIST_TABLE_NAME, null, cv);// 插入数据
//		// res = -1:failed
//		// res = 0:success
//		return res;
//	}
//
//	public boolean updateTodo(String title, String details, int id) {
//		// Log.e("updateTodo", "start");
//		db = mSQLHelper.getWritableDatabase();
//		ContentValues args = new ContentValues();
//		args.put(MySQLHelper.KEY_TITLE, title);
//		args.put(MySQLHelper.KEY_DETAILS, details);
//		Boolean resultBoolean = db.update(MySQLHelper.TODOLIST_TABLE_NAME,
//				args, MySQLHelper.KEY_ID + "=" + id, null) > 0;
//		return resultBoolean;
//	}
//
//	public boolean putTodoInBox(int db_id, int box_id) {
//		// Log.e("finishTodo", "start");
//		Boolean resultBoolean;
//		db = mSQLHelper.getWritableDatabase();
//		if (box_id == BOX_ID_TOP) { // When want to put into a Top Box
//			int boxNum = getAllIdOfBox(BOX_ID_TOP, false).size();
//			if (boxNum == 1) { // when there had a TOP task
//				int nowDbId = getDBid(0, BOX_ID_TOP, mContext);
//				ContentValues args = new ContentValues();
//				args.put(MySQLHelper.KEY_BOXNUM, BOX_ID_UNFINISHED); // put the
//																		// origin
//																		// task
//																		// return
//																		// to 0
//																		// box
//				db.update(MySQLHelper.TODOLIST_TABLE_NAME, args,
//						MySQLHelper.KEY_ID + "=" + nowDbId, null);
//			} else if (boxNum == 0) {
//				// When there have not a task
//			}
//			ContentValues mArgs = new ContentValues();
//			mArgs.put(MySQLHelper.KEY_BOXNUM, BOX_ID_TOP); // put the current
//															// task to 2 box
//			resultBoolean = db.update(MySQLHelper.TODOLIST_TABLE_NAME, mArgs,
//					MySQLHelper.KEY_ID + "=" + db_id, null) > 0;
//		} else {
//			ContentValues args = new ContentValues();
//			args.put(MySQLHelper.KEY_BOXNUM, box_id);
//			resultBoolean = db.update(MySQLHelper.TODOLIST_TABLE_NAME, args,
//					MySQLHelper.KEY_ID + "=" + db_id, null) > 0;
//		}
//
//		return resultBoolean;
//	}
//
//	public Cursor getCursor(boolean isFinished) {
//		// Log.e("getCursor", "start");
//		db = mSQLHelper.getReadableDatabase();
//		String selection = MySQLHelper.KEY_BOXNUM + "=?";
//		Cursor cursor;
//		if (isFinished) {
//			String[] selectionArgs = { String.valueOf(BOX_ID_FINISHED) };
//			cursor = db.query(MySQLHelper.TODOLIST_TABLE_NAME, null, selection,
//					selectionArgs, null, null, null);
//			// Log.e("------------------>", cursor.getString(1));
//		} else {
//			String[] selectionArgs = { String.valueOf(BOX_ID_UNFINISHED) };
//			cursor = db.query(MySQLHelper.TODOLIST_TABLE_NAME, null, selection,
//					selectionArgs, null, null, null);
//		}
//		return cursor;
//	}
//
//	public ArrayList<Integer> getAllIdOfBox(int boxId, Boolean getAll) {
//		// Log.e("getAllId", "start");
//		db = mSQLHelper.getReadableDatabase();
//		Cursor cursor;
//
//		if (getAll) {
//			cursor = db.query(MySQLHelper.TODOLIST_TABLE_NAME, null, null,
//					null, null, null, null);
//		} else {
//			String selection = MySQLHelper.KEY_BOXNUM + "=?";
//			String[] selectionArgs = { String.valueOf(boxId) };
//			cursor = db.query(MySQLHelper.TODOLIST_TABLE_NAME, null, selection,
//					selectionArgs, null, null, null);
//		}
//
//		int idIndex = cursor.getColumnIndex(MySQLHelper.KEY_ID);
//		ArrayList<Integer> mPosition_id = new ArrayList<Integer>();
//		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
//			int tempInt = cursor.getInt(idIndex);
//			Integer integer = tempInt;
//			// Log.e(TAG+"ALL id", String.valueOf(tempInt));
//			mPosition_id.add(integer);
//		}
//		return mPosition_id;
//	}
//
//	public String getFinishedPerCent() {
//		double finishedNUM = getAllIdOfBox(1, false).size();
//		double allNUM = getAllIdOfBox(999, true).size();
//		double percent = finishedNUM / allNUM;
//		NumberFormat nf = NumberFormat.getPercentInstance();
//		nf.setMinimumFractionDigits(2);
//		String str = nf.format(percent);
//		// Log.e(TAG,
//		// String.valueOf(finishedNUM)+"--"+String.valueOf(unFinishedNUM)+str);
//		if (allNUM == 0) {
//			str = "0%";
//		}
//		return str;
//	}
//
//	public int getFinishedPerCentNum() {
//		double finishedNUM = getAllIdOfBox(1, false).size();
//		double allNUM = getAllIdOfBox(999, true).size();
//		double percent = finishedNUM / allNUM * 100;
//
//		return (int) percent;
//	}
//
//	public String getTitleORDetils(int db_id, int tORd) {
//		db = mSQLHelper.getReadableDatabase();
//		String selection = MySQLHelper.KEY_ID + "=?";
//		String[] selectionArgs = { String.valueOf(db_id) };
//		Cursor cursor = db.query(MySQLHelper.TODOLIST_TABLE_NAME, null,
//				selection, selectionArgs, null, null, null);
//		String result = "";
//		int titleIndex = cursor.getColumnIndex(MySQLHelper.KEY_TITLE);
//		int detilsIndex = cursor.getColumnIndex(MySQLHelper.KEY_DETAILS);
//		ArrayList<String> title = new ArrayList<String>();
//		ArrayList<String> detils = new ArrayList<String>();
//		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
//			title.add(cursor.getString(titleIndex));
//			detils.add(cursor.getString(detilsIndex));
//		}
//		if (tORd == 1) {
//			result = title.get(0);
//		} else if (tORd == 2) {
//			result = detils.get(0);
//		}
//
//		return result;
//	}
//
//	public void deleteDB(String dbName) {
//
//		db = mSQLHelper.getWritableDatabase();// 获取数据库对象
//		// 删除hero_info表中所有的数据 传入1 表示删除所有行------>点击back按钮
//		db.delete(dbName, "1", null);
//
//	}
//
//	public int DeleteTodo(int db_id) {
//		db = mSQLHelper.getWritableDatabase();
//		String[] args = { String.valueOf(db_id) };
//		return db.delete(MySQLHelper.TODOLIST_TABLE_NAME, MySQLHelper.KEY_ID
//				+ "=?", args);
//	}
//
//	public int getDBid(int line_position, int boxId, Context c) {
//		ArrayList<Integer> al = getAllIdOfBox(boxId, false);
//		int dbid;
//		if (al.isEmpty() && boxId == 2) {
//			dbid = -999; // when this
//		} else {
//			dbid = al.get(line_position);
//			// Log.e("DayMatter_dbid", String.valueOf(dbid));
//		}
//
//		return dbid;
//	}
//
//	public void closedb(){
//		db.close();
//	}
//	

}
