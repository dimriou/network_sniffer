package com.example.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.database.Database;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Database.setMode(true);
		Database db = new Database(this);
		String tmp = db.getStoredValue(Database.LOGIN_RESPONSE);
		if (tmp!=null){
			if(tmp.contains("false")){//an den yparxei hdh sthn bash
				//toast unsuccessfull login
				Toast.makeText(this, "Unsuccessfull login", Toast.LENGTH_SHORT).show();
			}
		}
		tmp = db.getStoredValue(Database.DELETE_RESPONSE);
		if (tmp!=null){
			if(tmp.contains("true")){
				Toast.makeText(this, "Successfull Delete", Toast.LENGTH_SHORT).show();
			}
			else if(tmp.contains("false")){
				Toast.makeText(this, "Unsuccessfull Delete", Toast.LENGTH_SHORT).show();
			}
		}
/*		if()
			String name = "mitsos";//auton pou painrete apo bash
			String pass = "aaa";
				if(name.equals("mitsos")){
					Intent intent =new Intent(this, User.class);
					intent.putExtra(EXTRA_MESSAGE, name + "XXXXXXX" + pass);
					startActivityForResult(intent,1);
				}
				if(name.equals("admin")){
					Intent intent =new Intent(this, Admin.class);
					intent.putExtra(EXTRA_MESSAGE, name + "XXXXXXX" + pass);
					startActivityForResult(intent,1);
				}
		}*/
		setContentView(R.layout.activity_main);
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
		 // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.search_button://R.id.action_search:
	        	openSearch();
	            return true;
	        case R.id.action_settings:
	            openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    	}
	}
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	           //result
	        }
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        	this.finish();
	        	
	        }
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
	
	public void Login(View view) {		
		Intent intent = new Intent(this, LogerActivity.class);
		startActivityForResult(intent, 1);
		
	}
	
	
	public void Register(View view){
		Intent intent = new Intent(this, Register.class);
		startActivity(intent);
	}	
}
