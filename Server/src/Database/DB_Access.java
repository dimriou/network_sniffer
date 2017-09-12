package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;

import serverfiles.keftedakiaImpl.AvailableNodes;
import serverfiles.WSkeftedakiastoDI.StatisticalReports;

/**
 * DB_Acess: this class gives access to a database. It also offers functions
 * for insertion of users, reports and printing current database data.
 * 
 */
public class DB_Access {

	/*
	 * here the connection is stored in order to use it for operations
	 * on the database.
	 */
	private static volatile Connection connect = null;

	/*
	 * Constructor.
	 */
	private DB_Access(){}

	/*
	 * This method is used to establish the connection to the database.
	 */
	public static void open(String host,String name,String user,String pass){
		// this will load the MySQL driver, each DB has its own driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// setup the connection with the DB.
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + host + "/" + name + "?"	+ "user=" + user +"&password=" + pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	/*
	 * This method registers a user to the database.
	 */
	public static void DBregister(String ID){

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = connect.prepareStatement(
					"INSERT INTO `ergasia`.`users` (`ID`) VALUES (?);");

			// parameters start with 1
			preparedStatement.setString(1, ID);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// If the user already exists means that he was inactive 
			// and now re-registers to the system.
			if (e.getMessage().contains("Duplicate")){
				System.out.println("Already exists!");
			}
			else{
				e.printStackTrace();
			}
		} finally {
			close(preparedStatement);
		}
	}

