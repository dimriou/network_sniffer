package com.example.threads;

import java.util.ArrayList;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Implementations {
	
	
	public static volatile boolean isRunning = true;
	
	
	/*
	 * StatisticalReports.
	 */
	public class StatisticalReports implements KvmSerializable{
		
		/*
		 * PC/Laptop's ID.
		 */
		private String ID;
		
		/*
		 * List of IPs.
		 */
		private ArrayList<String> IPs;

		/*
		 * List of String Patterns.
		 */
		private ArrayList<String> Strings;
		
		/*
		 * List of interfaces.
		 */
		private ArrayList<String> interfaces;
		


		
		/*
		 * Constructor.
		 */
		public StatisticalReports(String ID) {
			this.ID = ID;
			IPs = new ArrayList<String>();
			Strings = new ArrayList<String>();
			interfaces = new ArrayList<String>();
			return;
		}
		
		/*
		 * Get ID.
		 */
		public String getID(){
			return this.ID;
		}
		
		/*
		 * Set ID.
		 */
		public void setID(String ID){
			this.ID = ID;
		}
		
		
		/*
		 * Show all array lists.
		 */
		public void show(){
			System.out.println(IPs.toString() + Strings.toString() + interfaces.toString());
		}

		/*
		 * Getter.
		 */
		public ArrayList<String> getIPs() {
			return IPs;
		}

		/*
		 * Setter.
		 */
		public void setIPs(ArrayList<String> iPs) {
			IPs = iPs;
		}

		/*
		 * Getter.
		 */
		public ArrayList<String> getStrings() {
			return Strings;
		}

		/*
		 * Setter.
		 */
		public void setStrings(ArrayList<String> strings) {
			Strings = strings;
		}

		/*
		 * Getter.
		 */
		public ArrayList<String> getInterfaces() {
			return interfaces;
		}

		/*
		 * Setter.
		 */
		public void setInterfaces(ArrayList<String> interfaces) {
			this.interfaces = interfaces;
		}

		@Override
		public String getInnerText() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getProperty(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getPropertyCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setInnerText(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setProperty(int arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}
	

	/*
	 * Not implemented.
	 */
	public static class AvailableNodes implements KvmSerializable{

		/*
		 * List of Available Nodes for the current user.
		 */
		
		private ArrayList<String> IDList;
		
		public ArrayList<String> getIDList() {
			return IDList;
		}

		public void setIDList(ArrayList<String> iDList) {
			IDList = new  ArrayList<String>(iDList);
		}

		/*
		 * Constructor.
		 */
		public AvailableNodes() {}

		@Override
		public String getInnerText() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getProperty(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getPropertyCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setInnerText(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setProperty(int arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
			
		
		
	}

}

/*
 * BufferedReader reader = null;
StringBuilder finals = new StringBuilder();
try {
reader = new BufferedReader(
new InputStreamReader(getAssets().open("Configuration")));

String mLine = reader.readLine();
while (mLine != null) {
finals.append(mLine);
finals.append("\n");
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
 */


