// Just a simple template view class fot creating views
// Its good if every other views implement have this format

// packages
package presentation;

// imports
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ViewChoose extends JFrame {

	// reference to view controller
	private ViewController m_viewController;

	// declare widgets variables
	JPanel panel;

	JButton okButton;
	JButton cancelButton;

	JComboBox difficultyComboBox;
	JComboBox roleComboBox;

	// construtor
	/** instanciates choose objective code view
	 *  @pre: 
	 *  @post: instanciates this view and saves instance of viewcontroller 
	 */
	public ViewChoose (ViewController p_viewController)
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

		// create buttons
		okButton   = new JButton ("Ok");
		cancelButton = new JButton ("Cancel");

		okButton.setAlignmentX (Component.CENTER_ALIGNMENT);
		cancelButton.setAlignmentX (Component.CENTER_ALIGNMENT);

		// combox box
		String[] dif = {"easy", "medium", "hard"};
		String[] rol = {"codebreaker", "codemaker"};

		difficultyComboBox = new JComboBox (dif);
		roleComboBox = new JComboBox (rol);

		difficultyComboBox.setAlignmentX (Component.CENTER_ALIGNMENT);
		roleComboBox.setAlignmentX (Component.CENTER_ALIGNMENT);

		panel.add (Box.createVerticalGlue());

		panel.add (difficultyComboBox);
		panel.add (Box.createRigidArea(new Dimension(0, 15)));
		panel.add (roleComboBox);
		panel.add (Box.createRigidArea(new Dimension(0, 15)));

		panel.add (okButton);
		panel.add (Box.createRigidArea(new Dimension(0, 15)));
		panel.add (cancelButton);
		panel.add (Box.createRigidArea(new Dimension(0, 15)));

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

	// --------sync functions to events------------------------
	/** adds listeners
	 *  @pre: 
	 *  @post: adds listeners
	 */
	private void addListeners()
	{
		okButton.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						actionPerformed_ok (event);
					}
				});
		cancelButton.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						actionPerformed_cancel (event);
					}
				});
	}

	// ----------methods called on by events ------------------
	public void actionPerformed_ok (ActionEvent event)
	{
		m_viewController.createNewGame (roleComboBox.getSelectedItem().toString(), difficultyComboBox.getSelectedItem().toString());
	}
	public void actionPerformed_cancel (ActionEvent event)
	{
		m_viewController.sincronize_viewChoose_to_viewMainMenu();
	}
}
