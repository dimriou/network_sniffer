package serverfiles;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import serverfiles.WSImplementation.AvailableNodes;

/**
 *
 * This is the web service interface.
 *
 * @author dimitris
 *
 */
@WebService
@SOAPBinding(style=Style.DOCUMENT)
public interface WSInterface {

	/*******************/
	/***   CLASSES   ***/
	/*******************/


	/*
	 * StatisticalReports.
	 */
	public class StatisticalReports{

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

	}

	/*
	 * MaliciousPatterns.
	 */
	public class MaliciousPatterns{

		/*
		 * List of the new IPs.
		 */
		private List<String> IPs;

		/*
		 * List of the new String Patterns.
		 */
		private List<String> Strings;

		/*
		 * Constructor.
		 */
		public MaliciousPatterns() {
			return;
		}

		/*
		 * Setter.
		 */
		public void setIPs(List<String> iPs) {
			IPs = iPs;
		}

		/*
		 * Setter.
		 */
		public void setStrings(List<String> strings) {
			Strings = strings;
		}

		/*
		 * Printing.
		 */
		public void show(){
			System.out.println(IPs.toString() + Strings.toString());
		}

		/*
		 * Getter
		 */
		public List<String> getIPs() {
			return IPs;
		}

		/*
		 * Getter
		 */
		public List<String> getStrings() {
			return Strings;
		}


	}



	/*******************/
	/*** WEB METHODS ***/
	/*******************/

	@WebMethod
	public boolean register(String nodeID);

	@WebMethod
	public MaliciousPatterns maliciousPatternRequest(String nodeID);

	@WebMethod
	public void maliciousPatternsStatisticalReport(String nodeID, StatisticalReports m);

	@WebMethod
	public boolean unregister(String nodeID);

	//-----------------------------------------------------------------------------------

	@WebMethod
	public boolean registration(String username, String password, AvailableNodes nodes);

	@WebMethod
	public StatisticalReports[] retrieveStatistics(String username, String password);

	@WebMethod
	public String retrieveMaliciousPatterns(String username,String password);

	@WebMethod
	public void insertMaliciousPatterns(String username,String password, String malliciousIP,String stringPatterns);

	//-----------------------------------------------------------------------------------

	@WebMethod
	public int login(String username, String password);

	@WebMethod
	public boolean logout(String username, String password);

	@WebMethod
	public boolean delete (String username, String password, String nodeId);

}