	/*
	 * This method registers the android user to the database.
	 */
	public static boolean android_DBregister(String username, String password,AvailableNodes av){
		PreparedStatement preparedStatement = null;

		// We insert a new android user.
		try {
			preparedStatement = connect.prepareStatement(
					"INSERT INTO `ergasia`.`android_users` (`username`, `password`, `known_pattern`,`isAdmin`) VALUES (?,?,?,?);");

			// parameters start with 1
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.setInt(3, 0);
			preparedStatement.setBoolean(4, false);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			return false;
		} finally {
			close(preparedStatement);
			
		}

		preparedStatement = null;		
		// We insert the available nodes of the new user.
		ArrayList<String> tmp = av.getIDList();
		for (String t:tmp){
			try {
				preparedStatement = connect.prepareStatement(
						"INSERT INTO `ergasia`.`available_nodes` (`username`, `pc_id`) VALUES (?,?);");

				// parameters start with 1
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, t);
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				// we ignore incorrect values of PC/Laptop ids.
				return true;
			} finally {
				close(preparedStatement);
			}
		}
		return true;
	}

	/*
	 * This method retrieves the "know patterns" indicator of a registered user.
	 */
	public static int getPatternPosition(String username,String password){
		PreparedStatement preparedStatement = null;
		ResultSet resultset = null;
		int pos = -1;
		// Now search for the user and get the stored position.
		try {
			preparedStatement = connect.prepareStatement(
					"SELECT known_pattern FROM ergasia.android_users " +
					"WHERE username = ? AND password = ?;");

			// parameters start with 1
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			resultset = preparedStatement.executeQuery();
			if (resultset.next()){
				pos = resultset.getInt("known_pattern");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement);
		}
		return pos;
	}
	
	/*
	 * Checks if a registered user is administrator.
	 */
	public static boolean isAdmin(String username,String password){
		PreparedStatement preparedStatement = null;
		ResultSet resultset = null;
		boolean result = false;
		// Now search for the user and get the his identity.
		try {
			preparedStatement = connect.prepareStatement(
					"SELECT isAdmin FROM ergasia.android_users " +
					"WHERE username = ? AND password = ?;");

			// parameters start with 1
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			resultset = preparedStatement.executeQuery();
			if (resultset.next()){
				Object temporary = resultset.getObject("isAdmin");
				result = ( (Boolean)temporary ).booleanValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement);
		}
		return result;
	}
	
	/*
	 * This method sets the last known pattern position of the specified user.
	 */
	public static void setPatternPosition(String username,String password,int value){
		PreparedStatement preparedStatement = null;

		// We update the android user pattern position.
		try {
			preparedStatement = connect.prepareStatement(
					"UPDATE `ergasia`.`android_users` SET `known_pattern`= ?" +
					" WHERE `username`= ? and`password`= ? ;");

			// parameters start with 1
			preparedStatement.setInt(1, value);
			preparedStatement.setString(2, username);
			preparedStatement.setString(3, password);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement);
		}
	}
	
	/*
	 * This method creates a List of the reports the given user 
	 * is not aware of.
	 */
	public static List<StatisticalReports> getStatistics(int reportID){
		List<StatisticalReports> list = new ArrayList<StatisticalReports>();
		StatisticalReports report;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultset2 = null;
		int reportid = 0;
		String id;
		ArrayList<String> ips,pats,ints;
		
		//-------------Reports------------------
		try {
			preparedStatement2 = connect.prepareStatement(
					"SELECT * FROM ergasia.reports " + 
					" WHERE report_ID > ?" + 
					" ORDER BY report_ID ASC;");

			preparedStatement2.setInt(1, reportID);
			resultset2 = preparedStatement2.executeQuery();
			// For every result, insert into the vector
			while (resultset2.next()) {
				PreparedStatement preparedStatement = null;
				ResultSet resultset = null;
				reportid = resultset2.getInt(1);
				id = resultset2.getString(2);
				report = new StatisticalReports(id);
				ips = new ArrayList<String>();
				pats = new ArrayList<String>();
				ints = new ArrayList<String>();
				/*******************************************/
				/*******************************************/
				//----------------IPS----------------
				try {
					preparedStatement = connect.prepareStatement(
							"SELECT * FROM ergasia.ip_reports " +
							" WHERE report_ID = ?");

					preparedStatement.setInt(1, reportid);
					resultset = preparedStatement.executeQuery();
					// For every result, insert into the vector
					while (resultset.next()) {		
						String entry = "Malicious data: " + resultset.getString(2) +
								" \tInterface Name: " + resultset.getString(3) +
								" \tInterface IP: "+ resultset.getString(4) + 
								" \tFrequency: " + resultset.getInt(5);
						ips.add(entry);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					close(preparedStatement);
				}
				report.setIPs(ips);
				//------------Patterns----------------
				try {
					preparedStatement = connect.prepareStatement(
							"SELECT * FROM ergasia.pattern_reports " +
							" WHERE report_ID = ?");

					preparedStatement.setInt(1, reportid);
					resultset = preparedStatement.executeQuery();
					// For every result, insert into the vector
					while (resultset.next()) {		
						String entry = "Malicious data: " + resultset.getString(2) +
								" \tInterface Name: " + resultset.getString(3) +
								" \tInterface IP: "+ resultset.getString(4) + 
								" \tFrequency: " + resultset.getInt(5);
						pats.add(entry);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					close(preparedStatement);
				}
				report.setStrings(pats);
				//-----------Interfaces----------------
				try {
					preparedStatement = connect.prepareStatement(
							"SELECT * FROM ergasia.interfaces " + 
							" WHERE report_ID = ?");

					preparedStatement.setInt(1, reportid);
					resultset = preparedStatement.executeQuery();
					// For every result, insert into the vector
					while (resultset.next()) {		
						ints.add(resultset.getString(2));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					close(preparedStatement);
				}
				report.setInterfaces(ints);
				/*******************************************/
				/*******************************************/
				list.add(report);
			}
			// Adding a blank statistical report as counter.
			report = new StatisticalReports(Integer.toString(reportid));
			list.add(report);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement2);
		}
		return list;
	}
	
	/*
	 * This method erases a PC/laptop from the database.
	 */
	public static boolean delete(String nodeID){
		PreparedStatement preparedStatement2 = null;
		ResultSet resultset2 = null;
		int reportid;
		
		//-------------Reports------------------
		try {
			preparedStatement2 = connect.prepareStatement(
					"SELECT report_ID FROM ergasia.reports " + 
					" WHERE user_ID = ?");

			preparedStatement2.setString(1, nodeID);
			resultset2 = preparedStatement2.executeQuery();
			// For every result, insert into the vector
			while (resultset2.next()) {
				PreparedStatement preparedStatement = null;
				reportid = resultset2.getInt(1);
				/*******************************************/
				/*******************************************/
				//----------------IPS----------------
				try {
					preparedStatement = connect.prepareStatement(
							"DELETE FROM `ergasia`.`ip_reports` " +
							" WHERE report_ID = ?");

					preparedStatement.setInt(1, reportid);
					preparedStatement.executeUpdate();
				} catch (Exception e) {
					return false;
				} finally {
					close(preparedStatement);
				}
				//------------Patterns----------------
				try {
					preparedStatement = connect.prepareStatement(
							"DELETE FROM `ergasia`.`pattern_reports` " +
							" WHERE report_ID = ?");

					preparedStatement.setInt(1, reportid);
					preparedStatement.executeUpdate();
				} catch (Exception e) {
					return false;
				} finally {
					close(preparedStatement);
				}
				//-----------Interfaces----------------
				try {
					preparedStatement = connect.prepareStatement(
							"DELETE FROM `ergasia`.`interfaces` " +
							" WHERE report_ID = ?");

					preparedStatement.setInt(1, reportid);
					preparedStatement.executeUpdate();
				} catch (Exception e) {
					return false;
				} finally {
					close(preparedStatement);
				}
				/*******************************************/
				/*******************************************/
			}
		} catch (Exception e) {
			return false;
		} finally {
			close(preparedStatement2);
		}
		try {
			preparedStatement2 = connect.prepareStatement(
					"DELETE FROM `ergasia`.`reports` " +
					" WHERE user_ID = ?");

			preparedStatement2.setString(1, nodeID);
			preparedStatement2.executeUpdate();
		} catch (Exception e) {
			return false;
		} finally {
			close(preparedStatement2);
		}
		try {
			preparedStatement2 = connect.prepareStatement(
					"DELETE FROM `ergasia`.`available_nodes` " +
					" WHERE pc_id = ?");

			preparedStatement2.setString(1, nodeID);
			preparedStatement2.executeUpdate();
		} catch (Exception e) {
			return false;
		} finally {
			close(preparedStatement2);
		}
		try {
			preparedStatement2 = connect.prepareStatement(
					"DELETE FROM `ergasia`.`users` " +
					" WHERE ID = ?");

			preparedStatement2.setString(1, nodeID);
			preparedStatement2.executeUpdate();
		} catch (Exception e) {
			return false;
		} finally {
			close(preparedStatement2);
		}
		return true;
	}
	
	/*
	 * This method is used to insert a malicious report into the database.
	 */
	public static void insertReport(String ID,ArrayList<String> ips,ArrayList<String> patterns,ArrayList<String> intfs){

		PreparedStatement preparedStatement = null;
		ResultSet resultset = null;

		// We insert a new report
		try {
			preparedStatement = connect.prepareStatement(
					"INSERT INTO `ergasia`.`reports` (`user_ID`) VALUES (?);");

			// parameters start with 1
			preparedStatement.setString(1, ID);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement);
		}
		int rID = 0;
		// Now search for the new report ID that was assigned to the report,
		// because the database auto increments this value(so we dont know it).
		try {
			preparedStatement = connect.prepareStatement(
					"SELECT max(report_ID) AS maximum FROM ergasia.reports "+
					"WHERE user_ID = ? ");

			// parameters start with 1
			preparedStatement.setString(1, ID);
			resultset = preparedStatement.executeQuery();
			resultset.next();
			rID = resultset.getInt("maximum");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement);
		}
		//---------------------------------------------------------------------------
		String[] array = null;
		// For every ip report in the array, we insert it as a seperate entry
		for (String s : ips){
			// We split the report into its contents
			array = s.split("[:\t]",8);
			try {
				preparedStatement = connect.prepareStatement(
						"INSERT INTO `ergasia`.`ip_reports` (`report_ID`, `ip`, `int_name`, `int_ip`, `frequency`)"+
						"VALUES (?,?,?,?,?);");

				// parameters start with 1
				preparedStatement.setInt(1, rID);
				preparedStatement.setString(2, array[1].trim());
				preparedStatement.setString(3, array[3].trim());
				preparedStatement.setString(4, array[5].trim());
				preparedStatement.setInt(5, Integer.parseInt(array[7].trim()));
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				System.out.println("bbb " + s);
				for (String ss:array){
					System.out.println(ss);
				}
				e.printStackTrace();
			} finally {
				close(preparedStatement);
			}
		}
		//---------------------------------------------------------------------------
		String pat = null;
		// For every pattern report in the array, we insert it as a seperate entry
		for (String s : patterns){
			// We split the report into its contents.First we extract the pattern
			pat = s.split("[\t]",2)[0].split(":",2)[1];
			// then the rest information.
			array = s.split("[\t]",2)[1].split("[:\t]", 6);
			try {
				preparedStatement = connect.prepareStatement(
						"INSERT INTO `ergasia`.`pattern_reports` (`report_ID`, `pattern`, `int_name`, `int_ip`, `frequency`)"+
						"VALUES (?,?,?,?,?);");

				// parameters start with 1
				preparedStatement.setInt(1, rID);
				preparedStatement.setString(2, pat);
				preparedStatement.setString(3, array[1].trim());
				preparedStatement.setString(4, array[3].trim());
				preparedStatement.setInt(5, Integer.parseInt(array[5].trim()));
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(preparedStatement);
			}
		}
		//---------------------------------------------------------------------------
		// For every interface in the array, we insert it as a seperate entry
		for (String s : intfs){
			try {
				preparedStatement = connect.prepareStatement(
						"INSERT INTO `ergasia`.`interfaces` (`report_ID`, `name`) VALUES ( ? , ? );");


				// parameters start with 1
				preparedStatement.setInt(1, rID);
				preparedStatement.setString(2, s.trim());
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(preparedStatement);
			}
		}
		return;
	}

	/*
	 * This method reads all the data contained in the database.
	 */
	public static JTable[] getAllDBdata(){

		PreparedStatement preparedStatement = null;
		ResultSet resultset = null;

		// We insert the data directly into JTables.
		JTable[] v = new JTable[5];
		Vector<Vector<String>> rowdata = null;
		Vector<String> row = null;
		Vector<String> columns = null;
		//--------------USERS----------------
		rowdata = new Vector<Vector<String>>();
		try {
			preparedStatement = connect.prepareStatement(
					"SELECT * FROM ergasia.users;");

			resultset = preparedStatement.executeQuery();
			// For every result, insert into the vector
			while (resultset.next()) {		
				row = new Vector<String>();
				row.add(resultset.getString(1));
				rowdata.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement);
		}
		columns = new Vector<String>();
		columns.add("User ID");
		// Now insert the resulting vector into the table
		v[0] = new JTable(rowdata,columns);
		//-------------Reports------------------
		rowdata = new Vector<Vector<String>>();
		try {
			preparedStatement = connect.prepareStatement(
					"SELECT * FROM ergasia.reports;");

			resultset = preparedStatement.executeQuery();
			// For every result, insert into the vector
			while (resultset.next()) {		
				row = new Vector<String>();
				row.add(Integer.toString(resultset.getInt(1)));
				row.add(resultset.getString(2));
				rowdata.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement);
		}
		columns = new Vector<String>();
		columns.add("Report ID");
		columns.add("User ID");
		// Now insert the resulting vector into the table
		v[1] = new JTable(rowdata,columns);
		//----------------IPS----------------
		rowdata = new Vector<Vector<String>>();
		try {
			preparedStatement = connect.prepareStatement(
					"SELECT * FROM ergasia.ip_reports;");

			resultset = preparedStatement.executeQuery();
			// For every result, insert into the vector
			while (resultset.next()) {		
				row = new Vector<String>();
				row.add(Integer.toString(resultset.getInt(1)));
				row.add(resultset.getString(2));
				row.add(resultset.getString(3));
				row.add(resultset.getString(4));
				row.add(Integer.toString(resultset.getInt(5)));
				rowdata.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement);
		}
		columns = new Vector<String>();
		columns.add("Report ID");
		columns.add("Malicious IP");
		columns.add("Interface name");
		columns.add("Interface IP");
		columns.add("Frequency");
		// Now insert the resulting vector into the table
		v[2] = new JTable(rowdata,columns);
		//------------Patterns----------------
		rowdata = new Vector<Vector<String>>();
		try {
			preparedStatement = connect.prepareStatement(
					"SELECT * FROM ergasia.pattern_reports;");

			resultset = preparedStatement.executeQuery();
			// For every result, insert into the vector
			while (resultset.next()) {		
				row = new Vector<String>();
				row.add(Integer.toString(resultset.getInt(1)));
				row.add(resultset.getString(2));
				row.add(resultset.getString(3));
				row.add(resultset.getString(4));
				row.add(Integer.toString(resultset.getInt(5)));
				rowdata.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement);
		}
		columns = new Vector<String>();
		columns.add("Report ID");
		columns.add("Malicious Pattern");
		columns.add("Interface name");
		columns.add("Interface IP");
		columns.add("Frequency");
		// Now insert the resulting vector into the table
		v[3] = new JTable(rowdata,columns);
		//-----------Interfaces----------------
		rowdata = new Vector<Vector<String>>();
		try {
			preparedStatement = connect.prepareStatement(
					"SELECT * FROM ergasia.interfaces;");

			resultset = preparedStatement.executeQuery();
			// For every result, insert into the vector
			while (resultset.next()) {		
				row = new Vector<String>();
				row.add(Integer.toString(resultset.getInt(1)));
				row.add(resultset.getString(2));
				rowdata.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(preparedStatement);
		}
		columns = new Vector<String>();
		columns.add("Report ID");
		columns.add("Interface name");
		// Now insert the resulting vector into the table
		v[4] = new JTable(rowdata,columns);
		//-----------------------------------
		return v;
	}

	/*
	 * This method closes the connection.
	 */
	public static void close() {
		close(connect);
	}

	/*
	 * Helper method for closure.
	 */
	private static void close(AutoCloseable c) {
		try {
			if (c != null) {
				c.close();
				c = null;
			}
		} catch (Exception e) {
			// exception!
		}
	}

} 
