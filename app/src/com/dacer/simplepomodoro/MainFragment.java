package com.dacer.simplepomodoro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dacer.interfaces.OnClickCircleListener;
import dacer.settinghelper.SettingUtility;
import dacer.utils.MyUtils;
import dacer.views.CircleView;
import dacer.views.CircleView.RunMode;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class MainFragment extends Fragment implements OnClickCircleListener{
	private static final String KEY_CONTENT = "MainFragment:Content";
	private String content = "???";
	private CircleView mView;
	Context mContext;
	
	public MainFragment() {
		
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            content = savedInstanceState.getString(KEY_CONTENT);
        }
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mView = new CircleView(getActivity(), 
				MyUtils.getScreenWidth()/2, 
				MyUtils.getScreenHeight()/2, 
				MyUtils.getBigCirRadius(getActivity()), 
				getString(R.string.start),
				360, this,RunMode.MODE_ONE);
		return mView;
	}

	@Override
	public void onClickCircle() {
		// TODO Auto-generated method stub
		//Start pomodoro
		new Thread(new myReadyAnimThread()).start();  
		
	}
	
	
	class myReadyAnimThread implements Runnable { 
		private int alpha = 255;
		private int sweep = 360;
		private Boolean run= true;
        public int sweepStep = 4;
        
		@Override
		public void run() {     
            while (!Thread.currentThread().isInterrupted()&& run) {      
                 try {  
                	 mView.setMySweep(sweep);
                	 mView.setMyAlpha(alpha);
                	 mView.postInvalidate();
                	 sweep-=sweepStep;
                	 if(alpha> sweep){
                		 alpha = sweep;
                	 }
                	 if(sweep < 280){
                		 sweepStep = 5;
                	 }
                	 if(sweep < 200){
                		 sweepStep = 6;
                	 }
                	 if(sweep<= 0){
                		 run = false;
                		 Intent intent = new Intent();
                		 intent.setClass(mContext, 
                				 PomoRunningActivity.class);
                		 intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                		 startActivity(intent);
                		 getActivity().finish();
                		 getActivity().overridePendingTransition(0, 0);
                	 }
                	 Thread.sleep(10);       
                 } catch (InterruptedException e) {      
                	 Thread.currentThread().interrupt();      
                 }  
             }      
        }     
	}
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, content);
    }

	
}