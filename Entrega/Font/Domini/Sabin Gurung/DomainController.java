
package model;

import data.CtrlScore;
import presentation.ViewController;
import data.CtrlSaveGame;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class DomainController
{
	private AGameMatch m_currentGame;
	private singletonRanking Ranking;
	private ViewController m_viewController;

	public DomainController(ViewController p_viewController) {
		m_viewController = p_viewController;
		Ranking = singletonRanking.getInstance();
	}

	// -----------------functions related to games ----------------------------------------------

	/** Creates a New game based of role and difficulty
	 *  pre: p_playerRole can either be codebreaker or codemaker
	 *  post: p_difficulty can either be easy or ---
	 */
	public void createNewGame (String p_playerRole, String p_difficulty) {
		m_currentGame = GameMatchManager.getInstance().createNewGame (p_playerRole, p_difficulty);
	}

	/** saves current game
	 *  pre: a match must already be taking place
	 *  post: creates a file of name p_fileName and writes content of current game on it
	 */
	public void saveGame (String p_fileName) {
		String content = GameMatchManager.getInstance().generateSaveState (m_currentGame);
		try 
		{
			CtrlSaveGame.getInstance().saveGameToDisk (content, p_fileName);
		}
		catch (IOException err)
		{

			System.out.println ("ioerro");
		}
	}

	/** load game
	 *  pre: -
	 *  post: create an instance of game based on the info in p_fileName
	 */
	public boolean loadGame (String p_fileName) {
		String content;
		try
		{
			content = CtrlSaveGame.getInstance().getSaveGameData (p_fileName);
			m_currentGame = GameMatchManager.getInstance().loadGame (content);
		}
		catch (IOException err)
		{

			System.out.println ("ioerro");
			return false;
		}
		return true;
	}

	/** update game
	 *  pre: game must already be taking place
	 *  post: updates current game and returns the history data
	 */
	public ArrayList <Pair <String, String>> updateGame (String p_code) throws IOException {
		// update code in current game
		m_currentGame.update (p_code);
		// end game view is game has ended
		if (m_currentGame.isGameEnded())
		{
			m_viewController.endGame(singletonBoard.getInstance().getPoints());	
		}
		// send data to display
		return m_currentGame.getBoardContents();
	}

	/** returns the objective code on current game
	 *  pre: game must already be taking place
	 *  post: -
	 */
	public String getCurrentGameObjCode()
	{
		return singletonBoard.getInstance().getCodeObjective().getCode();
	}

	/** return the length of code on current game
	 *  pre: game must already be taking place
	 *  post: -
	 */
	public int getCurrentGameCodeLength()
	{
		return singletonBoard.getInstance().getLength();
	}

	/** return if repetitions is allowed in current game or not
	 *  pre: game must already be taking place
	 *  post: -
	 */
	public boolean getCurrentGameRep()
	{
		return singletonBoard.getInstance().isRep();
	}

	/** return players role in current game
	 *  pre: game must already be taking place
	 *  post: -
	 */
	public String getCurrentGamePlayerRole()
	{
		return singletonBoard.getInstance().getPlayer_role();
	}

	/** return players role in current game
	 *  pre: game must already be taking place
	 *  post: -
	 */
	public ArrayList <Pair <String, String>> getCurrentGameHistory ()
	{
		return m_currentGame.getBoardContents();
	}
	// ------------------functions related to ranking --------------------------------------

	/** loads the ranking from a file and saves it to instance of Ranking
	 *  pre: -
	 *  post: returns true if the process is successful
	 */
	public boolean loadRanking (String p_fileName) throws IOException {
		Ranking = singletonRanking.getInstance();
		// read from file
		// if file not exists return false
		String saveData = CtrlScore.getInstance().readSavedRankingFile (p_fileName);
		// else load game and return true
		Ranking.loadSavedState (saveData);
		return true;
	}

	/** write the data that is in the instance of Ranking to file
	 *  pre: -
	 *  post: the data is saved into de file
	 */
	public void saveRankingToDisk () throws IOException {
		String content = Ranking.generateSaveState();
		CtrlScore.getInstance().updateRanking(content);
	}

	/** adds a record to Ranking
	 *  pre: a game has just ended
	 *  post: the record is added to Ranking
	 */
	public Record addRecordToRanking(String name, Integer puntuation) {
		Date d = new Date();
		d.getTime();
		Record newRecord = new Record(name,puntuation,d.toString(),singletonBoard.getInstance().getPlayer_role());
		Ranking.addEntry(newRecord);
		return newRecord;
	}

	/** gets the list of records in Ranking
	 *  pre: -
	 *  post: returns an allaylist with all the records saved
	 */
	public ArrayList<Record> getRanking() {
		return Ranking.getRanking();
	}
}
