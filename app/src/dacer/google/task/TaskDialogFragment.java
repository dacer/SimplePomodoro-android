package dacer.google.task;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dacer.simplepomodoro.R;

import dacer.interfaces.DialogDismissListener;
import dacer.settinghelper.SettingUtility;
import dacer.utils.ColorChanger;

public class TaskDialogFragment extends DialogFragment {

//	private final static String TAG = "TaskDialogFragment";
	private DialogDismissListener mListener = null;
	private String mTitle = null;
	private EditText ET_title;
	private Boolean isEditing = false;
	private int db_id;

	public TaskDialogFragment() {
	}

	// void database locked!!!
	public void initDialog(DialogDismissListener listener) {
		mListener = listener;
	}

	public void setIsEditing(int line_id) {
		isEditing = true;
		db_id = line_id;
		mTitle = new TaskLocalUtils(getActivity()).getTitleById(db_id);
	}
	
	public void setIsEditing(int db_id_input, String title){
		isEditing = true;
		db_id = db_id_input;
		mTitle = title;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(
				R.layout.dialog_edit_task_fragment, null);
		final TextView tv_dialog_title = (TextView) dialogView
				.findViewById(R.id.tv_dialog_title);

		ET_title = (EditText) dialogView.findViewById(R.id.title);
		
		View line = dialogView.findViewById(R.id.line_2);
		if(SettingUtility.isLightTheme()){
			line.setAlpha(0);
			tv_dialog_title.setTextColor(Color.parseColor("#fffffb"));
		}
		
		String title_color = ColorChanger.getCurrentColor(getActivity());
		tv_dialog_title.setBackgroundColor(Color.parseColor(title_color));
		tv_dialog_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ColorChanger.clickToChange(getActivity());
				String title_color = ColorChanger
						.getCurrentColor(getActivity());
				tv_dialog_title.setBackgroundColor(Color
						.parseColor(title_color));
				mListener.OnDialogDismiss();
			}
		});

		ET_title.setText(mTitle);
		if (isEditing) {
			// set the title of edit dialog
			tv_dialog_title.setText(getActivity().getString(R.string.edit_task));
		}
		   Timer timer = new Timer();
	        timer.schedule(new TimerTask()
	        {
	            
	            @Override
				public void run(){
	            ((InputMethodManager)getActivity().
	            		getSystemService(Context.INPUT_METHOD_SERVICE))
	            		.showSoftInput(ET_title, 0);
	            }
	            
	        },
	            150);
		
		builder.setView(dialogView)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								if (ET_title.getText().toString().equals("")) {
									Toast.makeText(getActivity(), 
											getString(R.string.the_title_can_not_be_empty),
											Toast.LENGTH_SHORT).show();
								} else {
									if (isEditing) {
										new TaskLocalUtils(getActivity()).setTitleById(db_id, ET_title.getText()
												.toString());
										if (mListener != null) {
											mListener.OnDialogDismiss();
										}
									} else {
										new TaskLocalUtils(getActivity()).addNewTask(ET_title.getText()
													.toString());
										if (mListener != null) {
											mListener.OnDialogDismiss();
										}
									}
								}
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								TaskDialogFragment.this.getDialog().cancel();

							}
						});

		return builder.create();
	}

}
