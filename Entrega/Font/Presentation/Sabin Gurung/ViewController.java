package presentation;

import javax.swing.*;

import java.util.*;

import model.DomainController;
import model.Pair;
import model.Record;

import java.io.IOException;
import java.util.ArrayList;

public class ViewController
{
	// ----Reference to views----
    private ViewMainMenu  m_viewMainMenu;
	private ViewShowScore m_viewShowScore;
	private ViewChoose m_viewChoose;
	private ViewGameCodeBreaker m_viewGameCodeBreaker;
	private ViewGameCodeMaker m_viewGameCodeMaker;
	private ViewObjCodeChooser m_viewObjCodeChooser;

	// ----Reference to DomainController---------
    private DomainController m_domainController;

	// -------Constructor-----------------
	/** instanciates viewcontroller along with all the views and controllers
	 *  @pre: 
	 *  @post: instanciates all views and domain controller
	 */
	public ViewController() {
		// create view and domain controller
		m_viewMainMenu  = new ViewMainMenu (this);
		m_viewShowScore = new ViewShowScore (this);
		m_viewChoose = new ViewChoose (this);
        m_viewGameCodeBreaker = new ViewGameCodeBreaker(this);
        m_viewGameCodeMaker = new ViewGameCodeMaker(this);
        m_viewObjCodeChooser = new ViewObjCodeChooser(this);

		m_domainController = new DomainController(this);
	}

	// -------Initializes out app---------------------------
	 
	/** makes main menu view visible
	 *  @pre: 
	 *  @post: 
	 */
	public void initialize () throws IOException {
		m_viewMainMenu.makeVisible();

		m_viewShowScore.makeInvisible();
		m_viewChoose.makeInvisible();
		m_viewGameCodeBreaker.makeInvisible();
		m_viewGameCodeMaker.makeInvisible();
		m_viewObjCodeChooser.makeInvisible();

		m_domainController.loadRanking("savedRanking.txt");
	}

	// -----------Methods that will be called by either view controller or domaincontroller to intercommunicate-----------------
	/** sends the code to update the game
	 *  @pre: a game where user is codebreaker is taking place 
	 *  @post: the code is updated and the views is updated 
	 */
	void updateGameCodeBreaker (String code) throws IOException {
		m_viewGameCodeBreaker.fillBoard (m_domainController.updateGame (code));
	}

	/** sends the code to update the game
	 *  @pre: a game where user is codemaker is taking place 
	 *  @post: the code is updated and the views is updated 
	 */
	void updateGameCodeMaker (String code) throws IOException {
		m_viewGameCodeMaker.fillBoard (m_domainController.updateGame (code));
	}

	/** sends the code to be set as objective code in current game
	 *  @pre: a game where user is code maker is taking place and objective
	 *       code hasnt been decided yet
	 *  @post: the code is updated and the views is updated 
	 */
	void sendObjcode (String code) throws IOException {
		m_viewObjCodeChooser.makeInvisible();

		m_viewGameCodeMaker.setObjCode (code);
		m_viewGameCodeMaker.fillBoard (m_domainController.updateGame (code));

		m_viewGameCodeMaker.makeVisible();
	}

	/** creates a new game
	 *  @pre: the user is in main menu view
	 *  @post: a new game is created and the view is switched from main menu to
	 *        respective game match view
	 */
	public void createNewGame (String p_playerRole, String p_difficulty) {
		m_domainController.createNewGame (p_playerRole, p_difficulty);
		System.out.println ("creating game of " + p_playerRole + " " + p_difficulty);

		m_viewChoose.makeInvisible();

		if (p_difficulty.equals ("easy"))
		{
			m_viewGameCodeBreaker.updateParameters (4, 6, false, new ArrayList <Pair<String, String>> ());
			m_viewGameCodeMaker.updateParameters (4, 6, false, new ArrayList <Pair <String, String>> ());
			m_viewObjCodeChooser.updateParameters (4, 6, false);
		}
		else if (p_difficulty.equals ("medium"))
		{
			m_viewGameCodeBreaker.updateParameters (4, 6, true, new ArrayList <Pair <String, String>> ());
			m_viewGameCodeMaker.updateParameters (4, 6, true, new ArrayList <Pair <String, String>> ());
			m_viewObjCodeChooser.updateParameters (4, 6, true);
		}
		else if (p_difficulty.equals ("hard"))
		{
			m_viewGameCodeBreaker.updateParameters (5, 6, true, new ArrayList <Pair <String, String>> ());
			m_viewGameCodeMaker.updateParameters (5, 6, true, new ArrayList <Pair <String, String>> ());
			m_viewObjCodeChooser.updateParameters (5, 6, true);
		}

		if (p_playerRole.equals ("codebreaker"))
		{
			m_viewGameCodeBreaker.makeVisible();
		}
		else if (p_playerRole.equals ("codemaker"))
		{
			m_viewObjCodeChooser.makeVisible();
		}
	}

