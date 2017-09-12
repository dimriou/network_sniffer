package serverfiles;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

import Database.DB_Access;

/**
 *
 * This is the implementation of the web service methods.
 *
 * @author dimitris
 *
 */
@WebService(endpointInterface="serverfiles.WSInterface")
public class WSImplementation implements WSInterface {

	//======================================================================//

		public static class proUser{

			/*
			 *  Username.
			 */
			private String Username;

			/*
			 * Password.
			 */
			private String Password;

			public String getPassword() {
				return Password;
			}

			/*
			 * Admin privileges or not.
			 */
			private boolean Ultrapro;

			/*
			 * Counter.
			 */
			private int reportCounter;

			public void setReportCounter(int reportCounter) {
				this.reportCounter = reportCounter;
			}

			public int getReportCounter() {
				return reportCounter;
			}

			/*
			 * Constructor.
			 */
			public proUser(String Username, String Password, boolean Ultrapro,int pos){

				this.Username = Username;
				this.Password = Password;
				this.Ultrapro = Ultrapro;
				this.reportCounter = pos;
			}

			public boolean getAdmin(){
				return Ultrapro;
			}

			public String getUsername(){
				return Username;
			}

			public void setSkill(){
				this.Ultrapro = true;
			}

			@Override
			public boolean equals(Object obj) {
				proUser user = (proUser) obj;
				if (Username.equals(user.Username))
					return true;
				return false;
			}

		}


		/*
		 * This is an ArrayList that holds all the proUsers(Android) inserted into the system.
		 */
		private static ArrayList<proUser> proUserList = new ArrayList<proUser>();


		/*
		 * AvailableNodes class.
		 */
		public static class AvailableNodes{

			/*
			 * List of Available Nodes for the current user.
			 */

			private ArrayList<String> IDList;

			/*
			 * Constructor.
			 */
			public AvailableNodes() {}

			public ArrayList<String> getIDList() {
				return IDList;
			}

			public void setIDList(ArrayList<String> iDList) {
				IDList = iDList;
			}

		}

		/*****************************************/
		/*********** IMPLEMENTATION **************/
		/*****************************************/

		@Override
		@WebMethod
		/*
		 * Login.
		 * (non-Javadoc)
		 * @see serverfiles.WSInterface#login(java.lang.String, java.lang.String)
		 */
		public int login(String username, String password){
			System.out.println("Login: " + username + " " + password);
			int position = DB_Access.getPatternPosition(username, password);
			if (position == -1){
				return 0;
			}
			boolean skillz = DB_Access.isAdmin(username, password);

			proUser pUser = new proUser(username, password, skillz,position);

			synchronized(proUserList){
				proUserList.add(pUser);
			}
			return ( (skillz==true) ? 2: 1);
		}

		@Override
		@WebMethod
		/*
		 * Logout.
		 * (non-Javadoc)
		 * @see serverfiles.WSInterface#logout(java.lang.String, java.lang.String)
		 */
		public boolean logout(String username, String password){
			System.out.println("Logout: " + username + " " + password);
			proUser user = new proUser(username, password, false, 0);
			int i=0;
			synchronized(proUserList){
				// If the user exists.
				if ( (i = proUserList.indexOf(user)) != -1){
					int counter = proUserList.get(i).getReportCounter();
					DB_Access.setPatternPosition(username, password, counter);
					proUserList.remove(i);
					return(true);
				}
			}
			return false;
		}

		@Override
		@WebMethod
		/*
		 * Delete.
		 * (non-Javadoc)
		 * @see serverfiles.WSInterface#delete(java.lang.String, java.lang.String, java.lang.String)
		 */
		public boolean delete (String username, String password, String nodeID){
			System.out.println("Delete: " + username + " " + password + " " + nodeID);
			proUser puser = new proUser(username, password, false, 0);
			int i=0;
			synchronized(proUserList){
				// If the user doesn't exist.
				if ( (i = proUserList.indexOf(puser)) == -1){
					return(false);
				}
			}

			User user = new User(nodeID);
			i=0;
			synchronized(UserList){
				// If the user is active.
				if ( (i = UserList.indexOf(user)) != -1){
					UserList.remove(i);
					// inform the user
				}
			}
			return (DB_Access.delete(nodeID));
		}

	//======================================================================//

	/*
	 * User.
	 */
	public static class User{

		/*
		 * ID.
		 */
		private String ID;

		/*
		 * IPposition.
		 */
		private int IPposition;

