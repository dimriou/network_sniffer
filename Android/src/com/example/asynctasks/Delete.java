package com.example.asynctasks;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.database.Database;
import com.example.threads.Worker_thread;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;





public class Delete extends AsyncTask<Object, Void, Context> {
	
	private volatile int checker = 0;

	private volatile boolean starter = false;

	
	private volatile boolean connection;

	private volatile String Username;

	private volatile String Password;

	private volatile String ID;
	
	private static final String NAMESPACE = "http://serverfiles/";
	private static final String URL = "http://192.168.2.7:8081/WSadder/WSkeftedakiastoDI?WSDL";


	
	/*
	 * Constructor for delete.
	 */
	public Delete(boolean connection,String Username,String Password, String ID){
		this.connection = connection;
		this.Password = Password;
		this.Username = Username;
		this.ID = ID;
	}


	@Override
	protected Context doInBackground(Object...params ) {
		// TODO Auto-generated method stub
		
		
		Database db = new Database((Context)params[0]);
		
		String check = db.getStoredValue(Database.DELETE_RESPONSE);
		
		if(check == null){

		String SOAP_ACTION;
		String METHOD_NAME;

		if(!this.connection){
			if(db.isRequestsEmpty() == 0){
				this.starter = true;
			}
			db.addRequest("delete", this.Username, this.Password, this.ID, null);
			    
			this.checker = 10;
			return (Context)params[0];
		}
		else{

	
			//===========================================================delete=========================================================================//
				SOAP_ACTION = "\"http://serverfiles/delete\"";
				METHOD_NAME = "delete";


				try{
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty("arg0", this.Username);
					request.addProperty("arg1", this.Password);
					request.addProperty("arg2", this.ID);
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
		if(this.checker == 1)
			Toast.makeText(context, "Deleting " + this.ID, Toast.LENGTH_SHORT).show();
		else if(this.checker == 10){
			if(this.starter){
				
				Intent wt = new Intent(context, Worker_thread.class);
				context.startService(wt);
			}
		
			Toast.makeText(context, "No connection ya filthy animal! " + this.ID, Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(context, "Coundl not delete ya filthy animal! " + this.ID, Toast.LENGTH_SHORT).show();

        
    }

}
