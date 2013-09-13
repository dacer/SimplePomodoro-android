package dacer.utils;

import java.util.ArrayList;

import dacer.settinghelper.SettingUtility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class ColorChanger {
	public final static String SP_COLOR_STRING = "sp_color_string";

	//Change the color String between 6 kinds of colors in order.
	public static void clickToChange(Context mContext) {
		ArrayList<String> color_al = new ArrayList<String>();
		color_al.add("#f56545");
		color_al.add("#ffbb33");
		color_al.add("#bbe535");
		color_al.add("#AA66CC");
		color_al.add("#66ccdd");
		color_al.add("#77ddbb");
		SharedPreferences sharedPreferences = PreferenceManager.
				getDefaultSharedPreferences(mContext);
		Editor editor = sharedPreferences.edit();
		String title_color = sharedPreferences.getString(SP_COLOR_STRING,
				"#ffbb33");
		int current_color_num = color_al.indexOf(title_color);
		if (current_color_num == 5) {
			editor.putString(SP_COLOR_STRING, color_al.get(0));
		} else {
			editor.putString(SP_COLOR_STRING,
					color_al.get(current_color_num + 1));
		}
		editor.commit();
	}

	//Get the current color.
	public static String getCurrentColor(Context mContext) {
		SharedPreferences sharedPreferences = PreferenceManager.
				getDefaultSharedPreferences(mContext);
		String current_color = sharedPreferences.getString(SP_COLOR_STRING,
				"#ffbb33");
		if(!SettingUtility.isLightTheme()){
			current_color = "#ffffff";
		}
		return current_color;
	}

}
