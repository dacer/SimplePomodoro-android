package dacer.google.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dacer.simplepomodoro.R;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.tasks.TasksScopes;

import dacer.interfaces.DialogDismissListener;
import dacer.settinghelper.SettingUtility;
import dacer.utils.GlobalContext;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
/**
 * Author:dacer
 * Date  :Sep 10, 2013
 */
public class TaskListFragment extends Fragment implements DialogDismissListener{
	private static final String KEY_CONTENT = "MainFragment:Content";
	private String mContent = "???";
	View rootView;
	//Google Task
	private static final Level LOGGING_LEVEL = Level.OFF;
	static final String TAG = "TaskListFragment";
	static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
	static final int REQUEST_AUTHORIZATION = 1;
	static final int REQUEST_ACCOUNT_PICKER = 2;
	final HttpTransport transport = AndroidHttp.newCompatibleTransport();
	final JsonFactory jsonFactory = new GsonFactory();
	GoogleAccountCredential credential;
	List<String> tasksList;
	com.google.api.services.tasks.Tasks service;
	int numAsyncTasks;
	eu.erikw.PullToRefreshListView listView;
	ArrayList<String> mTitles;
    ArrayList<Integer> mIds;
	  
	  
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_task, container,
				false);
		initView();
		refreshView();
		initGoogleTask();
		return rootView;
	}

	
	private void initView(){
		TextView tv_title = (TextView)rootView.findViewById(R.id.tv_title_task);
		ImageButton addTaskBTN = (ImageButton)rootView.findViewById(R.id.btn_add_task);
		listView = (eu.erikw.PullToRefreshListView) rootView.findViewById(R.id.list_task);
		listView.setOnRefreshListener(new OnRefreshListener() {

		    @Override
		    public void onRefresh() {
		    	if(SettingUtility.enableGTask()){
			        syncTask();
		    	}else{
		    		Toast.makeText(getActivity(), 
		    				getActivity().getString(
		    						R.string.Sync_with_google_task_is_not_enabled),
		    				Toast.LENGTH_LONG).show();
		    		listView.onRefreshComplete();
		    	}
		    }
		});
		
		Typeface roboto = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/Roboto-Thin.ttf");
//		tv_record.setText(Html.fromHtml("<u>"+"To be continued"+"</u>"));
		tv_title.setTypeface(roboto);
		tv_title.setText("Task");
        Boolean isLightTheme = SettingUtility.isLightTheme();
        if(isLightTheme){
    		tv_title.setTextColor(Color.BLACK);
    		rootView.setBackgroundColor(Color.WHITE);
    		
    		addTaskBTN.setBackgroundColor(Color.argb(1, 0, 0, 0));
    		addTaskBTN.setImageResource(R.drawable.new_btn_gray);
        }
        addTaskBTN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TaskDialogFragment dialog = new TaskDialogFragment();
				dialog.initDialog(TaskListFragment.this);
				dialog.show(getFragmentManager(), "");
			}
		});
        if(SettingUtility.enableGTask()){
        	addTaskBTN.setOnLongClickListener(new OnLongClickListener() {
    			
    			@Override
    			public boolean onLongClick(View arg0) {
    				// TODO Auto-generated method stub
//    				syncTask();
    				chooseAccount();
    				return true;
    			}
    		});
        }
        
        
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
//                                    mAdapter.remove(mAdapter.getItem(position));
                                	new TaskLocalUtils(GlobalContext.getInstance()).
                                	setCompletedByID(mIds.get(position-1),true);//position begin with 1,ArrayList begin with 0!
                                	
                                }
