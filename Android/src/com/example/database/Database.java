package com.example.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class Database offers access to the SQLite database stored into
 * the Smartphone/Tablet.
 *
 * @author ��������
 *
 */
public class Database extends SQLiteOpenHelper {
	
	/*
	 * Request class.
	 */
	public class Request{
		
		/*
		 * Request.
		 */
		private String request;
		
		/*
		 * Username.
		 */
		private String username;
		
		/*
		 * Password.
		 */
		private String password;
		
		/*
		 * First argument.
		 */
		private String arg1;
		
		/*
		 * Second argument.
		 */
		private String arg2;
		
		

		public String getRequest() {
			return request;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}

		public String getArg1() {
			return arg1;
		}

		public String getArg2() {
			return arg2;
		}

		public void setRequest(String request) {
			this.request = request;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setArg1(String arg1) {
			this.arg1 = arg1;
		}

		public void setArg2(String arg2) {
			this.arg2 = arg2;
		}
	}
	
	/*
	 * Database version.
	 */
	private static final int DB_VERSION = 4;

	/*
	 * The name of the database.
	 */
	private static String DB_NAME = "user_sqlite";

	/*
	 * Table "reports".
	 */
	private static final String TABLE_USERS = "reports";

	/*
	 * Columns of "reports".
	 */
	private static final String TABLE_USERS_COL_1 = "reportID";
	private static final String TABLE_USERS_COL_2 = "userID";

	/*
	 * Table "maliciousIPs".
	 */
	private static final String TABLE_IPS = "maliciousIPs";

	/*
	 * Columns of "maliciousIPs".
	 */
	private static final String TABLE_IPS_COL_1 = "id";
	private static final String TABLE_IPS_COL_2 = "ip";
	private static final String TABLE_IPS_COL_3 = "int_name";
	private static final String TABLE_IPS_COL_4 = "int_ip";
	private static final String TABLE_IPS_COL_5 = "frequency";

	/*
	 * Table "maliciousPats".
	 */
	private static final String TABLE_PATTERNS = "maliciousPats";

	/*
	 * Columns of "maliciousPats".
	 */
	private static final String TABLE_PATTERNS_COL_1 = "id";
	private static final String TABLE_PATTERNS_COL_2 = "pattern";
	private static final String TABLE_PATTERNS_COL_3 = "int_name";
	private static final String TABLE_PATTERNS_COL_4 = "int_ip";
	private static final String TABLE_PATTERNS_COL_5 = "frequency";

	/*
	 * Table "interfaces".
	 */
	private static final String TABLE_INTERFACES = "interfaces";

	/*
	 * Columns of "interfaces".
	 */
	private static final String TABLE_INTERFACES_COL_1 = "id";
	private static final String TABLE_INTERFACES_COL_2 = "int_name";

	/*
	 * Extra Table "pending_requests".
	 */
	private static final String TABLE_REQUESTS = "pending_requests";

	/*
	 * Columns of "pending_requests".
	 */
	private static final String TABLE_REQUESTS_COL_1 = "request";
	private static final String TABLE_REQUESTS_COL_2 = "username";
	private static final String TABLE_REQUESTS_COL_3 = "password";
	private static final String TABLE_REQUESTS_COL_4 = "first_argument";
	private static final String TABLE_REQUESTS_COL_5 = "second_arument";
	
	/*
	 * Response types.
	 */
	public static final int MALICIOUS_PATTERN_RESPONSE = 1;
	public static final int LOGIN_RESPONSE = 2;
	public static final int LOGOUT_RESPONSE = 3;
	public static final int DELETE_RESPONSE = 4;


	/**
	 * Constructor.
	 * 
	 * @param context
	 */
	public Database(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		Log.d("Database operation", "Database Constructor!");
	}

	/**
	 * Using this method you can set the database that the database
	 * operations refer to.
	 * True for admin, false for user.
	 */
	public static void setMode(boolean mode){
		if (mode){
			DB_NAME = "admin_sqlite";
		}
		else{
			DB_NAME = "user_sqlite";
		}
	}
	
