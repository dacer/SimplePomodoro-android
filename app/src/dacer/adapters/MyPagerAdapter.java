package dacer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dacer.simplepomodoro.MainFragment;
import com.dacer.simplepomodoro.RecordFragment;

import dacer.google.task.TaskListFragment;
import dacer.settinghelper.SettingUtility;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

	
	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 1) {
			Fragment fragment = new MainFragment();
			return fragment;
		} else if (position == 2) {
			Fragment fragment = new RecordFragment();
			return fragment;
		} else {
			Fragment fragment = new TaskListFragment();
			return fragment;
		}

	}

	@Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return "Title";
    }

}
