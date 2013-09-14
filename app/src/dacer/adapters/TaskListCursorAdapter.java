//package dacer.adapters;
//
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.support.v4.widget.SimpleCursorAdapter;
//import android.view.GestureDetector;
//import android.view.View;
//import android.view.View.OnLongClickListener;
//import android.view.ViewGroup;
//import dacer.google.task.TaskDialogFragment;
//import dacer.google.task.TaskRecorder;
//import dacer.interfaces.DialogDismissListener;
//
///**
// * Author:dacer
// * Date  :Sep 10, 2013
// */
//public class TaskListCursorAdapter extends SimpleCursorAdapter {
////	private final static String TAG = "MySimpleCursorAdapter";
//	Context mContext;
//	android.support.v4.app.FragmentManager mFragmentManager;
//	DialogDismissListener mListener;
//	ArrayList<String> mTitles;
//	Cursor cursor;
//	private GestureDetector gestureDetector;
//    View.OnTouchListener gestureListener;
//	
//	public TaskListCursorAdapter(Context context, int layout, Cursor c,
//			String[] from, int[] to, int flags,
//			android.support.v4.app.FragmentManager fm,
//			DialogDismissListener listener) {
//		super(context, layout, c, from, to, flags);
//		// TODO Auto-generated constructor stub
//		mContext = context;
//		mFragmentManager = fm;
//		mListener = listener;
//		cursor = c;
//		mTitles = getAllTitlesOfBox(c);
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		View layout = super.getView(position, convertView, parent);
//		final int line_id = position;
////		Log.e("line_id", String.valueOf(line_id));
//		final int db_id = getAllIdOfBox(cursor).get(line_id);
//
//		// Edit the list
////		layout.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				// TODO Auto-generated method stub
////			}
////		});
//		// Gesture detection
////        gestureDetector = new GestureDetector(new MyGestureDetector());
////        gestureListener = new View.OnTouchListener() {
////            public boolean onTouch(View v, MotionEvent event) {
////                return gestureDetector.onTouchEvent(event);
////            }
////        };
////		layout.setOnTouchListener(gestureListener);
//		layout.setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				TaskDialogFragment dialog = new TaskDialogFragment();
//				dialog.initDialog(mListener);
//				dialog.setIsEditing(db_id, mTitles.get(line_id));
//				dialog.show(mFragmentManager, "");
//				return true;
//			}
//		});
//
//		
////		Button btn_detele = (Button) layout.findViewById(R.id.btn_delete);
////		btn_detele.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				// TODO Auto-generated method stub
////				new TaskLocalUtils(GlobalContext.getInstance()).deleteTaskInDBFlagByID(db_id);
////				mListener.OnDialogDismiss();
////
////			}
////		});
//		return layout;
//	}
//
//	private ArrayList<Integer> getAllIdOfBox(Cursor cursor) {
//		int idIndex = cursor.getColumnIndex(TaskRecorder.KEY_ID);
//		ArrayList<Integer> mPosition_id = new ArrayList<Integer>();
//		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
//			int tempInt = cursor.getInt(idIndex);
//			Integer integer = tempInt;
//			mPosition_id.add(integer);
//		}
//		return mPosition_id;
//	}
//	private ArrayList<String> getAllTitlesOfBox(Cursor cursor) {
//		int idIndex = cursor.getColumnIndex(TaskRecorder.KEY_TITLE);
//		ArrayList<String> mPosition_id = new ArrayList<String>();
//		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
//			String temp = cursor.getString(idIndex);
//			mPosition_id.add(temp);
//		}
//		return mPosition_id;
//	}
//
////		 class MyGestureDetector extends SimpleOnGestureListener {
////			 private static final int SWIPE_MIN_DISTANCE = 120;
////			    private static final int SWIPE_MAX_OFF_PATH = 250;
////			    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
////		        @Override
////		        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
////		            try {
////		                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
////		                    return false;
////		                // right to left swipe
////		                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
////		                    Toast.makeText(GlobalContext.getActivity(), "Left Swipe", Toast.LENGTH_SHORT).show();
////		                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
////		                    Toast.makeText(GlobalContext.getActivity(), "Right Swipe", Toast.LENGTH_SHORT).show();
////		                }
////		            } catch (Exception e) {
////		                // nothing
////		            }
////		            return false;
////		        }
////
////		    }
//	
//		 
//}