	/**
	 * Creating.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("Database operation", "Creating Database ->");
		// Table users
		String create_table = "CREATE TABLE " + TABLE_USERS + " ( " + 
				TABLE_USERS_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				TABLE_USERS_COL_2 + " varchar(45) NOT NULL)";
		db.execSQL(create_table);
		Log.d("Database operation", "-> Created table " + TABLE_USERS + "!");
		// Table ips
		create_table = "CREATE TABLE " + TABLE_IPS + " ( " + 
				TABLE_IPS_COL_1 + " int(11) NOT NULL," +
				TABLE_IPS_COL_2 + " varchar(45) NOT NULL," +
				TABLE_IPS_COL_3 + " varchar(45) NOT NULL," +
				TABLE_IPS_COL_4 + " varchar(45) NOT NULL," +
				TABLE_IPS_COL_5 + " int(11) DEFAULT NULL," +
				"PRIMARY KEY (" + TABLE_IPS_COL_1 + "," + TABLE_IPS_COL_2 + "," + TABLE_IPS_COL_3 + "," + TABLE_IPS_COL_4 + ")," +
				"CONSTRAINT `reportID` FOREIGN KEY (" + TABLE_IPS_COL_1 + ") REFERENCES " + TABLE_USERS + " (" + TABLE_USERS_COL_1 + ") " +
				"ON DELETE CASCADE ON UPDATE CASCADE)";
		db.execSQL(create_table);
		Log.d("Database operation", "-> Created table " + TABLE_IPS + "!");
		// Table patterns
		create_table = "CREATE TABLE " + TABLE_PATTERNS + " ( " + 
				TABLE_PATTERNS_COL_1 + " int(11) NOT NULL," +
				TABLE_PATTERNS_COL_2 + " varchar(45) NOT NULL," +
				TABLE_PATTERNS_COL_3 + " varchar(45) NOT NULL," +
				TABLE_PATTERNS_COL_4 + " varchar(45) NOT NULL," +
				TABLE_PATTERNS_COL_5 + " int(11) DEFAULT NULL," +
				"PRIMARY KEY (" + TABLE_PATTERNS_COL_1 + "," + TABLE_PATTERNS_COL_2 + "," + TABLE_PATTERNS_COL_3 + "," + TABLE_PATTERNS_COL_4 + ")," +
				"CONSTRAINT `reportID` FOREIGN KEY (" + TABLE_PATTERNS_COL_1 + ") REFERENCES " + TABLE_USERS + " (" + TABLE_USERS_COL_1 + ") " +
				"ON DELETE CASCADE ON UPDATE CASCADE)";
		db.execSQL(create_table);
		Log.d("Database operation", "-> Created table " + TABLE_PATTERNS + "!");
		// Table interfaces
		create_table = "CREATE TABLE " + TABLE_INTERFACES + " ( " +
				TABLE_INTERFACES_COL_1 + " int(11) NOT NULL," +
				TABLE_INTERFACES_COL_2 + " varchar(45) NOT NULL," +
				"PRIMARY KEY (" + TABLE_INTERFACES_COL_1 + "," + TABLE_INTERFACES_COL_2 + ")," +
				"CONSTRAINT `iID` FOREIGN KEY (" + TABLE_INTERFACES_COL_1 + ") REFERENCES " + TABLE_USERS + " (" + TABLE_USERS_COL_1 + ") " +
				"ON DELETE CASCADE ON UPDATE CASCADE)";
		db.execSQL(create_table);
		Log.d("Database operation", "-> Created table " + TABLE_INTERFACES + "!");
		// Table requests
		create_table = "CREATE TABLE " + TABLE_REQUESTS + " ( " + 
				TABLE_REQUESTS_COL_1 + " varchar(45) NOT NULL," +
				TABLE_REQUESTS_COL_2 + " varchar(45) NULL DEFAULT 'null'," +
				TABLE_REQUESTS_COL_3 + " varchar(45) NULL DEFAULT 'null'," +
				TABLE_REQUESTS_COL_4 + " varchar(45) NULL DEFAULT 'null'," +
				TABLE_REQUESTS_COL_5 + " varchar(45) NULL DEFAULT 'null'," +
				"PRIMARY KEY (" + TABLE_REQUESTS_COL_1 + "," + TABLE_REQUESTS_COL_2 + "," + TABLE_REQUESTS_COL_3 + "," + TABLE_REQUESTS_COL_4 + "," + TABLE_REQUESTS_COL_5 + "))";
		db.execSQL(create_table);
		ContentValues values = new ContentValues();
		values.put(TABLE_REQUESTS_COL_1, "returnValues");
		values.put(TABLE_REQUESTS_COL_2, "null");
		values.put(TABLE_REQUESTS_COL_3, "null");
		values.put(TABLE_REQUESTS_COL_4, "null");
		values.put(TABLE_REQUESTS_COL_5, "null");
		// Inserting Row
		db.insert(TABLE_REQUESTS, null, values);
		Log.d("Database operation", "-> Created table " + TABLE_REQUESTS + "!");
	}

	/**
	 * Upgrading.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older tables if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERFACES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_IPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATTERNS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);

		// Create tables again
		onCreate(db);
		Log.d("Database operation", "Database Upgraded");
	}

	/**
	 * Close connection.
	 */
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
		Log.d("Database operation", "Closing Connection ->");
	}

