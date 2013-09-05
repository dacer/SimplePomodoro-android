package dacer.utils;

import android.app.Activity;
import android.app.Application;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class GlobalContext extends Application{

    private static GlobalContext globalContext = null;
	public static Activity activity = null;
	
	@Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
    }
	
	public static GlobalContext getInstance() {
        return globalContext;
    }
	
	public static Activity getActivity(){
		return activity;
	}
	
	public static void setActivity(Activity a){
		activity = a;
	}
}
