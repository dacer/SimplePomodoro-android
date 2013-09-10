package dacer.google.task;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import dacer.settinghelper.SettingUtility;

public class TaskListFragment extends Fragment {
	private static final String KEY_CONTENT = "MainFragment:Content";
	private String mContent = "???";
	View rootView;
	//Google Task
	private static final Level LOGGING_LEVEL = Level.OFF;
	  private static final String PREF_ACCOUNT_NAME = "accountName";
	  static final String TAG = "TaskListFragment";
	  static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
	  static final int REQUEST_AUTHORIZATION = 1;
	  static final int REQUEST_ACCOUNT_PICKER = 2;
	  final HttpTransport transport = AndroidHttp.newCompatibleTransport();
	  final JsonFactory jsonFactory = new GsonFactory();
	  GoogleAccountCredential credential;
	  List<String> tasksList;
	  ArrayAdapter<String> adapter;
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
	
	
	private void showGoogleTask(){
		Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);
		listView = (PullToRefreshListView) rootView.findViewById(R.id.list_task);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
		    @Override
		    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		    	SyncDBTasks.run(TaskListFragment.this);
		    }
		});
		credential =
		        GoogleAccountCredential.usingOAuth2(getActivity(), Collections.singleton(TasksScopes.TASKS));
		credential.setSelectedAccountName(SettingUtility.getAccountName());
		// Tasks client
		service =
		        new com.google.api.services.tasks.Tasks.Builder(transport, jsonFactory, credential)
		            .setApplicationName("SimplePomodoro").build();
		if (checkGooglePlayServicesAvailable()) {
			haveGooglePlayServices();
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
	    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tasksList);
	    listView.setAdapter(adapter);
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
	          haveGooglePlayServices();
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

	  private void haveGooglePlayServices() {
	    // check if there is already an account selected
	    if (credential.getSelectedAccountName() == null) {
	      chooseAccount();
	    } else {
	    	TaskUtils tUtils = new TaskUtils(service, getActivity());
	    	List<String> result = tUtils.getTasksTitleFromDB();
	        tasksList = result;
	        refreshView();
//	      AsyncLoadTasks.run(this);
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

	
}
