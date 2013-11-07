package com.dacer.simplepomodoro;

import com.robobunny.SeekBarPreference;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import dacer.settinghelper.SettingUtility;
import dacer.utils.GlobalContext;
import dacer.utils.MyScreenLocker;
import dacer.utils.MyUtils;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class SettingActivity extends PreferenceActivity {

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        GlobalContext.setActivity(this);
        setContentView(R.layout.preference_activity);
        getListView().setItemsCanFocus(true);
    }
    
    @Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setupSimplePreferencesScreen();		
	}

	private void setupSimplePreferencesScreen() {

		addPreferencesFromResource(MyUtils.
				isUnderHoneycomb()? R.xml.preferences_for_low: R.xml.preferences);
		bindPreferenceSummaryToValue(findPreference("pref_theme_type"));
//		bindPreferenceSummaryToValue(findPreference("pref_break_duration"));
//		bindPreferenceSummaryToValue(findPreference("pref_pomodoro_duration"));
//        bindPreferenceSummaryToValue(findPreference("pref_long_break_duration"));
		bindPreferenceSummaryToValue(findPreference("pref_notification_sound"));
		bindPreferenceSummaryToValue(findPreference("pref_first_day"));
		bindPreferenceSummaryToValue(findPreference("pref_daily_goal"));

		Preference google_task_Preference = findPreference("pref_sync_with_google_task");
		Preference email_us_Preference = findPreference("pref_email_us");
		Preference donate_Preference = findPreference("donate");
		Preference about_Preference = findPreference("pref_about");
		
		final Preference remove_manage_Preference = findPreference("pref_remove_manage");
		final CheckBoxPreference fast_mode_Preference = 
				(CheckBoxPreference)findPreference("pref_fast_mode");
	
		fast_mode_Preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				if(newValue.toString().equals("true")){
					MyScreenLocker locker = new MyScreenLocker(SettingActivity.this);
					locker.activeManage();
				}else{
					MyScreenLocker locker = new MyScreenLocker(SettingActivity.this);
					locker.removeManage();
					remove_manage_Preference.setEnabled(false);
				}
				return true;
			}
		});
//		
		remove_manage_Preference
		.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				//EMAIL!
				MyScreenLocker locker = new MyScreenLocker(SettingActivity.this);
				locker.removeManage();
				remove_manage_Preference.setEnabled(false);
				fast_mode_Preference.setChecked(false);
				return false;
			}
		});
		
		email_us_Preference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						// TODO Auto-generated method stub
						//EMAIL!
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("message/rfc822");
						intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"dacerfeedback@gmail.com"});
//						intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n"+MyUtils.getDeviceName()+"\n"+MyUtils.getDeviceVersion());
						startActivity(Intent.createChooser(intent, "Send Email"));
						return false;
					}
				});

		donate_Preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("https://m.alipay.com/personal/payment.htm?userId=2088012494284978&reason=%E6%94%AF%E6%8C%81%E8%BD%AF%E4%BB%B6%E5%BC%80%E5%8F%91&weChat=true");  
			    startActivity(new Intent(Intent.ACTION_VIEW,uri));  
				return false;
			}
		});
		
		about_Preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this, PlaneFighterActivity.class)); 
				return false;
			}
		});
		
		
		google_task_Preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				if(SettingUtility.isGTaskFirstEnabled()){
					showGtaskAlertDialog();
					MyScreenLocker locker = new MyScreenLocker(SettingActivity.this);
					locker.removeManage();
					remove_manage_Preference.setEnabled(false);
					fast_mode_Preference.setChecked(false);
				}
				return false;
			}
		});
	}

	@Override  
    protected void onResume() {  
        super.onResume();  
        final Preference remove_manage_Preference = findPreference("pref_remove_manage");
		final CheckBoxPreference fast_mode_Preference = 
				(CheckBoxPreference)findPreference("pref_fast_mode");
        MyScreenLocker locker = new MyScreenLocker(SettingActivity.this);
		remove_manage_Preference.setEnabled(locker.isActivited());
		remove_manage_Preference.setSummary(
				locker.isActivited()?getString(R.string.pref_remove_manage_summary):"");
		fast_mode_Preference.setChecked(locker.isActivited());
    }  
	

	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();
			if (preference instanceof ListPreference) {
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);

			} else if (preference instanceof RingtonePreference) {
				// For ringtone preferences, look up the correct display value
				// using RingtoneManager.
				if (TextUtils.isEmpty(stringValue)) {
					// Empty values correspond to 'silent' (no ringtone).
					preference.setSummary(R.string.pref_ringtone_silent);

				} else {
					Ringtone ringtone = RingtoneManager.getRingtone(
							preference.getContext(), Uri.parse(stringValue));

					if (ringtone == null) {
						// Clear the summary if there was a lookup error.
						preference.setSummary(null);
					} else {
						// Set the summary to reflect the new ringtone display
						// name.
						String name = ringtone
								.getTitle(preference.getContext());
						preference.setSummary(name);
					}
				}

			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference
				.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}
    
    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) 
//    {    
//       switch (item.getItemId()) 
//       {        
//          case android.R.id.home:
//        	 Intent intent = new Intent();
//			 intent.setClass(this,MainActivity.class);
//			 intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//			 startActivity(intent);
//			 finish();
//			 overridePendingTransition(0, 0);
////        	 startActivity(new Intent(this,MainActivity.class));
////             finish();          
//             return true;        
//          default:            
//             return super.onOptionsItemSelected(item);    
//       }
//    }
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	Intent intent = new Intent();
			 intent.setClass(this,MainActivity.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			 startActivity(intent);
			 finish();
			 overridePendingTransition(0, 0);    
        	return false;
        }
        return false;
    }
    
    //google task
    private void showGtaskAlertDialog(){
		AlertDialog d = new AlertDialog.Builder(this)
        .setTitle(getString(R.string.gtask_warning))
        .setMessage(getString(R.string.gtask_warning_summary))
        .setPositiveButton(R.string.ok, new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				
			}
		})
      	.create();
		d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		d.show();
	}
}
