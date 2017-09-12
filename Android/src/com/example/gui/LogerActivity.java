package com.example.gui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asynctasks.Login;
import com.example.threads.Connection;

public class LogerActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_loger);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setTitle("Log in");
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
        	Intent resultIntent = new Intent();
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  if (requestCode == 1) {
	        if(resultCode == RESULT_OK){	        		    	    
	           //clear the textviews
	        	EditText editText = (EditText) findViewById(R.id.password);
	        	editText.getText().clear();
	        	editText = (EditText) findViewById(R.id.username);
	        	editText.getText().clear();
	        }
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        	Intent resultIntent = new Intent();
	        	setResult(Activity.RESULT_CANCELED, resultIntent);
	        	this.finish();
	        }
	    }
  	return;
	}
	public void fregLogin(View view) {		
		EditText editText = (EditText) findViewById(R.id.password);
		String pass = editText.getText().toString();//use these 2 to find the user
		editText = (EditText) findViewById(R.id.username);
		String user = editText.getText().toString();//use these 2 to find the user
		
		
		Connection check = new Connection();
	    Boolean conn = check.checkNow(this.getApplicationContext());
		    
			
	    Login rg = new Login(conn,user,pass);
	    rg.execute(this);


	}

}
