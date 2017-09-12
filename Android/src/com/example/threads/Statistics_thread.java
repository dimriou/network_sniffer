package com.example.threads;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.database.Database;



public class Statistics_thread implements Runnable {


	private Context context;

	private volatile String Username;

	private volatile String Password;

	private static final String NAMESPACE = "http://serverfiles/";
	private static final String URL = "http://192.168.2.7:8081/WSadder/WSkeftedakiastoDI?WSDL";

	public Statistics_thread(String Username, String Password, Context context){
		this.Username = Username;
		this.Password = Password;
		this.context = context;

	}

	public Context getCont(){
		return this.context;
	}

	public String getUsername(){
		return this.Username;
	}

	public String getPassword(){
		return this.Password;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

//		 Looper.prepare();
		
//		final String new_Username = getUsername();
//		final String new_Password = getPassword();
//		final Context new_Context = getCont();
		final ArrayList<String> arraylist1 = new ArrayList<String>();
		final ArrayList<String> arraylist2 = new ArrayList<String>();
		final ArrayList<String> arraylist3 = new ArrayList<String>();
		
		
		
		final Database db = new Database(this.context);

		
//		final Handler m_Handler = new Handler();
//		final Runnable mRunnable = new Runnable(){
			
//			final Connection check = new Connection();
//			@Override
//			public void run() {

			//	Looper.prepare(); 
				Connection check = new Connection();
			while(Implementations.isRunning){
				Boolean conn = check.checkNow(this.context);

				if(conn){


					String SOAP_ACTION;
					String METHOD_NAME;

					//===========================================================retrieveStatistics=========================================================================//

					SOAP_ACTION = "\"http://serverfiles/retrieveStatistics\"";
					METHOD_NAME = "retrieveStatistics";


					try{
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						request.addProperty("arg0", this.Username);
						request.addProperty("arg1", this.Password);
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.setOutputSoapObject(request);

						HttpTransportSE ht = new HttpTransportSE(URL);
						ht.call(SOAP_ACTION, envelope);

						SoapObject pinakasStatisticalReports = (SoapObject) envelope.bodyIn;
						for(int z = 0; z< pinakasStatisticalReports.getPropertyCount();z++ ){			
							SoapObject stoixeioStatisticalReports =(SoapObject) pinakasStatisticalReports.getProperty(z);
														
							int change = 0;

							for (int i = 1; i< stoixeioStatisticalReports.getPropertyCount(); i++) 
							{ 
								
								String pedio = stoixeioStatisticalReports.getProperty(i).toString();
								Log.d("--------------------",pedio);

								
								if(pedio != "")
								{ 
									if(change == 0){
										if( pedio.equals((String)stoixeioStatisticalReports.getPropertyAsString("Strings")) ){
											change = 1;
											arraylist2.add(pedio);
											continue;
										}
										arraylist1.add( pedio);
										
									}
									if(change == 1){
										if(  pedio.equals(stoixeioStatisticalReports.getPropertyAsString("interfaces")) ){
											arraylist3.add( pedio );
											change = 2;
											continue;
										}
										arraylist2.add(pedio);
									}
									if(change == 2){
										arraylist3.add(pedio);
									}
								}
							}
							db.addReport(stoixeioStatisticalReports.getProperty(0).toString() , arraylist1, arraylist2, arraylist3);

						}
						

						

						
						

					}
					catch( Exception e){
						e.printStackTrace();
					}


					//=================================================================================================================================================//

					//===========================================================retrieveMaliciousPatterns===============================================================//

					SOAP_ACTION = "\"http://server/retrieveMaliciousPatterns\"";
					METHOD_NAME = "retrieveMaliciousPatterns";


					try{
						SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
						request.addProperty("arg0", this.Username);
						request.addProperty("arg1", this.Password);
						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.setOutputSoapObject(request);

						HttpTransportSE ht = new HttpTransportSE(URL);
						ht.call(SOAP_ACTION, envelope);

						final SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
						final String str = response.toString();
						
						
						db.setReturnValue(str, Database.MALICIOUS_PATTERN_RESPONSE);

					}
					catch( Exception e){
						e.printStackTrace();
					}

					//=================================================================================================================================================//
				}
				else{
					
					if(db.isRequestsEmpty() == 0){
						Intent wt = new Intent(context, Worker_thread.class);
						context.startService(wt);
					}
					db.addRequest("retrieveStatistics", this.Username, this.Password, null, null);
					db.addRequest("retrieveMaliciousPatterns", this.Username, this.Password, null, null);
					


					
				}
//				m_Handler.postDelayed(this, 3000);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
	}



	}

