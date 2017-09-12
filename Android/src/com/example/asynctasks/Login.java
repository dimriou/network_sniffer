package com.example.asynctasks;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.database.Database;
import com.example.gui.Admin;
import com.example.gui.User;
import com.example.threads.Worker_thread;





public class Login  extends AsyncTask<Object, Void, Context>{
	
	public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
	
	private volatile boolean starter = false;
	
	private volatile int checker = 0;
	
	private volatile boolean connection;

	private volatile String Username;

	private volatile String Password;
	
	private static final String NAMESPACE = "http://serverfiles/";
	private static final String URL = "http://192.168.2.7:8081/WSadder/WSkeftedakiastoDI?WSDL";


	
	/*
	 * Constructor for registration.
	 */

	public Login(boolean connection,String Username,String Password ){
		this.connection = connection;
		this.Password = Password;
		this.Username = Username;
	}


	@Override
	protected Context doInBackground(Object...params ) {
		
	
		// TODO Auto-generated method stub
		Database db = new Database((Context)params[0]);
		
		String check = db.getStoredValue(Database.LOGIN_RESPONSE);
		
		if(check == null){

		String SOAP_ACTION;
		String METHOD_NAME;

		if(!this.connection){
			if(db.isRequestsEmpty() == 0){
				this.starter = true;
			}
			db.addRequest("login", this.Username, this.Password, null, null);

			this.checker = 10;
			return (Context)params[0];
		}
		else{

	
			//===========================================================login=========================================================================//
				SOAP_ACTION = "\"http://serverfiles/login\"";
				METHOD_NAME = "login";


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
					
					if((!(this.Username.equals("XXXXXXX") || this.Password.equals("XXXXXXX"))) && !(this.Password.isEmpty()) ){
						if( Integer.parseInt(error)==2 ){
							this.checker = 2;
							return (Context)params[0];
			
						}
						else if(Integer.parseInt(error)==1){				
							this.checker = 1;
							return (Context)params[0];
			
						}
						else{
							this.checker = 0;
							return (Context)params[0];
			
						}
					}
					else{
						
					this.checker = 3;
					
					return (Context)params[0];
					}
				}
				catch( Exception e){
					e.printStackTrace();
				}
				this.checker = 0;
				return (Context)params[0];
			}

		}
		this.checker = 0;
		return (Context)params[0];
	}
	
	@Override
    protected void onPostExecute(Context context) {
		if(this.checker == 10 ){
			if(this.starter){
				Intent wt = new Intent(context, Worker_thread.class);
				context.startService(wt);
			}
			Toast.makeText(context, "No connection ya filthy animal", Toast.LENGTH_SHORT).show();
		}
		if(this.checker == 2){
			
			Intent intenta = new Intent(context, Admin.class); 
			intenta.putExtra(EXTRA_MESSAGE, this.Username + "XXXXXXX" + this.Password );
			
			((Activity)context).startActivityForResult(intenta, 1);	
		}
		else if(this.checker == 1){
		
		
		Intent intentb = new Intent(context, User.class);
		intentb.putExtra(EXTRA_MESSAGE, this.Username + "XXXXXXX" + this.Password );
		((Activity)context).startActivityForResult(intentb, 1);	
		}
		else if( this.checker == 0){
			Toast.makeText(context, "Could not operate", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(context, "XXXXXXX not allowed and password can't be empty", Toast.LENGTH_SHORT).show();
        
    }

}