	/*-----------------------------------------*/
	/*----------- DATA OPERATIONS -------------*/
	/*-----------------------------------------*/

	/**
	 * Returns all user IDs.
	 */
	public List<String> getAllIds() {
		List<String> temp = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT DISTINCT " + TABLE_USERS_COL_2 + " FROM " + TABLE_USERS;
		// Execute query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				temp.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		// return list
		return temp;
	}
	
	/**
	 * Returns all interfaces of a specific user.
	 */
	public List<String> getInterfaces(String userID){
		List<String> temp = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT DISTINCT " + TABLE_INTERFACES_COL_2 + " FROM " + TABLE_INTERFACES +
				" WHERE " + TABLE_INTERFACES_COL_1 + " in ( " + 
					"SELECT " + TABLE_USERS_COL_1 + " FROM " + TABLE_USERS +
					" WHERE " + TABLE_USERS_COL_2 + " = \"" + userID + "\"" +
				" )";
		// Execute query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				temp.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		// return list
		return temp;
	}
	
	/**
	 * Returns malicious IP statistics of a given interface of a given PC/Laptop.
	 */
	public String getIPStatistics(String int_name,String userID){
		StringBuilder temp = new StringBuilder();
		// Select All Query
		String selectQuery = "SELECT " + TABLE_IPS_COL_2 + "," + TABLE_IPS_COL_5 + " FROM " + TABLE_IPS + "," + TABLE_USERS +
				" WHERE " + TABLE_IPS_COL_1 + " = " + TABLE_USERS_COL_1 + " and " + TABLE_USERS_COL_2 + " = \"" + userID + "\"" +
				" and " + TABLE_IPS_COL_3 + " = \"" + int_name + "\"";
		// Execute query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to string
		if (cursor.moveToFirst()) {
			do {
				temp.append("IP: " + cursor.getString(0) + " with f= " + cursor.getInt(1) + "\n");
			} while (cursor.moveToNext());
		}
		// return string
		return temp.toString();
	}

	/**
	 * Returns malicious Pattern statistics of a given interface of a given PC/Laptop.
	 */
	public String getPatternStatistics(String int_name,String userID){
		StringBuilder temp = new StringBuilder();
		// Select All Query
		String selectQuery = "SELECT " + TABLE_PATTERNS_COL_2 + "," + TABLE_PATTERNS_COL_5 + " FROM " + TABLE_PATTERNS + "," + TABLE_USERS +
				" WHERE " + TABLE_PATTERNS_COL_1 + " = " + TABLE_USERS_COL_1 + " and " + TABLE_USERS_COL_2 + " = \"" + userID + "\"" + 
				" and " + TABLE_PATTERNS_COL_3 + " = \"" + int_name + "\"";
		// Execute query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to string
		if (cursor.moveToFirst()) {
			do {
				temp.append("Pattern: " + cursor.getString(0) + " with f= " + cursor.getInt(1) + "\n");
			} while (cursor.moveToNext());
		}
		// return string
		return temp.toString();		
	}
	
	/**
	 * Returns info of a given interface of a given PC/Laptop.
	 */
	public String interfaceInfo(String int_name,String userID){
		StringBuilder temp = new StringBuilder();
		// Select All Query
		String selectQuery =
					" SELECT DISTINCT " + TABLE_IPS_COL_4 + " FROM " + TABLE_IPS + "," + TABLE_USERS +
					" WHERE " + TABLE_IPS_COL_1 + " = " + TABLE_USERS_COL_1 + " and " + TABLE_USERS_COL_2 + " = \"" + userID + "\"" +
					" and " + TABLE_IPS_COL_3 + " = \"" + int_name + "\"" +	 
				" UNION " +
					" SELECT DISTINCT " + TABLE_PATTERNS_COL_4 + " FROM " + TABLE_PATTERNS + "," + TABLE_USERS +
					" WHERE " + TABLE_PATTERNS_COL_1 + " = " + TABLE_USERS_COL_1 + " and " + TABLE_USERS_COL_2 + " = \"" + userID + "\"" +
					" and " + TABLE_PATTERNS_COL_3 + " = \"" + int_name + "\"";
		// Execute query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to string
		if (cursor.moveToFirst()) {
			do {
				temp.append("Interface's IP: " + cursor.getString(0) + "\n");
			} while (cursor.moveToNext());
		}
		// return string
		return temp.toString();
	}
	
