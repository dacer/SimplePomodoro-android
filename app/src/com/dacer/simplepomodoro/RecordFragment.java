package com.dacer.simplepomodoro;

import java.util.ArrayList;
import java.util.List;

import com.dacer.androidcharts.LineView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import dacer.settinghelper.SettingUtility;
import dacer.utils.MyPomoRecorder;
import dacer.views.WeekCirView;
import dacer.views.WeekCirViewMode2;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class RecordFragment extends Fragment {
	private static final String KEY_CONTENT = "MainFragment:Content";
	private String mContent = "???";
	private TextView tv_today;
	private TextView tv_total;
	
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
		initCirView(rootView);
		
		ImageButton settingButton = (ImageButton)rootView.findViewById(R.id.btn_setting);

		if(SettingUtility.isLightTheme()){
			settingButton.setImageResource(R.drawable.ic_action_overflow_gray);
			settingButton.setBackgroundColor(Color.parseColor("#ffffff"));
		}else{
			settingButton.setImageResource(R.drawable.ic_action_overflow);
			settingButton.setBackgroundColor(Color.parseColor("#000000"));
		}
		settingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
			 	intent.setClass(getActivity(), SettingActivity.class);
			 	intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			 	startActivity(intent);
			 	getActivity().finish();
			 	getActivity().overridePendingTransition(0, 0);
			}
	    });
		
		
		
		
		LineView lineView = (LineView)rootView.findViewById(R.id.line_view);
		ArrayList<String> strList = new ArrayList<String>();
		ArrayList<Integer> dataList = new ArrayList<Integer>();
		strList.add("12-01");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		strList.add("12-02");
		dataList.add(2);
		dataList.add(3);
		dataList.add(3);
		lineView.setBottomTextList(strList);
		lineView.setDataList(dataList);
		return rootView;
	}

	private void initCirView(View rootView){
		WeekCirView wcView = (WeekCirView)rootView.findViewById(R.id.weekCirView);
		WeekCirViewMode2 mode2View = (WeekCirViewMode2)rootView.findViewById(R.id.weekCirViewMode2);
		MyPomoRecorder recorder = new MyPomoRecorder(getActivity());
		int[] weekPomo = recorder.getPomoOfThisWeek();
		wcView.setAllColor(Color.parseColor("#cccccc"));
		
		wcView.setIndexColor(recorder.getWeekend(), Color.parseColor("#FFBB33"));
		
		mode2View.enabelMode();
		float[] weekPercent = new float[7];
		for(int i=0; i<7; i++){
			weekPercent[i] = (float)weekPomo[i]/SettingUtility.getDailyGoal();
		}
		mode2View.setEveryPercent(weekPercent);
	}
	
	private void initFont(View rootView){
		TextView tv_total_text = (TextView) rootView.findViewById(R.id.tv_total_text);
		TextView tv_today_text = (TextView) rootView.findViewById(R.id.tv_today_text);
		tv_today = (TextView) rootView.findViewById(R.id.tv_today);
		tv_total = (TextView) rootView.findViewById(R.id.tv_total_num);
		TextView tv_title = (TextView)rootView.findViewById(R.id.tv_title_record);
		Typeface roboto = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/Roboto-Thin.ttf");
		tv_today.setTypeface(roboto);
		tv_total.setTypeface(roboto);
		tv_title.setTypeface(roboto);
		tv_today_text.setTypeface(roboto);
		tv_total_text.setTypeface(roboto);
		
        if(SettingUtility.isLightTheme()){
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
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

}
