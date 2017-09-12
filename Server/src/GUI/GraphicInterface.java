package GUI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import main.Mainserver;
import serverfiles.keftedakiaImpl;
import Database.DB_Access;

/**
 * This class implements our graphical interface.
 */
public class GraphicInterface extends JFrame implements ActionListener{

	/*
	 * Neccessary UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * JLabels used in GUI.
	 */
	private JLabel mainLabel, MLabel, SLabel;
	
	/*
	 * Three buttons.
	 */
	private JButton click1,click2,click3;


	/*
	 * Constructor.
	 */
	public GraphicInterface(){

		//Initialize Layout.
		setLayout(null);
		setSize(600,400);
		setTitle("GUI");

		//Create new Labels and Buttons.
		mainLabel = new JLabel ("Hello admin!Choose the operation you want to proceed to :)  ");
		MLabel = new JLabel("Add Mallicious IP's/Patterns: ");
		SLabel = new JLabel("Get Network Statistics: ");
		click1 = new JButton("IP");
		click2 = new JButton("Statistics");
		click3 = new JButton("Pattern");


		//Position the contents of Layout.
		mainLabel.setBounds(90,30,500,100);
		MLabel.setBounds(75,200,230,30);
		SLabel.setBounds(360,200,200,30);
		click1.setBounds(120,245,100,30);
		click2.setBounds(385,260,105,30);
		click3.setBounds(120, 285, 100, 30);

		//Add ActionListeners for the 3 option Buttons.
		click1.addActionListener(this);
		click2.addActionListener(this);
		click3.addActionListener(this);

		// add them to the frame
		add(MLabel);
		add(SLabel);
		add(click1);
		add(click2);
		add(click3);
		add(mainLabel);

		// We add a listener waiting for the window to close.
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				
				// Before the window closes
				synchronized (Mainserver.class) {

					// We set running to false
					Mainserver.isRunning = false;
					// ...and wake mainserver thread up
					Mainserver.class.notify();
				}

			}

		});



		//Exit GUI when (x) Button is pressed.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setResizable(true);	
		setVisible(true);
	}

	/*
	 * We set an action listener, waiting for buttons to be pressed.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e ){
		// If the first button was pressed
		if( e.getSource() == click1 ) {
			// A dialog prompts the user to insert ip
			String input = null;
			input = JOptionPane.showInputDialog("Hello! now enter malicious IP");
			if(input != null && !input.isEmpty() ){
				// If not empty, insert ip to the system
				keftedakiaImpl.insertIP(input);
			}

		}
		// If the third button was pressed
		if( e.getSource() == click3 ) {
			// A dialog prompts the user to insert pattern
			String input = null;
			input = JOptionPane.showInputDialog("Hello! now enter malicious Pattern");
			if(input != null && !input.isEmpty()  ){
				// If not empty, insert pattern to the system
				keftedakiaImpl.insertPattern(input);
			}

		}
		// If the second button was pressed
		if( e.getSource() == click2 ) {
			// We create a sub window
			Window w;
			// The new window needs the database data in order to print them
			w = new Window(DB_Access.getAllDBdata());
			w.getTitle();
		}
	}

}