//                                mAdapter.notifyDataSetChanged();
                                refreshView();//Temporary solution for mAdapter do not have remove void 
                                			//delete the tasks in cursor && adapter to replace it.
                            }
                        });
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());
        listView.setOnItemLongClickListener (new OnItemLongClickListener() {
        	  public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
        		TaskDialogFragment dialog = new TaskDialogFragment();
  				dialog.initDialog(TaskListFragment.this);
  				dialog.setIsEditing(mIds.get(position), mTitles.get(position));
  				dialog.show(getFragmentManager(), "");
				return false;
        	  }
        });
	}

	private void initGoogleTask(){
		Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);
		credential =
		        GoogleAccountCredential.usingOAuth2(getActivity(), Collections.singleton(TasksScopes.TASKS));
		credential.setSelectedAccountName(SettingUtility.getAccountName());
		// Tasks client
		service =
		        new com.google.api.services.tasks.Tasks.Builder(transport, jsonFactory, credential)
		            .setApplicationName("SimplePomodoro").build();
		checkGooglePlayServicesAvailable();
//		}
	}
	
	  void refreshView() {
		TaskLocalUtils tLocalUtils = new TaskLocalUtils(GlobalContext.getInstance());
	    final Cursor cr = tLocalUtils.getAllCursorInMainList();
	    mTitles = getAllTitlesOfCurosr(cr);
	    mIds = getAllIdFromCursor(cr);
	    
//	    final TaskListCursorAdapter mAdapter = new TaskListCursorAdapter(getActivity(), R.layout.my_task_list, 
//					cr,
//					new String[] { TaskRecorder.KEY_TITLE },
//					new int[] { R.id.tvLarger }, 2, getFragmentManager(), this);
	    final SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getActivity(), 
	    		R.layout.my_task_list, cr, new String[] { TaskRecorder.KEY_TITLE },
	    		new int[] { R.id.tvLarger });
		listView.setAdapter(mAdapter);
		
		
	  }

	  @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    switch (requestCode) {
	      case REQUEST_GOOGLE_PLAY_SERVICES:
	        if (resultCode == Activity.RESULT_OK) {
	          syncTask();
	        } else {
	          checkGooglePlayServicesAvailable();
	        }
	        break;
	      case REQUEST_AUTHORIZATION:
	        if (resultCode == Activity.RESULT_OK) {
	          FragmentAsyncTask.run(this);
	        } else {
	          chooseAccount();
	        }
	        break;
	      case REQUEST_ACCOUNT_PICKER:
	        if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
	          String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
	          if (accountName != null) {
	            credential.setSelectedAccountName(accountName);
	            SettingUtility.setAccountName(accountName);
	            FragmentAsyncTask.run(this);
	          }
	        }
	        break;
	    }
	  }
	  
	  
		
		void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
		    getActivity().runOnUiThread(new Runnable() {
		      public void run() {
		        Dialog dialog =
		            GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, getActivity(),
		                REQUEST_GOOGLE_PLAY_SERVICES);
		        dialog.show();
		      }
		    });
		  }
		
	  /** Check that Google Play services APK is installed and up to date. */
	  private boolean checkGooglePlayServicesAvailable() {
	    final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
	    if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
	      showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
	      return false;
	    }
	    return true;
	  }

	  private void syncTask() {
	    // check if there is already an account selected
	    if (credential.getSelectedAccountName() == null) {
	      chooseAccount();
	    } else {
	    	FragmentAsyncTask.run(this);
	    }
	  }

	  private void chooseAccount() {
	    startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
	  }
	  
	  
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
	@Override
	public void OnDialogDismiss() {
		// TODO Auto-generated method stub
		refreshView();
		
	}

	private ArrayList<Integer> getAllIdFromCursor(Cursor cursor){
		int idIndex = cursor.getColumnIndex(TaskRecorder.KEY_ID);
		ArrayList<Integer> mPosition_id = new ArrayList<Integer>();
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			int tempInt = cursor.getInt(idIndex);
			Integer integer = tempInt;
			mPosition_id.add(integer);
		}
		return mPosition_id;
	}
	
	 private ArrayList<String> getAllTitlesOfCurosr(Cursor cursor) {
		int idIndex = cursor.getColumnIndex(TaskRecorder.KEY_TITLE);
		ArrayList<String> mPosition_id = new ArrayList<String>();
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			String temp = cursor.getString(idIndex);
			mPosition_id.add(temp);
		}
		return mPosition_id;
	}
	
}
