package GUI;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import serverfiles.keftedakiaImpl;
import Database.DB_Access;

/**
 * 
 * Window implements the sub window that prints all information
 * (database contents, ip and patterns inserted into the system, 
 * active users)
 * 
 */
public class Window extends JFrame implements ActionListener{

	/*
	 * Neccessary(HA!) UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * Button.
	 */
	private JButton button = null;
	
	/*
	 * GridBag layout needs constraints.
	 */
	private GridBagConstraints c = null;
	
	/*
	 * A container that all elements are stored into.
	 */
	private Container pane = null;
	
	/*
	 * A text area to print information.
	 */
	private JTextArea userText = null;
	
	/*
	 * The text area above is contained here so that
	 * it is scrollable.
	 */
	private JScrollPane text = null;
	
	/*
	 * This vector contains all database tables that are also scrollable.
	 */
	private Vector<JScrollPane> JVector;


	/*
	 * This method is used to add components to a panel.
	 */
	private void addComponentsToPane(JTable[] array){

		// We set the layout options.
		pane.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;


		
		//adding text area
		userText= new JTextArea(keftedakiaImpl.getInformation());	
		text = new JScrollPane(userText);
		// We now create a text area
		AreaPanel();

		//adding database panel
		int counter = BasePanel(array);



		//adding refresh button
		button = new JButton("REFRESH");
		c.gridx = counter;
		c.gridy = 3;
		c.ipady = 0;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(0,100,0,0);
		// now we add it
		pane.add(button, c);
		// and we add a button listener
		button.addActionListener(this);
	}

	/*
	 * JTextArea initialization method
	 */
	private void AreaPanel(){
		text.updateUI();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 10;
		c.gridwidth = 5;
		c.ipady = 40;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0,0,0,0);
		pane.add(text, c);	
	}

	/*
	 * Methode used to initialize the panel used to show DataBase 
	 */
	private int BasePanel(JTable[] array){
		int counter = 0;
		
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 10;
		c.weightx = 0.7;
		c.ipady = 90;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0,0,0,0);
		// For every table in the array we insert it in the vector
		for(JTable t:array){
			JVector.addElement(new JScrollPane(t));
			JVector.lastElement().setVisible(true);
			JVector.lastElement().setAutoscrolls(true);
			JVector.lastElement().setPreferredSize(new Dimension(100, 100));
			JVector.lastElement().setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			JVector.lastElement().getHorizontalScrollBar().setUnitIncrement(50);
			pane.add(JVector.lastElement(), c);
			c.gridx ++;
			counter ++;
		}
		return counter-1;
	}

	/*
	 * Constructor.
	 */
	public Window(JTable[] array){
		// We create a new JFrame
		JFrame frame = new JFrame("Statistics");
		pane = frame.getContentPane();
		// Create new vector
		JVector = new Vector<JScrollPane>(5);
		// and fill it.
		addComponentsToPane(array);

		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// When the refresh button is hit, we iterate over the vector and remove the items
		 for(int index = 4; index >= 0; index--){
			 try {
				 pane.remove(JVector.elementAt(index));	
				 JVector.removeElementAt(index);
				 pane.revalidate();
				 pane.repaint(); 
			 } catch (ArrayIndexOutOfBoundsException exc) {}
		 }
		//Create new Labels and Buttons.

		BasePanel(DB_Access.getAllDBdata());
		
		//Make the vector re-appear.
		pane.revalidate(); 
		pane.repaint();
		// Now replace text area
		userText.replaceRange(keftedakiaImpl.getInformation(), 0, userText.getText().length());
		// Now update text information
		AreaPanel();
		text.revalidate();
		text.repaint();
	}

}
