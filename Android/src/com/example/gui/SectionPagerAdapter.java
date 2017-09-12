package com.example.gui;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;
	
	public SectionPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.fragments.size();
	}
	public CharSequence getPageTitle(Context context, int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getString(R.string.statistics).toUpperCase(l);
		case 1:
			return context.getString(R.string.interfaces).toUpperCase(l);
		case 2:
			return context.getString(R.string.deletemal).toUpperCase(l);
		case 3:
			return context.getString(R.string.addmal).toUpperCase(l);
		}
		return null;
	}

}
