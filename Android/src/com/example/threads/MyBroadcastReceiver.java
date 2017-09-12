package com.example.threads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyBroadcastReceiver extends BroadcastReceiver{
	 
	 @Override
	 public void onReceive(Context context, Intent intent) {
	  Bundle extras = intent.getExtras();
	     if (extras != null) {
	      if(extras.containsKey("value")){
	      // System.out.println("Value is:"+extras.get("value"));
	      }
	     }
	 }
	 
	}