		/*
		 * MPposition.
		 */
		private int MPposition;

		/*
		 * Constructor.
		 */
		public User(String ID){
			this.ID = ID;
			IPposition = 0;
			MPposition = 0;
		}

		/*
		 * Getter.
		 */
		public String getID() {
			return ID;
		}

		/*
		 * Getter.
		 */
		public int getIPposition() {
			return IPposition;
		}

		/*
		 * Getter.
		 */
		public int getMPposition() {
			return MPposition;
		}

		/*
		 * Increase value by a certain amount.
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public void increaseIPposition(int amount){
			IPposition += amount;
		}

		/*
		 * Increase value by a certain amount.
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public void increaseMPposition(int amount){
			MPposition += amount;
		}

		@Override
		public boolean equals(Object obj) {
			User u = (User)obj;
			return(this.ID.equals(u.getID()));
		}

		@Override
		public String toString(){
			return("ID :" + ID + " IP position: " + IPposition + " MP position: " + MPposition + "\n");
		}

	}

	/*
	 * This is an ArrayList that holds all the IPs inserted into the system.
	 */
	private static ArrayList<String> IPfile = new ArrayList<String>();

	/*
	 * This is an ArrayList that holds all the patterns inserted into the system.
	 */
	private static ArrayList<String>  MPfile = new ArrayList<String>();


	/*
	 * This is an ArrayList that holds all the active users inserted into the system.
	 */
	private static ArrayList<User> UserList = new ArrayList<User>();


	/*****************************************/
	/*********** IMPLEMENTATION **************/
	/*****************************************/

	/*
	 * Register.
	 * @see serverfiles.WSInterface#register(java.lang.String)
	 */
	@Override
    @WebMethod
	public boolean register(String nodeID){
		User new_user = new User(nodeID);
		synchronized(UserList){
			// If the user doesn't already exist
			if ( UserList.indexOf(new_user) == -1 ){
				UserList.add(new_user);
				DB_Access.DBregister(nodeID);
				return(true);
			}
			else{
				return(false);
			}
		}
	}

	/*
	 * maliciousPatternRequest
	 * @see serverfiles.WSInterface#maliciousPatternRequest(java.lang.String)
	 */
	@Override
    @WebMethod
    public MaliciousPatterns maliciousPatternRequest(String nodeID){
		User u = new User(nodeID);
		synchronized (UserList) {
			int index = UserList.indexOf(u);
			// If the user is found
			if ( index != -1 ){
				// We get him
				u = UserList.get(index);
				MaliciousPatterns mp = new MaliciousPatterns();
				synchronized(IPfile){
					// We insert into the response all the ips he doesn't know
					mp.setIPs(IPfile.subList(u.getIPposition(), IPfile.size()));
					// and update his number of known ips
					u.increaseIPposition(IPfile.size()-u.getIPposition());
				}
				synchronized(MPfile){
					// We insert into the response all the patterns he doesn't know
					mp.setStrings(MPfile.subList(u.getMPposition(), MPfile.size()));
					// and update his number of known patterns
					u.increaseMPposition(MPfile.size()-u.getMPposition());
				}
				return mp;
			}
			else{
				return null;
			}
		}
	}

	/*
	 * maliciousPatternsStatisticalReport
	 * @see serverfiles.WSInterface#maliciousPatternsStatisticalReport(java.lang.String, serverfiles.WSInterface.StatisticalReports)
	 */
	@Override
	@WebMethod
	public void maliciousPatternsStatisticalReport(String nodeID, StatisticalReports m){

		// We insert the received Reports to the database
		DB_Access.insertReport(nodeID, m.getIPs(), m.getStrings(), m.getInterfaces());

		System.out.println("************************");
		System.out.println("Interfaces:");
		for (String s: m.getInterfaces()){
			System.out.println("Int: " + s);
		}
		System.out.println("IPs:");
		for (String s: m.getIPs()){
			System.out.println("IP: " + s);
		}
		System.out.println("MPs:");
		for (String s: m.getStrings()){
			System.out.println("MP: " + s);
		}
	}

	/*
	 * unregister
	 * @see serverfiles.WSInterface#unregister(java.lang.String)
	 */
	@Override
	@WebMethod
	public boolean unregister(String nodeID){
		User u = new User(nodeID);
		synchronized(UserList){
			int index = UserList.indexOf(u);
			// If the users is found
			if ( index != -1 ){
				u = UserList.remove(index);
				return true;
			}
			else{
				return false;
			}
		}
	}

