package main;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.ws.Endpoint;

import serverfiles.keftedakiaImpl;
import Database.DB_Access;
import GUI.GraphicInterface;

/**
 * Mainserver: implements the main functionality of the server.
 *  That is, it starts a web service server, it establishes connection to the database,
 * 	initiates a gui for the admin.
 * 
 * 
 * @author dimitris
 *
 */
public class Mainserver {
	
	/*
	 * isRunning: variable used as flag in order for the main method to check 
	 * if it should proceed to cleanup and then exit.
	 */
	public static volatile boolean isRunning = true ;  

	/**
	 * Main method.
	 * @param args
	 */
	public static void main(String[] args) {
		/**************************/
		/**  READ PROPERTY FILE  **/
		/**************************/
		// new reader for the configuration file
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader("Configuration"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String IP = null,port = null,DBname = null,DBusername = null,DBpass = null,DBhost = null;
		try {
			// we use the first line as a check if the file is null 
			// (the first line is not important)
			if ( file.readLine() != null)
			{
				// next line is the server IP
				IP = file.readLine();
				// next line is the server port
				port = file.readLine();
				// next line is the database IP or domain name
				DBhost = file.readLine();
				// next line is the database name
				DBname = file.readLine();
				// next line is the user name to use 
				DBusername = file.readLine();
				// next line is the password to use
				DBpass = file.readLine();
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		finally{
			try {
				// We close the file
				file.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		/**************************/
		/** ESTABLISH CONNECTION **/
		/**************************/
		// We initiate the database connection
		DB_Access.open(DBhost,DBname,DBusername,DBpass);
		/**************************/
		/**       PUBLISH        **/
		/**************************/
		// publish the service
		Endpoint ep = Endpoint.create(new keftedakiaImpl());
		ep.publish("http://" + IP + ":" +  port +"/WSadder/");
		// http://192.168.0.3:8081/WSadder/WSkeftedakiastoDI?WSDL
		System.out.println("Started");
		System.out.println("");
		/**************************/
		/**         GUI          **/
		/**************************/
		// create new GUI
		GraphicInterface gui = new GraphicInterface();
		gui.setFont(new Font("Verdana", Font.BOLD, 12));
		synchronized(Mainserver.class){
			// While the method is in a state that has to continue running.
			while(isRunning){
				try {
					// Wait until the shutdown hook notifies you.
					Mainserver.class.wait();
				} catch (InterruptedException e) {
					System.out.println("Error at: wait()");
					e.printStackTrace();
				}
			}
		}
		
		/**************************/
		/**   SHUTDOWN OPTIONS   **/
		/**************************/
		// We offer some final shutdown options(print and exit).
		int choice = 0;
		Scanner in = new Scanner(System.in);
		
		while(choice!=2){
			System.out.println("Remaining server options:");
			System.out.println("1.print");
			System.out.println("2.stop server");
			choice = in.nextInt();
			if (choice == 1){
				// if the first choice was chosen then we print information
				System.out.println(keftedakiaImpl.getInformation());
			}
			System.out.println("------------------------------------------------");
		}
		in.close();
		/**************************/
		/**        CLEANUP       **/
		/**************************/
		// stop publishing the service
		ep.stop();
		System.out.println("Stopped");
		// Close connection to database
		DB_Access.close();
	}

}
