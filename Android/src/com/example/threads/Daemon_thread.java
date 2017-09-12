package com.example.threads;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;

import com.example.database.Database;
import com.example.threads.Implementations.AvailableNodes;

public class Daemon_thread implements Callable<Integer> {
	/*
	 * <service
  android:name="MyService"
  android:icon="@drawable/icon"
  android:label="@string/service_name"
  >
</service> 
	 */

	private volatile Database db;
	
	private volatile String Username;

	private volatile String Password;

	private volatile String ID;

	private volatile AvailableNodes an; 

	private volatile String IP;

	private volatile String MP;


	private static final String NAMESPACE = "http://serverfiles/";
	private static final String URL = "http://192.168.2.7:8081/WSadder/WSkeftedakiastoDI?WSDL";


	/*
	 * Constructor for login,logout,insertMaliciousPatterns,retrieveStatistics.
	 */

	public Daemon_thread(String Username,String Password,Database db){

		this.Password = Password;
		this.Username = Username;
		this.db = db;
	}

	/*
	 * Constructor for delete.
	 */
	public Daemon_thread(String Username,String Password, String ID,Database db){
		
		this.Password = Password;
		this.Username = Username;
		this.ID = ID;
		this.db = db;
	}

	/*
	 * Constructor for registration.
	 */

	public Daemon_thread(String Username,String Password, AvailableNodes an,Database db){

		this.Password = Password;
		this.Username = Username;
		this.an = an;
		this.db = db;
	}

	/*
	 * Constructor for insertMaliciousPatterns.
	 */

	public Daemon_thread(String Username,String Password, String IP, String MP,Database db){

		this.Password = Password;
		this.Username = Username;
		this.IP = IP;
		this.MP = MP;
		this.db = db;
	}
	
	



	@Override
	public Integer call() {
		// TODO Auto-generated method stub

		String SOAP_ACTION;
		String METHOD_NAME;
		int method=0;
		
		
		

		switch (method) {


			//===========================================================insertMaliciousPatterns===============================================================//

		case 1:
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

			}
			catch( Exception e){
				e.printStackTrace();
			}
			break;

			//=================================================================================================================================================//

			//===========================================================login=================================================================================//		

		case 2:
			SOAP_ACTION = "\"http://server/login\"";
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
				
				db.setReturnValue(error, Database.LOGIN_RESPONSE);
				
				if((!(this.Username.equals("XXXXXXX") || this.Password.equals("XXXXXXX"))) && !(this.Password.isEmpty()) )
					return Integer.parseInt(error);

			}
			catch( Exception e){
				e.printStackTrace();
			}
			return 0;
		


			//=================================================================================================================================================//	

			//===========================================================logout=================================================================================//	

		case 3:
			SOAP_ACTION = "\"http://server/registration\"";
			METHOD_NAME = "registration";


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
				
				db.setReturnValue(error, Database.LOGOUT_RESPONSE);


				if(error.contains("true"))
					return 1;
				else
					return 0;
			}
			catch( Exception e){
				e.printStackTrace();
			}
			return 0;

			//=================================================================================================================================================//	


			//===========================================================delete=================================================================================//	

		case 4:
			SOAP_ACTION = "\"http://server/delete\"";
			METHOD_NAME = "delete";


			SOAP_ACTION = "\"http://server/registration\"";
			METHOD_NAME = "registration";


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
				
				db.setReturnValue(error, Database.DELETE_RESPONSE);

				
				if(error.contains("true"))
					return 1;
				else return 0;
				

			}
			catch( Exception e){
				e.printStackTrace();
			}
			break;
			


			//===========================================================retrieveStatistics=========================================================================//
		case 5:
			SOAP_ACTION = "\"http://server/retrieveStatistics\"";
			METHOD_NAME = "retrieveStatistics";


			try{
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("arg0", this.Username);
				request.addProperty("arg1", this.Password);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);

				HttpTransportSE ht = new HttpTransportSE(URL);
				ht.call(SOAP_ACTION, envelope);
				
				ArrayList<String> arraylist1 = new ArrayList<String>();
				ArrayList<String> arraylist2 = new ArrayList<String>();
				ArrayList<String> arraylist3 = new ArrayList<String>();
				
				SoapObject pinakasStatisticalReports = (SoapObject) envelope.bodyIn;
				for(int z = 0; z< pinakasStatisticalReports.getPropertyCount();z++ ){			
					SoapObject stoixeioStatisticalReports =(SoapObject) pinakasStatisticalReports.getProperty(z);
												
					int change = 0;

					for (int i = 1; i< stoixeioStatisticalReports.getPropertyCount(); i++) 
					{ 
						
						String pedio = stoixeioStatisticalReports.getProperty(i).toString();						
						
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
			break;


			//=================================================================================================================================================//

			//===========================================================retrieveMaliciousPatterns===============================================================//
		case 6:
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
			break;

			//=================================================================================================================================================//
			//=================================================================================================================================================//	





		}
		return 0;
	}


}