	/**
	 * Adds a Statistical Report into the database.
	 */
	public void addReport(String userID,ArrayList<String> ips,ArrayList<String> pats,ArrayList<String> ints) {
		SQLiteDatabase db = this.getWritableDatabase();
		// Add to the reports table the userID
		ContentValues values = new ContentValues();
		values.put( TABLE_USERS_COL_2, userID);
		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		// Retrieve the report id
		String query = "SELECT max(" + TABLE_USERS_COL_1 + ") FROM " + TABLE_USERS;
		Cursor cursor = db.rawQuery(query, null);
		int id = -1;
		if (cursor.moveToFirst()) {
			id = cursor.getInt(0);
		}
		//---------------------------------------------------------------------------
		String[] array = null;
		// For every ip report in the array, we insert it as a seperate entry
		for (String s : ips){
			// We split the report into its contents
			array = s.split("[:\t]",8);
			// And we create the new values
			values = new ContentValues();
			values.put(TABLE_IPS_COL_1, id);
			values.put(TABLE_IPS_COL_2, array[1].trim());
			values.put(TABLE_IPS_COL_3, array[3].trim());
			values.put(TABLE_IPS_COL_4, array[5].trim());
			values.put(TABLE_IPS_COL_5, Integer.parseInt(array[7].trim()));
			db.insert(TABLE_IPS, null, values);
		}
		//---------------------------------------------------------------------------
		String pat = null;
		// For every pattern report in the array, we insert it as a seperate entry
		for (String s : pats){
			// We split the report into its contents.First we extract the pattern
			pat = s.split("[\t]",2)[0].split(":",2)[1];
			// then the rest information.
			array = s.split("[\t]",2)[1].split("[:\t]", 6);
			// And we create the new values
			values = new ContentValues();
			values.put(TABLE_PATTERNS_COL_1, id);
			values.put(TABLE_PATTERNS_COL_2, pat);
			values.put(TABLE_PATTERNS_COL_3, array[1].trim());
			values.put(TABLE_PATTERNS_COL_4, array[3].trim());
			values.put(TABLE_PATTERNS_COL_5, Integer.parseInt(array[5].trim()));
			db.insert(TABLE_PATTERNS, null, values);
		}
		//---------------------------------------------------------------------------
		// For every interface in the array, we insert it as a seperate entry
		for (String s : ints){
			// And we create the new values
			values = new ContentValues();
			values.put(TABLE_INTERFACES_COL_1, id);
			values.put(TABLE_INTERFACES_COL_2, s.trim());
			db.insert(TABLE_INTERFACES, null, values);
		}
		//---------------------------------------------------------------------------
		return;
	}
	
	/**
	 * Adds a pending request to the database.
	 */
	public void addRequest(String req,String user,String pass,String arg1,String arg2){
		SQLiteDatabase db = this.getWritableDatabase();
		// We create the new values
		ContentValues values = new ContentValues();
		values.put(TABLE_REQUESTS_COL_1, req);
		values.put(TABLE_REQUESTS_COL_2, user);
		values.put(TABLE_REQUESTS_COL_3, pass);
		values.put(TABLE_REQUESTS_COL_4, arg1);
		values.put(TABLE_REQUESTS_COL_5, arg2);
		// Inserting Row
		db.insert(TABLE_REQUESTS, null, values);
	}
	
	/**
	 * Adds the returned values to the database.
	 */
	public void setReturnValue(String s1,int i){
		if ( (i<1) || (i>4) ){
			return;
		}
		String selectQuery = "UPDATE " + TABLE_REQUESTS +
				" SET " + ( i==1 ? TABLE_REQUESTS_COL_2 : ( i==2 ? TABLE_REQUESTS_COL_3 : (i==3 ? TABLE_REQUESTS_COL_4 : TABLE_REQUESTS_COL_5) ) ) + " = '" + s1 + "'" +
				" WHERE " + TABLE_REQUESTS_COL_1 + " LIKE \"returnValues\" ";
		// Execute query
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(selectQuery);
	}
	
