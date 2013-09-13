package dacer.adapters;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import dacer.google.task.TaskDialogFragment;
import dacer.interfaces.DialogDismissListener;

public class TaskListCursorAdapter extends SimpleCursorAdapter {
//	private final static String TAG = "MySimpleCursorAdapter";
	Context mContext;
	android.support.v4.app.FragmentManager mFragmentManager;
	DialogDismissListener mListener;
	List<String> mTitles;

	public TaskListCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags,
			android.support.v4.app.FragmentManager fm,
			DialogDismissListener listener, List<String> titles) {
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
		mContext = context;
		mFragmentManager = fm;
		mListener = listener;
		mTitles = titles;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View layout = super.getView(position, convertView, parent);
		final int line_id = position;
//		Log.e("line_id", String.valueOf(line_id));
		final int db_id = line_id;

		// Edit the list
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//finish the task!!!!!!!!!!!!!!!!
			}
		});

		layout.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				TaskDialogFragment dialog = new TaskDialogFragment();
				dialog.initDialog(mListener);
				dialog.setIsEditing(line_id, mTitles.get(line_id));
				dialog.show(mFragmentManager, "");
				return true;
			}
		});

		
//		Button btn_detele = (Button) layout.findViewById(R.id.btn_delete);
//		btn_detele.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				int finishedDBid = helper.getDBid(line_id,
//						MySQLHelper.BOX_ID_UNFINISHED, mContext);
//				Boolean isFinished = helper.putTodoInBox(finishedDBid,
//						MySQLHelper.BOX_ID_FINISHED);
//				if (isFinished) {
//					mListener.OnDialogDismiss();
//					Toast.makeText(mContext, 
//							mContext.getString(R.string.have_been_placed_in_the_finished_box), 
//							Toast.LENGTH_SHORT)
//							.show();
//
//				}
//
//			}
//		});
		return layout;
	}

}
