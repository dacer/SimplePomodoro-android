package com.dacer.simplepomodoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import dacer.utils.MyPomoRecorder;

public class RecordFragment extends Fragment {
	private static final String KEY_CONTENT = "MainFragment:Content";
	private String mContent = "???";
	private TextView tv_today;
	private TextView tv_total;
//	private AdView adView;
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
		
		View rootView = inflater.inflate(R.layout.fragment_record, container,
				false);
		initFont(rootView);
		
		ImageButton imageButton = (ImageButton)rootView.findViewById(R.id.btn_setting);
		imageButton.setBackgroundColor(Color.BLACK);
	    imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
			 	intent.setClass(getActivity(), SettingActivity.class);
			 	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			 	startActivity(intent);
			 	getActivity().finish();
			 	getActivity().overridePendingTransition(0, 0);
//				startActivity(new Intent(
//						getActivity(), SettingActivity.class));
//				getActivity().finish();
			}
	    });
		return rootView;
	}

	private void initFont(View rootView){
		TextView tv_total_text = (TextView) rootView.findViewById(R.id.tv_total_text);
		TextView tv_today_text = (TextView) rootView.findViewById(R.id.tv_today_text);
		tv_today = (TextView) rootView.findViewById(R.id.tv_today);
		tv_total = (TextView) rootView.findViewById(R.id.tv_total);
		TextView tv_title = (TextView)rootView.findViewById(R.id.tv_title_record);
		Typeface roboto = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/Roboto-Thin.ttf");
		tv_today.setTypeface(roboto);
		tv_total.setTypeface(roboto);
		tv_title.setTypeface(roboto);
		tv_today_text.setTypeface(roboto);
		tv_total_text.setTypeface(roboto);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean isLightTheme = (sp.getString("pref_theme_type", "black").equals("white"));
        if(isLightTheme){
        	tv_today.setTextColor(Color.BLACK);
    		tv_total.setTextColor(Color.BLACK);
    		tv_title.setTextColor(Color.BLACK);
    		tv_today_text.setTextColor(Color.BLACK);
    		tv_total_text.setTextColor(Color.BLACK);
    		rootView.setBackgroundColor(Color.WHITE);
        }
        MyPomoRecorder mRecorder = new MyPomoRecorder(getActivity());
        tv_total.setText(": "+
        		String.valueOf(mRecorder.getTotalPomo()));
        tv_today.setText(": "+
        		String.valueOf(mRecorder.getTodayPomo()));
//      //ad
//        Boolean caidan = !mRecorder.caidan();
//        if(caidan && mRecorder.getTotalPomo() >3){
//        	adView = new AdView(getActivity(), AdSize.BANNER, "a1519a1170dffb0");
//        	LinearLayout layout = (LinearLayout)rootView.findViewById(R.id.ad_layout);
//        	layout.addView(adView);
//        	adView.loadAd(new AdRequest());
//        	
//        }
//      	    
//      		//AD
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

//	@Override
//	  public void onDestroy() {
//		if (adView != null) {
//			  adView.destroy();
//		}
//	    super.onDestroy();
//	  }
	
}
