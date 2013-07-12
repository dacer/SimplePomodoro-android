package com.dacer.simplepomodoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.umeng.analytics.MobclickAgent;

import dacer.utils.MyScreenLocker;
import dacer.utils.MyUtils;

public class SettingActivity extends PreferenceActivity {
//
//	public static final String PRE_THEME_TYPE = "pref_theme_type";
//	public static final String PRE_ENABLE_FULLSCREEN = "pref_enable_fullscreen";
	
	public static final String PRE_ADREMOVED = "adremoved";
//	public static final String PRE_LIGHTS_ON= "pref_lights_on";
	private AdView adView;
	private Boolean adRemoved;
	private int caidan = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_activity);
        getListView().setItemsCanFocus(true);
        //set the google ad
        SharedPreferences sharedpreferences =  PreferenceManager.getDefaultSharedPreferences(this);
        adRemoved = sharedpreferences.getBoolean(PRE_ADREMOVED, false);
        if(adRemoved){
        	
        }else{
        	adView = new AdView(this, AdSize.BANNER, "a1519a1170dffb0");
            LinearLayout layout = (LinearLayout)findViewById(R.id.ad_view);
            layout.addView(adView);
            adView.loadAd(new AdRequest());
        }
    }
    
    @Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setupSimplePreferencesScreen();		
	}

	private void setupSimplePreferencesScreen() {

		addPreferencesFromResource(MyUtils.
				isUnderHoneycomb()? R.xml.preferences_for_low: R.xml.preferences);
//		ActionBar bar = getSupportActionBar(); 
//		bar.setDisplayHomeAsUpEnabled(true); 
		
		bindPreferenceSummaryToValue(findPreference("pref_theme_type"));
		bindPreferenceSummaryToValue(findPreference("pref_break_duration"));
		bindPreferenceSummaryToValue(findPreference("pref_long_break_duration"));
		bindPreferenceSummaryToValue(findPreference("pref_notification_sound"));
		
		Preference email_us_Preference = findPreference("pref_email_us");
		Preference about_Preference = findPreference("pref_about");
		
		final Preference remove_manage_Preference = findPreference("pref_remove_manage");
		final CheckBoxPreference fast_mode_Preference = 
				(CheckBoxPreference)findPreference("pref_fast_mode");
		
//		if(!fast_mode_Preference.isChecked()){
//			remove_manage_Preference.setEnabled(false);
//		}
//		
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
//						intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//						intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
						startActivity(Intent.createChooser(intent, "Send Email"));
						return false;
					}
				});

		about_Preference
		.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				caidan++;
				if(caidan > 20 && !adRemoved){
					SharedPreferences sharedpreferences =  PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
					Editor editor = sharedpreferences.edit();
					editor.putBoolean(PRE_ADREMOVED, true);
					editor.commit();
					Toast.makeText(SettingActivity.this, 
							String.valueOf("AD removed"), Toast.LENGTH_LONG).show();
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
		
		MobclickAgent.onResume(this);
    }  
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
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

}
