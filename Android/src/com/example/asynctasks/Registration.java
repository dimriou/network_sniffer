package com.example.asynctasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.threads.Implementations.AvailableNodes;




public class Registration extends AsyncTask<Object, Void, Context> {
	
	private volatile Context context;
	
	private volatile int checker=0;

	private volatile boolean connection;

	private volatile String Username;

	private volatile String Password;

	private volatile AvailableNodes an; 
	
	private static final String NAMESPACE = "http://serverfiles/";
	private static final String URL = "http://192.168.2.7:8081/WSadder/WSkeftedakiastoDI?WSDL";


	
	/*
	 * Constructor for registration.
	 */

	public Registration(boolean connection,String Username,String Password, Context context){
		this.connection = connection;
		this.Password = Password;
		this.Username = Username;
		this.context = context;
	}
	



	@Override
	protected  Context doInBackground(Object...params ) {
		// TODO Auto-generated method stub
		


		String SOAP_ACTION;
		String METHOD_NAME;

		if(!this.connection){
			
			this.checker = 10;
			return  (Context)params[0];
		}
		else{

	
			//===========================================================registration=========================================================================//
				SOAP_ACTION = "\"http://serverfiles/registration\"";
				METHOD_NAME = "registration";


				try{
					
					AvailableNodes av = new AvailableNodes();
					ArrayList<String> tmp = new ArrayList<String>();
					BufferedReader reader = null;
					StringBuilder finals = new StringBuilder();
					try {
						reader = new BufferedReader(new InputStreamReader( context.getAssets().open("Configuration.txt")));

						String mLine = reader.readLine();
						int change = 0;
						while (mLine != null) {
							if(mLine.equals("[Available nodes]")){
								change = 1;
							}
							else if (change == 1){
								tmp.add(mLine);
							}
							mLine = reader.readLine();
						}
					}
					catch (IOException e) {}
					finally {
						if (reader != null) {
							try {
								reader.close();
							} catch (IOException e) {}

						}
					}
					av.setIDList(tmp);
					
					
					PropertyInfo pi = new PropertyInfo();
					pi.setName("AvailableNodes");
					pi.setValue(av);
					pi.setType(AvailableNodes.class);
					
					for(String s:tmp){
						Log.d("--------------------------",s);
					}
					
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty("arg0", this.Username);
					request.addProperty("arg1", this.Password);
					request.addProperty(pi);
					
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

					envelope.setOutputSoapObject(request);
					envelope.addMapping(NAMESPACE, "AvailableNodes", new AvailableNodes().getClass());


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
	
	@Override
    protected void onPostExecute( Context context) {
		
		if(this.checker == 1 ){
			
			Toast.makeText(context, "Registration completed", Toast.LENGTH_SHORT).show();
			
		}
		if(this.checker == 2 ){
			
			Toast.makeText( context, "User Exists", Toast.LENGTH_SHORT).show();
			//editText.getText().clear();
		}
		if(this.checker == 10){
			Toast.makeText(context, "No connection ya filthy animal", Toast.LENGTH_SHORT).show();
		}
        
    }


}
