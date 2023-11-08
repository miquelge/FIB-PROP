// Just a simple template view class fot creating views
// Its good if every other views implement have this format

// packages
package presentation;

// imports
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class ViewMainMenu extends JFrame {

	// reference to view controller
	private ViewController m_viewController;

	// declare widgets variables
	private JMenuBar menubar;
	private JPanel panel;
	private JLabel logo;
	private JButton newGameButton;
	private JButton showScoreButton;
	private JButton loadGameButton;

	// construtor
	/** instanciates main menu view
	 *  @pre: 
	 *  @post: instanciates this view and saves instance of viewcontroller 
	 */
	public ViewMainMenu (ViewController p_viewController)
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
		// initialize menu bar
		initMenuBar();

		panel = new JPanel();
		panel.setLayout (new BoxLayout (panel, BoxLayout.Y_AXIS));
		add (panel);

		panel.setBorder(new EmptyBorder(new Insets(40, 60, 40, 60)));

		logo = new JLabel();
		logo.setIcon(new ImageIcon("./images/logo.png"));

		Border border = LineBorder.createGrayLineBorder();
		logo.setBorder(border);

		// create buttons
		newGameButton   = new JButton ("New Game");
		showScoreButton = new JButton ("Show Score");
		loadGameButton  = new JButton ("Load Game");

		logo.setAlignmentX(Component.CENTER_ALIGNMENT);
		newGameButton.setAlignmentX (Component.CENTER_ALIGNMENT);
		showScoreButton.setAlignmentX (Component.CENTER_ALIGNMENT);
		loadGameButton.setAlignmentX (Component.CENTER_ALIGNMENT);

		panel.add (Box.createVerticalGlue());
		panel.add(logo);
		panel.add (Box.createRigidArea(new Dimension(0, 80)));
		panel.add (newGameButton);
		panel.add (Box.createRigidArea(new Dimension(0, 15)));
		panel.add (showScoreButton);
		panel.add (Box.createRigidArea(new Dimension(0, 15)));
		panel.add (loadGameButton);
		panel.add(Box.createRigidArea(new Dimension(0, 15)));

		// set window size and quit event
		setTitle ("MasterMind");
		setMinimumSize (new Dimension (500, 500));
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setLocationRelativeTo (null);
	}

	/** initializes main menu 
	 *  @pre: 
	 *  @post: initializes menu bar
	 */
	private void initMenuBar()
	{
		menubar = new JMenuBar();
		setJMenuBar (menubar);

		JMenu gameMenu  = new JMenu ("Game");
		JMenu helpMenu  = new JMenu ("Help");
		JMenu aboutMenu = new JMenu ("About");

		menubar.add (gameMenu);
		menubar.add (helpMenu);
		menubar.add (aboutMenu);

		JMenuItem exitMenuItem = new JMenuItem ("Exit");
		gameMenu.add (exitMenuItem);
		JMenuItem howToPlayMenuItem = new JMenuItem ("How to Play");
		helpMenu.add (howToPlayMenuItem);
		JMenuItem aboutMenuItem = new JMenuItem ("About software");
		aboutMenu.add (aboutMenuItem);

		aboutMenuItem.addActionListener ((ActionEvent event) ->
				{
					ViewAbout va = new ViewAbout();
					va.makeVisible();
				});
		howToPlayMenuItem.addActionListener ((ActionEvent event) ->
				{
					ViewHelp vh = new ViewHelp();
					vh.makeVisible();
				});
		exitMenuItem.addActionListener ((ActionEvent event) ->
				{
					System.exit(0);
				});
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
	private void addListeners()
	{
		newGameButton.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						actionPerformed_newGame (event);
					}
				});
		showScoreButton.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						actionPerformed_showScore (event);
					}
				});
		loadGameButton.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						actionPerformed_loadGame (event);
					}
				});
	}

	// ----------methods called on by events ------------------
	public void actionPerformed_newGame (ActionEvent event)
	{
		m_viewController.sincronize_viewMainMenu_to_viewChoose();
	}
	public void actionPerformed_showScore (ActionEvent event)
	{
		m_viewController.sincronize_viewMainMenu_to_viewShowScore();
	}
	public void actionPerformed_loadGame (ActionEvent event)
	{
		JFileChooser c = new JFileChooser();
		c.setCurrentDirectory(new java.io.File("./SavedFiles"));
		int rVal = c.showOpenDialog (this);
		if (rVal == JFileChooser.APPROVE_OPTION) 
		{
			System.out.println ("Loading File: " + c.getSelectedFile().getAbsolutePath());
			m_viewController.loadGame (c.getSelectedFile().getAbsolutePath());
		}
		else if (rVal == JFileChooser.CANCEL_OPTION) 
		{
			System.out.println ("You pressed cancel");
		}
	}
}