	/** ends the game
	 *  @pre: the game match has just ended
	 *  @post: ends the game and prompts the user to add score, and swtiches view to show score
	 */
	public void endGame (int points) throws IOException {
		m_viewGameCodeBreaker.makeInvisible();
		m_viewGameCodeMaker.makeInvisible();
		System.out.println ("your points is " + points);

		String name = JOptionPane.showInputDialog (null, "your score is " + points +
		                                          ". Enter name to save score.");

		while (name != null && name.equals("")) {
			System.out.println("No name has been found, please try again");
			name = JOptionPane.showInputDialog (null, "your score is " + points +
					". Enter name to save score.");
		}

		if (name != null)
		{
			System.out.println ("saving record " + name + " " + points);
			Record last = m_domainController.addRecordToRanking(name,points);
			m_domainController.saveRankingToDisk();
		}


		ArrayList<Record> preData = m_domainController.getRanking();
		m_viewShowScore.setData(preData);

		m_viewShowScore.makeVisible();
	}

	/** saves current game
	 *  @pre: a game match is taking place
	 *  @post: saves the instance of game path on the provided filename
	 */
	public void saveGame (String path)
	{
		m_domainController.saveGame (path);
	}

	/** loads game match from file
	 *  @pre: filename is valid
	 *  @post: loads a game match from file and switches view to respective game match view
	 */
	public void loadGame (String path)
	{
		// loads games
		boolean sucess = m_domainController.loadGame (path);

		if (sucess)
		{
			m_viewMainMenu.makeInvisible();
			if (m_domainController.getCurrentGamePlayerRole().equals ("codebreaker"))
			{
				m_viewGameCodeBreaker.updateParameters (m_domainController.getCurrentGameCodeLength(),
						                                6,
						                                m_domainController.getCurrentGameRep(),
						                                m_domainController.getCurrentGameHistory());
				m_viewGameCodeBreaker.makeVisible();
			}
			else
			{
				m_viewGameCodeMaker.updateParameters (m_domainController.getCurrentGameCodeLength(),
						                                6,
						                                m_domainController.getCurrentGameRep(),
						                                m_domainController.getCurrentGameHistory());
				m_viewGameCodeMaker.setObjCode (m_domainController.getCurrentGameObjCode());
				m_viewGameCodeMaker.makeVisible();
			}
		}
		else
		{
			System.out.println ("Error loading game");
		}

	}


	// ---------Methods of Synchronization-----------------
	// eg.
	public void sincronize_viewMainMenu_to_viewShowScore() {
		m_viewMainMenu.makeInvisible();

		ArrayList<Record> preData = m_domainController.getRanking();
		m_viewShowScore.setData(preData);

		m_viewShowScore.makeVisible();
	}

	public void sincronize_viewGame_to_viewMainMenu()
	{
		m_viewGameCodeBreaker.makeInvisible();
		m_viewGameCodeMaker.makeInvisible();

		m_viewMainMenu.makeVisible();
	}
	
	public void sincronize_viewShowScore_to_viewMainMenu() {
		m_viewShowScore.makeInvisible();
		m_viewMainMenu.makeVisible();
	}

	public void sincronize_viewMainMenu_to_viewChoose() {
		m_viewMainMenu.makeInvisible();
		m_viewChoose.makeVisible();
	}

	public void sincronize_viewChoose_to_viewMainMenu() {
		m_viewMainMenu.makeVisible();
		m_viewChoose.makeInvisible();
	}

}
