package dacer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dacer.simplepomodoro.MainFragment;
import com.dacer.simplepomodoro.RecordFragment;


public class MyPagerAdapter extends FragmentPagerAdapter {

	
	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			Fragment fragment = new MainFragment();
			return fragment;
		} else {
			Fragment fragment = new RecordFragment();
			return fragment;
		} 

	}

	@Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return "Title";
    }

}