	/**
	 * Retrieve all pending requests.
	 */
	public List<Request> getRequests(){
		List<Request> temp = new ArrayList<Request>();
		Request r;
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_REQUESTS +
				" WHERE " + TABLE_REQUESTS_COL_1 + " NOT LIKE \"noLogin\" AND " + TABLE_REQUESTS_COL_1 + " NOT LIKE \"returnValues\" ";
		// Execute query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				r = new Request();
				r.setRequest(cursor.getString(0));
				r.setUsername(cursor.getString(1));
				r.setPassword(cursor.getString(2));
				r.setArg1(cursor.getString(3));
				r.setArg2(cursor.getString(4));
				temp.add(r);
			} while (cursor.moveToNext());
		}
		selectQuery = "DELETE FROM " + TABLE_REQUESTS +
				" WHERE " + TABLE_REQUESTS_COL_1 + " NOT LIKE \"noLogin\" AND " + TABLE_REQUESTS_COL_1 + " NOT LIKE \"returnValues\" ";
		// Execute query
		db.execSQL(selectQuery);
		// return list
		return temp;
	}
	
	/**
	 * Return a value (if any) stored in the database from the service that scans
	 * for internet connection.
	 */
	public String getStoredValue(int i){
		if ( (i<1) || (i>4) ){
			return null;
		}
		String s;
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_REQUESTS +
				" WHERE " + TABLE_REQUESTS_COL_1 + " LIKE \"returnValues\" ";
		// Execute query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			s = cursor.getString(i);
		}
		else{
			return null;
		}
		selectQuery = "UPDATE " + TABLE_REQUESTS +
				" SET " + ( i==1 ? TABLE_REQUESTS_COL_2 : ( i==2 ? TABLE_REQUESTS_COL_3 : (i==3 ? TABLE_REQUESTS_COL_4 : TABLE_REQUESTS_COL_5) ) ) + " = NULL" +
				" WHERE " + TABLE_REQUESTS_COL_1 + " LIKE \"returnValues\" ";
		// Execute query
		db.execSQL(selectQuery);
		// return list
		return s;
	}
	
	/**
	 * Returns the username and password if it is saved in the database.
	 */
	public List<String> getUserPass(){
		List<String> tmp = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT " + TABLE_REQUESTS_COL_2 + "," + TABLE_REQUESTS_COL_3 + " FROM " + TABLE_REQUESTS +
				" WHERE " + TABLE_REQUESTS_COL_1 + " LIKE \"noLogin\" ";
		// Execute query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			tmp.add(cursor.getString(0));
			tmp.add(cursor.getString(1));
			selectQuery = "DELETE FROM " + TABLE_REQUESTS +
					" WHERE " + TABLE_REQUESTS_COL_1 + " LIKE \"noLogin\"";
			// Execute query
			db.execSQL(selectQuery);
		}
		else{
			return null;
		}
		return tmp;
	}
	
	/**
	 * Checks if there are pending requests.
	 */
	public int isRequestsEmpty(){
		String selectQuery = "SELECT COUNT(*) FROM " + TABLE_REQUESTS +
				" WHERE " + TABLE_REQUESTS_COL_1 + " NOT LIKE \"noLogin\" AND " + TABLE_REQUESTS_COL_1 + " NOT LIKE \"returnValues\" ";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			return ( cursor.getInt(0));
		}
		else{
			return -1;
		}
	}
	
	/**
	 * Print the whole database.
	 */
	public void print(){
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_USERS;
		// Execute query
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Log.d("PRINT USER:",Integer.toString(cursor.getInt(0)) + " " + cursor.getString(1));
			} while (cursor.moveToNext());
		}
		/****************/
		selectQuery = "SELECT * FROM " + TABLE_INTERFACES;
		cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Log.d("PRINT INT:",Integer.toString(cursor.getInt(0)) + " " + cursor.getString(1));
			} while (cursor.moveToNext());
		}
		/****************/
		selectQuery = "SELECT * FROM " + TABLE_IPS;
		cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Log.d("PRINT IP:",Integer.toString(cursor.getInt(0)) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3) + " " + Integer.toString(cursor.getInt(4)));
			} while (cursor.moveToNext());
		}
		/****************/
		selectQuery = "SELECT * FROM " + TABLE_PATTERNS;
		cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Log.d("PRINT PAT:",Integer.toString(cursor.getInt(0)) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3) + " " + Integer.toString(cursor.getInt(4)));
			} while (cursor.moveToNext());
		}
		/****************/
		selectQuery = "SELECT * FROM " + TABLE_REQUESTS;
		cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Log.d("PRINT REQ:",cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4));
			} while (cursor.moveToNext());
		}
	}
	
}
