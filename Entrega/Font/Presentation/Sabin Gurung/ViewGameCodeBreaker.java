package presentation;

import model.Pair;

import java.io.IOException;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewGameCodeBreaker extends JFrame {

	private ViewController m_viewController;

	private int codeLength;
	private int numColors;
	private int noRows = 10;
	private boolean repAllowed;

	private JPanel mainPanel;
	private JPanel candidatPanel;
	private JPanel BNPanel;

	private JButton[][] candidatButtons;
	private JButton[][] BNButtons;

	private JPanel sidePanel;

	private JPanel trycodePanel;
	private JButton[] trycodeButtons;
	private int tryCodeIndex;

	private JPanel chooseCodePanel;
	private JButton b0;
	private JButton b1;
	private JButton b2;
	private JButton b3;
	private JButton b4;
	private JButton b5;

	private JPanel buttonsPanel;
	private JButton testButton;
	private JButton eraseButton;

	private HashMap <Color, Integer> colorToCodeTranslation;

	private HashMap <Color, Boolean> colorUsed;

	private JMenuBar menubar;

	// construtor
	/** instanciates game match as code breaker view
	 *  @pre: 
	 *  @post: instanciates this view and saves instance of viewcontroller 
	 */
	public ViewGameCodeBreaker (ViewController p_viewController) {
		m_viewController = p_viewController;
		initializeColorTranslation();
		initUI();
		addListeners();
	}

	/** initializes color to code translation map and color used map
	 *  @pre: 
	 *  @post: creates a map that contains color to code translation
	           and a map that cotains used color or not
	 */
	private void initializeColorTranslation()
	{
		colorToCodeTranslation = new HashMap <Color, Integer> ();
		colorToCodeTranslation.put (Color.blue, 0);
		colorToCodeTranslation.put (Color.green, 1);
		colorToCodeTranslation.put (Color.magenta, 2);
		colorToCodeTranslation.put (Color.red, 3);
		colorToCodeTranslation.put (Color.yellow, 4);
		colorToCodeTranslation.put (Color.cyan, 5);
		colorToCodeTranslation.put (Color.orange, 6);

		colorUsed = new HashMap <Color, Boolean> ();
		colorUsed.put (Color.blue, false);
		colorUsed.put (Color.green, false);
		colorUsed.put (Color.magenta, false);
		colorUsed.put (Color.red, false);
		colorUsed.put (Color.yellow, false);
		colorUsed.put (Color.cyan, false);
		colorUsed.put (Color.orange, false);
	}

	/** set that none of the color has been used
	 *  @pre: 
	 *  @post: set all the values to false in the map containing color used or no map
	 */
	private void setFalseOnAllColor()
	{
		for (Color key : colorUsed.keySet())
		{
			colorUsed.replace (key, false);
		}
	}

	/** gets a color from a code peg (id)
	 *  @pre: 
	 *  @post: returns the color responding to code id
	 */
	private Color getColor (int x) {
		switch (x) {
			case (int) '0': return Color.blue;
			case (int) '1': return Color.green;
			case (int) '2': return Color.magenta;
			case (int) '3': return Color.red;
			case (int) '4': return Color.yellow;
			case (int) '5': return Color.cyan;
			case (int) '6': return Color.orange;
			case (int) 'N': return Color.black;
			case (int) 'B': return Color.gray;
		}
		return Color.black;
	}

	/** initializes main menu 
	 *  @pre: 
	 *  @post: initializes menu bar
	 */
	private void initMenuBar()
	{
		menubar = new JMenuBar();
		this.setJMenuBar (menubar);

		JMenu gameMenu  = new JMenu ("Game");
		menubar.add (gameMenu);

		JMenuItem saveNewItem = new JMenuItem("Save Game into new file");
		gameMenu.add(saveNewItem);
		JMenuItem saveMenuItem = new JMenuItem ("Save Game into existing file");
		gameMenu.add (saveMenuItem);
		JMenuItem quitMenuItem = new JMenuItem ("Quit Current Game");
		gameMenu.add (quitMenuItem);

		saveNewItem.addActionListener ((ActionEvent event) ->
		{
			String name = JOptionPane.showInputDialog (null, "Enter the name of the file to save your current game:");
			String AbsolutePath = "./SavedFiles/" + name + ".txt";

			System.out.println ("Saving To: " + AbsolutePath);
			m_viewController.saveGame (AbsolutePath);
		});
		saveMenuItem.addActionListener ((ActionEvent event) ->
		{
			JFileChooser c = new JFileChooser();
			c.setCurrentDirectory(new java.io.File("./SavedFiles"));
			int rVal = c.showOpenDialog (this);
			if (rVal == JFileChooser.APPROVE_OPTION)
			{
				System.out.println ("Saving To: " + c.getSelectedFile().getAbsolutePath());
				m_viewController.saveGame (c.getSelectedFile().getAbsolutePath());
			}
		});
		quitMenuItem.addActionListener ((ActionEvent event) ->
		{
			m_viewController.sincronize_viewGame_to_viewMainMenu();
		});

	}

	/** setups ui
	 *  @pre: 
	 *  @post: setups ui for this view
	 */
	private void initUI() {
		initMenuBar();

		mainPanel = new JPanel ();
		candidatPanel = new JPanel();
		BNPanel = new JPanel();
		sidePanel = new JPanel();

		mainPanel.setLayout (new GridLayout (1, 2, 30, 20));

		candidatPanel.setBackground (new Color (50, 50, 50));
		BNPanel.setBackground (new Color (200, 200, 200));

		this.add (mainPanel);
		mainPanel.add (candidatPanel);
		mainPanel.add (BNPanel);
		mainPanel.add (sidePanel);

		sidePanel.setLayout (new BoxLayout (sidePanel, BoxLayout.Y_AXIS));
		sidePanel.add (Box.createVerticalGlue());

		trycodePanel = new JPanel();
		sidePanel.add (trycodePanel);

		sidePanel.add (Box.createRigidArea(new Dimension(0, 25)));

		chooseCodePanel = new JPanel();
		sidePanel.add (chooseCodePanel);
		chooseCodePanel.setLayout (new GridLayout (3, 2, 15, 15));
		b0 = new JButton(); b0.setBackground (getColor ('0')); b0.setOpaque (true);
		b1 = new JButton(); b1.setBackground (getColor ('1')); b1.setOpaque (true);
		b2 = new JButton(); b2.setBackground (getColor ('2')); b2.setOpaque (true);
		b3 = new JButton(); b3.setBackground (getColor ('3')); b3.setOpaque (true);
		b4 = new JButton(); b4.setBackground (getColor ('4')); b4.setOpaque (true);
		b5 = new JButton(); b5.setBackground (getColor ('5')); b5.setOpaque (true);
		chooseCodePanel.add (b0);                 chooseCodePanel.add(b1);
		chooseCodePanel.add (b2);                 chooseCodePanel.add(b3);
		chooseCodePanel.add (b4);                 chooseCodePanel.add(b5);


		sidePanel.add (Box.createRigidArea(new Dimension(0, 25)));

		// buttons panel
		buttonsPanel = new JPanel();
		sidePanel.add (buttonsPanel);
		buttonsPanel.setLayout (new GridLayout (0, 2));
		testButton = new JButton ("Test");
		eraseButton = new JButton ("Erase");
		buttonsPanel.add (testButton);
		buttonsPanel.add (eraseButton);

		sidePanel.add (Box.createVerticalGlue());

		// set window size and quit event
		setTitle ("MasterMind");
		setMinimumSize (new Dimension (500, 500));
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setLocationRelativeTo (null);
	}

	/** initializes board
	 *  @pre: 
	 *  @post: initializes board
	 */
	private void initBoard()
	{
		// borrar taulas
		candidatPanel.removeAll();
		BNPanel.removeAll();

		// set layouts
		candidatPanel.setLayout (new GridLayout (noRows, codeLength, 5, 15));
		BNPanel.setLayout (new GridLayout (noRows, codeLength, 5, 15));
		
		// genrar buttons que representa bolas
		candidatButtons = new JButton[noRows][codeLength];
		BNButtons = new JButton[noRows][codeLength];

		for (int i = 0; i < noRows; ++i)
		{
			for (int j = 0; j < codeLength; ++j)
			{
				candidatButtons[i][j] = new JButton();
				candidatButtons[i][j].setEnabled (false);
				BNButtons[i][j] = new JButton();
				BNButtons[i][j].setEnabled (false);
				candidatPanel.add (candidatButtons[i][j]);
				BNPanel.add (BNButtons[i][j]);
			}
		}
	}


	/** add listeners to buttons
	 *  @pre: 
	 *  @post: add listeners to buttons
	 */
	private void addListeners() {
		b0.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						if (tryCodeIndex < codeLength)
						{
							if (!repAllowed && colorUsed.get (b0.getBackground())) {
                                JOptionPane.showMessageDialog(getContentPane(), "Repetitions are not allowed");
                                return;

                            }
							colorUsed.replace (b0.getBackground(), true);

							trycodeButtons[tryCodeIndex].setBackground (b0.getBackground());
							trycodeButtons[tryCodeIndex].setOpaque (true);
							++tryCodeIndex;
						}
					}
				});
		b1.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						if (tryCodeIndex < codeLength)
						{
							if (!repAllowed && colorUsed.get (b1.getBackground())) {
                                JOptionPane.showMessageDialog(getContentPane(), "Repetitions are not allowed");
                                return;
                            }
							colorUsed.replace (b1.getBackground(), true);

							trycodeButtons[tryCodeIndex].setBackground (b1.getBackground());
							trycodeButtons[tryCodeIndex].setOpaque (true);
							++tryCodeIndex;
						}
					}
				});
		b2.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						if (tryCodeIndex < codeLength)
						{
                            if (!repAllowed && colorUsed.get (b2.getBackground())) {
                                JOptionPane.showMessageDialog(getContentPane(), "Repetitions are not allowed");
                                return;
                            }
                            colorUsed.replace (b2.getBackground(), true);

							trycodeButtons[tryCodeIndex].setBackground (b2.getBackground());
							trycodeButtons[tryCodeIndex].setOpaque (true);
							++tryCodeIndex;
						}
					}
				});
		b3.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						if (tryCodeIndex < codeLength)
						{
                            if (!repAllowed && colorUsed.get (b3.getBackground())) {
                                JOptionPane.showMessageDialog(getContentPane(), "Repetitions are not allowed");
                                return;
                            }
							colorUsed.replace (b3.getBackground(), true);

							trycodeButtons[tryCodeIndex].setBackground (b3.getBackground());
							trycodeButtons[tryCodeIndex].setOpaque (true);
							++tryCodeIndex;
						}
					}
				});
		b4.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						if (tryCodeIndex < codeLength)
						{
                            if (!repAllowed && colorUsed.get (b4.getBackground())) {
                                JOptionPane.showMessageDialog(getContentPane(), "Repetitions are not allowed");
                                return;
                            }
							colorUsed.replace (b4.getBackground(), true);

							trycodeButtons[tryCodeIndex].setBackground (b4.getBackground());
							trycodeButtons[tryCodeIndex].setOpaque (true);
							++tryCodeIndex;
						}
					}
				});
		b5.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						if (tryCodeIndex < codeLength)
						{
                            if (!repAllowed && colorUsed.get (b5.getBackground())) {
                                JOptionPane.showMessageDialog(getContentPane(), "Repetitions are not allowed");
                                return;
                            }
							colorUsed.replace (b5.getBackground(), true);

							trycodeButtons[tryCodeIndex].setBackground (b5.getBackground());
							trycodeButtons[tryCodeIndex].setOpaque (true);
							++tryCodeIndex;
						}
					}
				});
		eraseButton.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						if (tryCodeIndex > 0)
						{
							--tryCodeIndex;
							colorUsed.replace (trycodeButtons[tryCodeIndex].getBackground(), false);

							trycodeButtons[tryCodeIndex].setBackground (null);
							trycodeButtons[tryCodeIndex].setOpaque (false);
						}
						else {
                            JOptionPane.showMessageDialog(getContentPane(), "Nothing to erase");
                        }
					}
				});
		testButton.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						if (tryCodeIndex != codeLength)
						{
                            JOptionPane.showMessageDialog(getContentPane(), "Test code not complete");

                            System.out.println ("Error: code candidat not complete");
						}
						else
						{
							StringBuilder sb = new StringBuilder();
							for (int i = 0; i < codeLength; ++i)
							{
								Color a = trycodeButtons[i].getBackground();
								sb.append (colorToCodeTranslation.get (a));
								// borrar
								trycodeButtons[i].setBackground (null);
								trycodeButtons[i].setOpaque (false);
							}

							setFalseOnAllColor();

							tryCodeIndex = 0;

							try {
								m_viewController.updateGameCodeBreaker (sb.toString());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	/** setups parameters for views
	 *  @pre: 
	 *  @post: parameters need to create view is updated and the view is created
	 */
	public void updateParameters(int length, int num_colors, boolean reps, ArrayList <Pair <String, String>> history) {
		codeLength = length;
		numColors = num_colors;
		repAllowed = reps;

		initBoard();
		fillBoard (history);

		// init display panel of test code
		trycodePanel.removeAll();
		trycodePanel.setLayout (new GridLayout(0, codeLength, 5, 5));
		trycodeButtons = new JButton[length];
		for (int i = 0; i < length; ++i)
		{
			trycodeButtons[i] = new JButton();
			trycodeButtons[i].setEnabled (false);
			trycodePanel.add (trycodeButtons[i]);
		}

		tryCodeIndex = 0;
		setFalseOnAllColor();
	}


	/** fills the view with correct colors based from the data recieved
	 *  @pre: key part of data corresponds to candidat code and value part corresponds to BN code
	 *  @post: fills the board 
	 */
	public void fillBoard (ArrayList <Pair <String, String>> data)
	{
		for (int i = 0; i < data.size(); ++i)
		{
			String candidat = data.get(i).getKey();
			String bn = data.get(i).getValue();

			if (candidat != null)
				fillRow (candidat, candidatButtons[i]);
			if (bn != null)
				fillRow (bn, BNButtons[i]);
		}

	}

	/** fills a row with colors
	 *  @pre: lenth of the data is smaller than size of buttons
	 *  @post: fills the button with colors based on the data
	 */
	private void fillRow (String data, JButton[] buttons)
	{
		for (int i = 0; i < data.length(); ++i)
		{
			char a = data.charAt (i);
			buttons[i].setBackground (getColor ((int) a));
			buttons[i].setOpaque (true);
		}
	}

	/** Enable this view
	 *  @pre: 
	 *  @post: this view is enables and visible
	 */
	public void makeVisible() {
		setEnabled (true);
		setVisible (true);
	}

	/** Disable this view
	 *  @pre: 
	 *  @post: this view is disabled and invisible
	 */
	public void makeInvisible() {
		setEnabled (false);
		setVisible (false);
	}

}
