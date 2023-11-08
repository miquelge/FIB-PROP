package presentation;

import model.Pair;

import java.io.IOException;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewObjCodeChooser extends JFrame {

	private ViewController m_viewController;

	private int codeLength;
	private int numColors;
	private int noRows = 10;
	private boolean repAllowed;

	private JPanel mainPanel;

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


	// construtor
	/** instanciates game match as code breaker view
	 *  @pre: 
	 *  @post: instanciates this view and saves instance of viewcontroller 
	 */
	public ViewObjCodeChooser (ViewController p_viewController) {
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

	/** setups ui
	 *  @pre: 
	 *  @post: setups ui for this view
	 */
	private void initUI() {
		mainPanel = new JPanel ();
		sidePanel = new JPanel();

		mainPanel.setLayout (new GridLayout (1, 1, 30, 20));

		this.add (mainPanel);
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
							if (!repAllowed && colorUsed.get (b0.getBackground()))
								return;
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
							if (!repAllowed && colorUsed.get (b1.getBackground()))
								return;
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
							if (!repAllowed && colorUsed.get (b2.getBackground()))
								return;
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
							if (!repAllowed && colorUsed.get (b3.getBackground()))
								return;
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
							if (!repAllowed && colorUsed.get (b4.getBackground()))
								return;
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
							if (!repAllowed && colorUsed.get (b5.getBackground()))
								return;
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
					}
				});
		testButton.addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent event)
					{
						if (tryCodeIndex != codeLength)
						{
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
								m_viewController.sendObjcode (sb.toString());
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
	public void updateParameters(int length, int num_colors, boolean reps) {
		codeLength = length;
		numColors = num_colors;
		repAllowed = reps;

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
