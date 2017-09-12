package com.example.threads;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.threads.Implementations.AvailableNodes;






public class Methods_thread implements Runnable {
	//<uses-permission android:name="android.permission.INTERNET" />
	//<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	/*
	 * Method that is used in thread.
	 */

	private volatile int method;

	private volatile boolean connection;

	private volatile String Username;

	private volatile String Password;

	private volatile String ID;

	private volatile AvailableNodes an; 

	private volatile String IP;

	private volatile String MP;


	private static final String NAMESPACE = "http://serverfiles/";
	private static final String URL = "http://192.168.2.3:8081/WSadder/WSkeftedakiastoDI?WSDL";


	/*
	 * Constructor for login,logout.
	 */

	public Methods_thread(int method,boolean connection,String Username,String Password){
		this.method = method;
		this.connection = connection;
		this.Password = Password;
		this.Username = Username;
	}

	/*
	 * Constructor for delete.
	 */
	public Methods_thread(int method,boolean connection,String Username,String Password, String ID){
		this.method = method;
		this.connection = connection;
		this.Password = Password;
		this.Username = Username;
		this.ID = ID;
	}

	/*
	 * Constructor for registration.
	 */

	public Methods_thread(int method,boolean connection,String Username,String Password, AvailableNodes an){
		this.method = method;
		this.connection = connection;
		this.Password = Password;
		this.Username = Username;
		this.an = an;
	}

	/*
	 * Constructor for insertMaliciousPatterns.
	 */

	public Methods_thread(int method,boolean connection,String Username,String Password, String IP, String MP){
		this.method = method;
		this.connection = connection;
		this.Password = Password;
		this.Username = Username;
		this.IP = IP;
		this.MP = MP;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub

		//an yparxei stin vasi pending(return values)

		String SOAP_ACTION;
		String METHOD_NAME;

		if(!this.connection){
			/*if( vasi adeia){
			 * 	
			 * 	vale vasi;
			 * 
			 * 	//ksekina neo worker_thread
			 * 	Daemon_Thread ps = new Daemon_thread(this.Username,this.Password);			
			 * 	Thread nima = new Thread(ps);
			 * 	nima.start();
			 * }
			 * else
			 * 	vale vasi;
			 * 
			 * 
			 * 
			 */
		}
		else{

			switch (this.method) {
			//===========================================================registration=========================================================================//
			case 1:
				SOAP_ACTION = "\"http://server/registration\"";
				METHOD_NAME = "registration";


				try{
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty("arg0", this.Username);
					request.addProperty("arg1", this.Password);
					request.addProperty("arg2", "Aloha");
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.setOutputSoapObject(request);

					HttpTransportSE ht = new HttpTransportSE(URL);
					ht.call(SOAP_ACTION, envelope);

					final SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
					final String error = response.toString();

				}
				catch( Exception e){
					e.printStackTrace();
				}

				break;
				//=================================================================================================================================================//

				//===========================================================insertMaliciousPatterns===============================================================//

			case 2:
				SOAP_ACTION = "\"http://server/insertMaliciousPatterns\"";
				METHOD_NAME = "insertMaliciousPatterns";


				try{
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty("arg0", this.Username);
					request.addProperty("arg1", this.Password);
					request.addProperty("arg3", this.IP);
					request.addProperty("arg1", this.MP);
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.setOutputSoapObject(request);

					HttpTransportSE ht = new HttpTransportSE(URL);
					ht.call(SOAP_ACTION, envelope);

					final SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
					final String str = response.toString();

				}
				catch( Exception e){
					e.printStackTrace();
				}
				break;

				//=================================================================================================================================================//

				//===========================================================login=================================================================================//		

			case 3:
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
					final String str = response.toString();

				}
				catch( Exception e){
					e.printStackTrace();
				}
				break;


				//=================================================================================================================================================//	

				//===========================================================logout=================================================================================//	

			case 4:
				SOAP_ACTION = "\"http://server/logout\"";
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
					final String str = response.toString();

				}
				catch( Exception e){
					e.printStackTrace();
				}
				break;

				//=================================================================================================================================================//	


				//===========================================================delete=================================================================================//	

			case 5:
				SOAP_ACTION = "\"http://server/delete\"";
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
					final String str = response.toString();

				}
				catch( Exception e){
					e.printStackTrace();
				}
				break;
				//=================================================================================================================================================//	





			}



		}


	}



}
