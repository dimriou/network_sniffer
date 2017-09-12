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
import com.example.gui.Add;
import com.example.threads.Worker_thread;
import com.example.threads.Implementations.AvailableNodes;




public class InsertMaliciousPatterns extends AsyncTask<Object, Void, Context> {
	
	private volatile int checker = 1;
	
	private volatile boolean starter = false;

	
	private volatile boolean connection;

	private volatile String Username;

	private volatile String Password;

	private volatile String IP;

	private volatile String MP;
	
	private static final String NAMESPACE = "http://serverfiles/";
	private static final String URL = "http://192.168.2.7:8081/WSadder/WSkeftedakiastoDI?WSDL";


	
	/*
	 * Constructor for registration.
	 */

	public InsertMaliciousPatterns(boolean connection,String Username,String Password, String IP, String MP ){
		this.connection = connection;
		this.Password = Password;
		this.Username = Username;
		this.IP = IP;
		this.MP = MP;
	}


	@Override
	protected Context doInBackground(Object...params ) {
		// TODO Auto-generated method stub
		
		Database db = new Database((Context)params[0]);
		
		

		String SOAP_ACTION;
		String METHOD_NAME;

		if(!this.connection){
			if(db.isRequestsEmpty() == 0){
				this.starter = true;
			}
			db.addRequest("insertMaliciousPatterns", this.Username, this.Password, this.IP, this.MP  );
	
			this.checker = 10;
			return (Context)params[0];
		}
		else{

	
			//===========================================================insertMaliciousPatterns=========================================================================//
				SOAP_ACTION = "\"http://serverfiles/insertMaliciousPatterns\"";
				METHOD_NAME = "insertMaliciousPatterns";


				try{
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty("arg0", this.Username);
					request.addProperty("arg1", this.Password);
					request.addProperty("arg2", this.IP);
					request.addProperty("arg3", this.MP);
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.setOutputSoapObject(request);

					HttpTransportSE ht = new HttpTransportSE(URL);
					ht.call(SOAP_ACTION, envelope);

					
					return (Context)params[0];

				}
				catch( Exception e){
					e.printStackTrace();
				}
			}
		return (Context)params[0];

	}
	
	@Override
    protected void onPostExecute(Context context) {
		if(this.checker == 1)
			Toast.makeText(context, "Adding " + this.MP + "  " + this.IP, Toast.LENGTH_SHORT).show();
		else
			if(this.starter){
				
				
				Intent wt = new Intent(context, Worker_thread.class);
				context.startService(wt);
			}
			Toast.makeText( context, "No connection ya filthy animal" , Toast.LENGTH_SHORT).show();
	}

}
