// Just a simple template view class fot creating views
// Its good if every other views implement have this format

// packages
package presentation;

// imports
import model.Record;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewShowScore extends JFrame {

	// reference to view controller
	private ViewController m_viewController;

	// declare widgets variables
	JMenuBar menubar;
	JPanel panel;

	JButton returnButton;

	JTable scoreTable;
	JScrollPane scoreScrollPane;

	// construtor
	/** instanciates show score view
	 *  @pre: 
	 *  @post: instanciates this view and saves instance of viewcontroller 
	 */
	public ViewShowScore (ViewController p_viewController)
	{
		m_viewController = p_viewController;

		initUI();
		addListeners();
	}

	/** setups ui
	 *  @pre: 
	 *  @post: setups ui for this view
	 */
	private void initUI()
	{
		panel = new JPanel();
		panel.setLayout (new BoxLayout (panel, BoxLayout.Y_AXIS));
		add (panel);

		panel.setBorder(new EmptyBorder(new Insets(40, 60, 40, 60)));
		
		// create table;
		scoreTable = new JTable ();
		scoreScrollPane = new JScrollPane (scoreTable);

		panel.add (scoreScrollPane);

		// create buttons
		returnButton   = new JButton ("Return");
		returnButton.setAlignmentX (Component.CENTER_ALIGNMENT);

		panel.add (Box.createRigidArea(new Dimension(0, 20)));
		panel.add (returnButton);
		panel.add (Box.createRigidArea(new Dimension(0, 20)));

		// set window size and quit event
		setTitle ("MasterMind");
		setMinimumSize (new Dimension (500, 500));
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setLocationRelativeTo (null);
	}

	// -----------enabling and disabling functions --------
	/** Enable this view
	 *  @pre: 
	 *  @post: this view is enables and visible
	 */
	public void makeVisible()
	{
		setEnabled (true);
		setVisible (true);
	}

	/** Disable this view
	 *  @pre: 
	 *  @post: this view is disabled and invisible
	 */
	public void makeInvisible()
	{
		setEnabled (false);
		setVisible (false);
	}

	/** displays the data on the table
	 *  @pre: preData is a list of records
	 *  @post: preData is displayed on the table
	 */
	public void setData (ArrayList<Record> preData)
	{
		DefaultTableModel model = (DefaultTableModel) scoreTable.getModel();
		String[] data = new String[4];
		model.setRowCount(0);

		String[] col = {"Name", "Score", "Role", "Date"};
		model.setColumnIdentifiers(col);
		for (int i = 0; i < preData.size(); ++i) {
			Record curr = preData.get(i);
			data[0] = curr.getPlayerName().toString();
			data[1] = curr.getScore().toString();
			data[2] = curr.getRole().toString();
			data[3] = curr.getDate().toString();
			model.addRow(data);
		}
	}

	/** appends a record to the records displayed on the table
	 *  @pre: 
	 *  @post: the record in appended on the table
	 */
	public void setLastEntry(Record r) {
		DefaultTableModel model = (DefaultTableModel) scoreTable.getModel();
		String[] data = new String[4];
		data[0] = r.getPlayerName().toString();
		data[1] = r.getScore().toString();
		data[2] = r.getRole().toString();
		data[3] = r.getDate().toString();
		model.addRow(data);
 	}

	// --------sync functions to events------------------------
	private void addListeners()
	{
		returnButton.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						actionPerformed_return (event);
					}
				});
	}

	// ----------methods called on by events ------------------
	public void actionPerformed_return (ActionEvent event)
	{
		m_viewController.sincronize_viewShowScore_to_viewMainMenu();
	}
}
