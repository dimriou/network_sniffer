package com.example.threads;

import java.util.ArrayList;
import java.util.concurrent.FutureTask;

import com.example.database.Database;
import com.example.database.Database.Request;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

public class Worker_thread extends IntentService {
	/*//einai dikia mou efthini na kanw stopservice/stopself<----------------------------------------------------------------------------------------------------
	 * Intent intent = new Intent(this, DownloadService.class);
    // add infos for the service which file to download and where to store
    intent.putExtra(DownloadService.FILENAME, "index.html");
    intent.putExtra(DownloadService.URL,
        "http://www.vogella.com/index.html");
    startService(intent);
	 */
	
	/*
	 * <service
  android:name="Worker_thread"
  android:icon="@drawable/icon"
  android:label="@string/service_name"
  >
</service> 
	 */

	
	
	
	 public Worker_thread(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
	    return Service.START_STICKY;
	  }




	@Override
	protected void onHandleIntent(Intent intent) {
		 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		   // return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	
		
		Database db = new Database(this);
		
		
		
		
		while(true){
			if(cm.getActiveNetworkInfo().isConnectedOrConnecting()){
				ArrayList<Database.Request> tmp = (ArrayList<Request>)db.getRequests();
				int ans;
				for(Request i: tmp){

					if( i.getRequest().equals("insertMaliciousPatterns") ){

						Daemon_thread dt = new Daemon_thread(i.getUsername(), i.getPassword(),i.getArg1(),i.getArg2(),db );
						FutureTask<Integer> fu = new FutureTask<Integer>(dt);
						fu.run();
						
						
						
						try {
							// We try to get the return value of our thread.
							ans = fu.get();
						} catch (Exception e) {
							System.out.println("Error while retrieving the return value of insertMalicious");
							System.exit(0);
						}
						

					}
					if( i.getRequest().equals("login") ){

						Daemon_thread dt = new Daemon_thread(i.getUsername(), i.getPassword(),db );
						FutureTask<Integer> fu = new FutureTask<Integer>(dt);
						fu.run();
						
						
						try {
							// We try to get the return value of our thread.
							ans = fu.get();
							Intent tenta = new Intent();
							intent.setAction("login");
							intent.putExtra("value", ans);
							sendBroadcast(tenta);
							
							
						} catch (Exception e) {
							System.out.println("Error while retrieving the return value of login");
							System.exit(0);
						}

					}
					if( i.getRequest().equals("logout") ){

						Daemon_thread dt = new Daemon_thread(i.getUsername(), i.getPassword(),db );
						FutureTask<Integer> fu = new FutureTask<Integer>(dt);
						fu.run();
						
						try {
							// We try to get the return value of our thread.
							ans = fu.get();
							Intent tenta = new Intent();
							intent.setAction("logout");
							intent.putExtra("value", ans);
							sendBroadcast(tenta);						} 
							catch (Exception e) {
							System.out.println("Error while retrieving the return value of logout");
							System.exit(0);
						}

					}
					if( i.getRequest().equals("delete") ){

						Daemon_thread dt = new Daemon_thread(i.getUsername(), i.getPassword(),i.getArg1(),db );
						FutureTask<Integer> fu = new FutureTask<Integer>(dt);
						fu.run();
						
						try {
							// We try to get the return value of our thread.
							ans = fu.get();
							Intent tenta = new Intent();
							intent.setAction("delete");
							intent.putExtra("value", ans);
							sendBroadcast(tenta);
						} catch (Exception e) {
							System.out.println("Error while retrieving the return value of delete");
							System.exit(0);
						}

					}
				}
				break;
			}
			else{
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
