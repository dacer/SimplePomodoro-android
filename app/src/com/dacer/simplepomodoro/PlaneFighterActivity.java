package com.dacer.simplepomodoro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import dacer.planefighter.BGSurfaceView;
import dacer.planefighter.onGameOverListener;
import dacer.settinghelper.SettingUtility;
import dacer.utils.GlobalContext;

public class PlaneFighterActivity extends FragmentActivity implements onGameOverListener{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalContext.setActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        
        initGameTwo();
    }
    
//    private void initGameOne(){
//    	BackgroundView view = new BackgroundView(this);
//    	view.setOnGameOverListener(this);
//        setContentView(view);
//    }

    private void initGameTwo(){
    	BGSurfaceView view = new BGSurfaceView(this);
    	view.setOnGameOverListener(this);
        setContentView(view);
    }
    
	@Override
	public void onGameOver(final int score) {
		// TODO Auto-generated method stub
		
		runOnUiThread(new Runnable() {
			  @Override
			public void run() {
				  if(score >= 40){
					  if(!SettingUtility.isADRemoved()){
						  	SettingUtility.removeAD(true);
							Toast.makeText(PlaneFighterActivity.this, "ADRemoved", Toast.LENGTH_LONG).show();
					  }
						
				  	}else{
						if(SettingUtility.isADRemoved()){
							SettingUtility.removeAD(false);
							Toast.makeText(PlaneFighterActivity.this, "AD is back :)", Toast.LENGTH_LONG).show();
						}
					}
				  AlertDialog.Builder builder = new AlertDialog.Builder(PlaneFighterActivity.this);
					final LayoutInflater inflater = getLayoutInflater();
					final View dialogView = inflater.inflate(
							R.layout.dialog_game_over_fragment, null);
					TextView tvScore = (TextView)dialogView.findViewById(R.id.tv_score);
					tvScore.setText(" "+String.valueOf(score));
					builder.setView(dialogView)
							.setPositiveButton(R.string.restart,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int id) {
											initGameTwo();
										}
									})
							.setNegativeButton(R.string.exit,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int id) {
											finish();
										}
									});
				  AlertDialog dialog = builder.create();
					dialog.setCancelable(false);
					dialog.show();
			  }
			});
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
