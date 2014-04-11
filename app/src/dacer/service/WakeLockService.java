package dacer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;


import com.dacer.simplepomodoro.MainActivity;
import com.dacer.simplepomodoro.R;

import dacer.settinghelper.SettingUtility;


/**
 * Created by Dacer on 10/3/13.
 * For Mi phone's. Prevent phone from sleeping.
 * Need to destroy it manually.
 * And auto play tick by setting.
 */
public class WakeLockService extends Service {
    private PowerManager.WakeLock wl;
    private MediaPlayer mediaPlayer;
    
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
//        LogRecorder.record("---WakelockService started---");
        init();
    }

    public void onDestroy (){
//        LogRecorder.record("---WakelockService destroyed---");
        if(wl.isHeld()){
            wakeRelase();
        }
        stopTick();
    }

    private void init(){
        //Wake Lock
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SimplePomodoro");
        wakeLock();


        //Put service to foreground
        String tickerText = "";
        Notification notification = new Notification(R.drawable.ic_notification_icon, tickerText,
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, getString(R.string.sp_is_running),
                getString(R.string.click_to_return), pendingIntent);
        startForeground(22, notification);


        //Play tick and disable Ticking in Break Duration
        if(SettingUtility.isTick() && !SettingUtility.isBreakRunning()){
            playTick();
        }
//        if (SettingUtility.isBreakRunning()){
//        	stopSelf();
//        }
    }

    private void wakeLock(){
        wl.acquire();
//        LogRecorder.record("---Wake Locked---");
    }

    private void wakeRelase(){
        wl.release();
//        LogRecorder.record("---Wake Released---");
    }

    private void playTick() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tick);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void stopTick() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
