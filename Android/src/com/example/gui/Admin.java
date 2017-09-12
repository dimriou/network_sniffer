package com.example.gui;

import java.util.List;
import java.util.Vector;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.asynctasks.Logout;
import com.example.gui.Statistics.MyStats;
import com.example.gui.Statistics.SendStat;
import com.example.threads.Connection;

public class Admin extends FragmentActivity implements SendStat, MyStats, ActionBar.TabListener{	
	private SectionPagerAdapter mPagerAdapter;
	private String password;
	private String username;
	private ActionBar actionBar;
	private ViewPager pager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpagerlayout);
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		username = message.split("XXXXXXX")[0];
		password = message.split("XXXXXXX")[1];
		Toast.makeText(this, username + " loged in", Toast.LENGTH_SHORT).show();
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		initialisePaging();
		Statistics st = (Statistics) mPagerAdapter.getItem(0);
		st.getString(username, password);
	}

	private void initialisePaging() {
		// TODO Auto-generated method stub
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Statistics.class.getName()));
		fragments.add(Fragment.instantiate(this, Interfaces.class.getName()));
		fragments.add(Fragment.instantiate(this, Deletor.class.getName()));
		fragments.add(Fragment.instantiate(this, Add.class.getName()));
		mPagerAdapter = new SectionPagerAdapter(this.getSupportFragmentManager(), fragments);

		pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setAdapter(mPagerAdapter);
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mPagerAdapter.getPageTitle(this, i)).setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.user_menu_options, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.search_button://R.id.action_search:
			openSearch();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		case android.R.id.home:
			Intent resultIntent = new Intent();
			setResult(Activity.RESULT_CANCELED, resultIntent);
			this.finish();
			return true;
		case R.id.my_interfaces:
			/*
			 * kaleite thn synarthsh gia to ti emfanizei
			 */
			showme();
			return true;
		case R.id.refresh:
			Interfaces in = (Interfaces) mPagerAdapter.getItem(1);			
			in.getData("remake");
			refresh();
			return true;
		case R.id.log_out_button:
			/*
			edw kaleis synarthsh na ton petajeis ejw apo bash
			 */
	      	Connection check = new Connection();
        	Boolean conn = check.checkNow(this.getApplicationContext());
        	
    	    Logout rg = new Logout(conn,username,password);
    	    rg.execute(this);
    	    
			resultIntent = new Intent();
			setResult(Activity.RESULT_OK, resultIntent);
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Settings button pressed", Toast.LENGTH_SHORT).show();

	}

	private void openSearch() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Search button pressed", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void sendData(String stat) {
		Interfaces in = (Interfaces) mPagerAdapter.getItem(1);
		in.getData(stat);


	}
	@Override
	public void showme() {
		Statistics st = (Statistics) mPagerAdapter.getItem(0);
		st.showMine();
		
	}
	@Override
	public void refresh() {
		Statistics st = (Statistics) mPagerAdapter.getItem(0);
		st.refresh();
		
	}
//	public void deletoras(View view){
//		Deletor del = (Deletor) mPagerAdapter.getItem(2);
//		del.deletoras(view);
//	}
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
