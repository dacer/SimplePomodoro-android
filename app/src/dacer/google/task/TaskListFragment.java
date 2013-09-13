package dacer.google.task;

import java.io.IOException;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dacer.simplepomodoro.R;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.tasks.TasksScopes;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import dacer.adapters.TaskListCursorAdapter;
import dacer.interfaces.DialogDismissListener;
import dacer.settinghelper.SettingUtility;
import dacer.utils.GlobalContext;

public class TaskListFragment extends Fragment implements DialogDismissListener{
	private static final String KEY_CONTENT = "MainFragment:Content";
	private String mContent = "???";
	View rootView;
	//Google Task
	private static final Level LOGGING_LEVEL = Level.OFF;
//	  private static final String PREF_ACCOUNT_NAME = "accountName";
	  static final String TAG = "TaskListFragment";
	  static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
	  static final int REQUEST_AUTHORIZATION = 1;
	  static final int REQUEST_ACCOUNT_PICKER = 2;
	  final HttpTransport transport = AndroidHttp.newCompatibleTransport();
	  final JsonFactory jsonFactory = new GsonFactory();
	  GoogleAccountCredential credential;
	  List<String> tasksList;
//	  ArrayAdapter<String> adapter;
	  com.google.api.services.tasks.Tasks service;
	  int numAsyncTasks;
	  PullToRefreshListView listView;
	  
	  
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
		initFont();
		initListener();
		showGoogleTask();
		return rootView;
	}

	
	private void initFont(){
		TextView tv_title = (TextView)rootView.findViewById(R.id.tv_title_task);
		Typeface roboto = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/Roboto-Thin.ttf");
//		tv_record.setText(Html.fromHtml("<u>"+"To be continued"+"</u>"));
		tv_title.setTypeface(roboto);
		tv_title.setText("Task");
        Boolean isLightTheme = SettingUtility.isLightTheme();
        if(isLightTheme){
    		tv_title.setTextColor(Color.BLACK);
    		rootView.setBackgroundColor(Color.WHITE);
        }
	}
	
	private void initListener(){
		Button addTaskBTN = (Button)rootView.findViewById(R.id.btn_add_task);
		addTaskBTN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TaskDialogFragment dialog = new TaskDialogFragment();
				dialog.initDialog(TaskListFragment.this);
				dialog.show(getFragmentManager(), "");
			}
		});
		
		listView = (PullToRefreshListView) rootView.findViewById(R.id.list_task);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
		    @Override
		    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		    	SyncDBTasks.run(TaskListFragment.this);
		    }
		});
	}
	
	
	private void showGoogleTask(){
		Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);
		credential =
		        GoogleAccountCredential.usingOAuth2(getActivity(), Collections.singleton(TasksScopes.TASKS));
		credential.setSelectedAccountName(SettingUtility.getAccountName());
		// Tasks client
		service =
		        new com.google.api.services.tasks.Tasks.Builder(transport, jsonFactory, credential)
		            .setApplicationName("SimplePomodoro").build();
		if (checkGooglePlayServicesAvailable()) {
			try {
				haveGooglePlayServices();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	  void refreshView() {
//	    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tasksList);
//	    listView.setAdapter(adapter);
		TaskLocalUtils tLocalUtils = new TaskLocalUtils(GlobalContext.getInstance());
	    Cursor cr = tLocalUtils.getAllCursor();
	    
	    final TaskListCursorAdapter adapter = new TaskListCursorAdapter(getActivity(), R.layout.my_task_list, 
					cr,
					new String[] { TaskRecorder.KEY_TITLE },
					new int[] { R.id.tvLarger }, 2, getFragmentManager(), this);
		listView.setAdapter(adapter);
		
		SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
//                                    adapter.remove(adapter.getItem(position));
                                	//delete 
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
		listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener(touchListener.makeScrollListener());
		}

	  @Override
	public void onResume() {
	    super.onResume();
//	    if (checkGooglePlayServicesAvailable()) {
//	      haveGooglePlayServices();
//	    }
	  }

	  @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    switch (requestCode) {
	      case REQUEST_GOOGLE_PLAY_SERVICES:
	        if (resultCode == Activity.RESULT_OK) {
	          try {
				haveGooglePlayServices();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        } else {
	          checkGooglePlayServicesAvailable();
	        }
	        break;
	      case REQUEST_AUTHORIZATION:
	        if (resultCode == Activity.RESULT_OK) {
	          SyncDBTasks.run(this);
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
	            SyncDBTasks.run(this);
	          }
	        }
	        break;
	    }
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

	  private void haveGooglePlayServices() throws IOException {
	    // check if there is already an account selected
	    if (credential.getSelectedAccountName() == null) {
	      chooseAccount();
	    } else {
//	    	List<String> result = tLocalUtils.getTasksTitleFromDB();
//	        tasksList = result;
	        refreshView();
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

	
}
