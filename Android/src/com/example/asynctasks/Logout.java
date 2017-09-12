package com.example.asynctasks;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.database.Database;
import com.example.threads.Implementations.AvailableNodes;
import com.example.threads.Implementations;
import com.example.threads.Statistics_thread;
import com.example.threads.Worker_thread;




public class Logout extends AsyncTask<Object, Void, Context> {
	
		private volatile boolean starter = false;
		
	
		private volatile int checker=0;


		private volatile boolean connection;

		private volatile String Username;

		private volatile String Password;
				
		
		
		private static final String NAMESPACE = "http://serverfiles/";
		private static final String URL = "http://192.168.2.7:8081/WSadder/WSkeftedakiastoDI?WSDL";


		
		/*
		 * Constructor for registration.
		 */

		public Logout(boolean connection,String Username,String Password){
			this.connection = connection;
			this.Password = Password;
			this.Username = Username;
		}


		@Override
		protected Context doInBackground(Object...params ) {
			// TODO Auto-generated method stub
			
			Database db = new Database((Context) params[0]);
			
			String check = db.getStoredValue(Database.LOGOUT_RESPONSE);
			
			if(check == null){

			String SOAP_ACTION;
			String METHOD_NAME;

			if(!this.connection){
				if(db.isRequestsEmpty() == 0){
					this.starter = true;
				}
				db.addRequest("logout", this.Username, this.Password, null, null);
				this.checker = 10;
				return (Context)params[0];
			}
			else{

		
				//===========================================================logout=========================================================================//
					SOAP_ACTION = "\"http://serverfiles/logout\"";
					METHOD_NAME = "logout";


					try{
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						request.addProperty("arg0", this.Username);
						request.addProperty("arg1", this.Password);
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.setOutputSoapObject(request);

						HttpTransportSE ht = new HttpTransportSE(URL);
						ht.call(SOAP_ACTION, envelope);

						final SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
						final String error = response.toString();
						
						if(error.contains("true"))
							this.checker = 1;
					else
						this.checker = 0;
					
					return (Context)params[0];

				}
				catch( Exception e){
					e.printStackTrace();
				}
				this.checker = 0;
				return  (Context)params[0];
				}

			}
			this.checker = 0;
			return (Context)params[0];
		}
		
		
		@Override
	    protected void onPostExecute(Context context) {
			if(this.checker == 1 ){
				Implementations.isRunning = false;
				Toast.makeText(context, "Logout completed", Toast.LENGTH_SHORT).show();
				
			}
			if(this.checker == 0 ){
				
				Toast.makeText( context, "Error loging out", Toast.LENGTH_SHORT).show();
				//editText.getText().clear();
			}
			if(this.checker == 10){
				if( this.starter ){
	
					Intent wt = new Intent(context, Worker_thread.class);
					context.startService(wt);
				}
				Toast.makeText(context, "No connection ya filthy animal", Toast.LENGTH_SHORT).show();
			}
	        
	    }

}