	//------------------------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * @see serverfiles.WSInterface#registration(java.lang.String, java.lang.String, serverfiles.WSInterface.AvailableNodes)
	 */
	@Override
	@WebMethod
	public boolean registration(String username, String password, AvailableNodes nodes){
		System.out.println("Register: " + username + " " + password);
		for (String s:nodes.getIDList()){
			System.out.println(" av: " + s);
		}
		return DB_Access.android_DBregister(username, password, nodes);
	}

	/*
	 * (non-Javadoc)
	 * @see serverfiles.WSInterface#retrieveStatistics(java.lang.String, java.lang.String)
	 */
	@Override
	@WebMethod
	public StatisticalReports[] retrieveStatistics(String username, String password){
		System.out.println("Retrieve stat: " + username + " " + password);
		proUser user = new proUser(username, password, false, 0);
		proUser user2;
		synchronized (proUserList) {
			int pos = proUserList.indexOf(user);
			if (pos == -1){
				return null;
			}
			user2 = proUserList.get(pos);
		}
		int reportID = user2.getReportCounter();
		ArrayList<StatisticalReports> list = (ArrayList<StatisticalReports>) DB_Access.getStatistics(reportID);
		StatisticalReports r = list.get(list.size()-1);
		user2.setReportCounter(Integer.parseInt(r.getID()));
		list.remove(list.size()-1);
		return list.toArray(new StatisticalReports[list.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see serverfiles.WSInterface#retrieveMaliciousPatterns(java.lang.String, java.lang.String)
	 */
	@Override
	@WebMethod
	public String retrieveMaliciousPatterns(String username,String password){
		System.out.println("Retrieve Pat: " + username + " " + password);
		proUser puser = new proUser(username, password, false, 0);
		synchronized(proUserList){
			// If the user doesn't exist.
			if ( proUserList.indexOf(puser) == -1){
				return(null);
			}
		}
		StringBuffer b = new StringBuffer();
		synchronized (IPfile) {
			for (String f: IPfile){
				b.append(f);
				b.append("\n");
			}
		}
		b.append("####");
		synchronized(MPfile){
			for (String f: MPfile){
				b.append(f);
				b.append("\n");
			}
		}
		return (b.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see serverfiles.WSInterface#insertMaliciousPatterns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@WebMethod
	public void insertMaliciousPatterns(String username,String password, String malliciousIP,String stringPatterns){
		System.out.println("Insert Mal: " + username + " " + password + " " + malliciousIP + " " + stringPatterns);
		proUser puser = new proUser(username, password, false, 0);
		synchronized(proUserList){
			// If the user doesn't exist.
			if ( proUserList.indexOf(puser) == -1){
				return;
			}
		}
		if (malliciousIP != null){
			synchronized(IPfile){
				IPfile.add(malliciousIP);
			}
		}
		if (stringPatterns != null){
			synchronized(MPfile){
				MPfile.add(stringPatterns);
			}
		}
	}

	/**********************/
	/** SERVER FUNCTIONS **/
	/**********************/
	/*
	 * Insert ip to the ip file.
	 */
	public static void insertIP(String ip){
		synchronized(IPfile){
			IPfile.add(ip);
		}
	}

	/*
	 * Insert pattern to the MP file.
	 */
	public static void insertPattern(String p){
		synchronized(MPfile){
			MPfile.add(p);
		}
	}

	/*
	 * This methods returns a string with all the available information
	 * about the IP and MP files and all the active users from the user list.
	 *
	 */
	public static String getInformation(){
		StringBuffer b = new StringBuffer();
		b.append("Users: \n");
		synchronized (UserList) {
			for (User u: UserList){
				b.append(u.toString());
				b.append("->Known ips:");
				synchronized(IPfile){
					for (int i=0;i<u.IPposition;i++){
						b.append(" " + IPfile.get(i));
					}
				}
				b.append("\n->Known strings:");
				synchronized(MPfile){
					for (int i=0;i<u.MPposition;i++){
						b.append(" " + MPfile.get(i));
					}
				}
			}
		}
		b.append("\n");
		b.append("IP file: \n");
		synchronized (IPfile) {
			for (String s: IPfile){
				b.append(s);
				b.append("\n");
			}
		}
		b.append("\n");
		b.append("MP file: \n");
		synchronized (MPfile) {
			for (String s: MPfile){
				b.append(s);
				b.append("\n");
			}
		}
		return(b.toString());

	}

}
