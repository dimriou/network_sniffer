package com.example.gui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asynctasks.Registration;
import com.example.threads.Connection;

public class Register extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
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
	
	
	public void register(View view){
		EditText editText = (EditText) findViewById(R.id.password);
		String pass = editText.getText().toString();//use these 2 to reg the user
		editText = (EditText) findViewById(R.id.username);
		String user = editText.getText().toString();//use these 2 to reg the user
			
		Connection check = new Connection();
	    Boolean conn = check.checkNow(this.getApplicationContext());
		    
			
	    Registration rg = new Registration(conn,user,pass,this);
	    rg.execute(this);
		
		
	}
}
