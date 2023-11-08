package model;

import java.util.ArrayList;

/** abstract base class for a match
 *  there are two concrete match that will extend this class
 *  match where player is codemaker
 *  match where player is codebreaker
 */
public abstract class AGameMatch
{
	// variables that are needed by both classes
	protected boolean m_isGameEnded = false;
	// total number of guesses that can be made
	protected int m_totalTurns = 10;
	// current turn
	protected int m_currentTurn = 1;
	// Board
	protected singletonBoard Board;

	public AGameMatch()
	{
		Board = singletonBoard.getInstance();
	}

	/** checks whether the game has ended or not
	 *  pre: a game is taking place
	 *  post:
	 */
	public boolean isGameEnded()
	{
		return m_isGameEnded;
	}

	/** returns totat number of turns */
	public int getTotalTurns()
	{
		return m_totalTurns;
	}

	/** sets total number of turns */
	public void setTotalTurns (int a)
	{
		m_totalTurns = a;
	}

	/** gets current turn */
	public int getCurrentTurn()
	{
		return m_currentTurn;
	}

	/** sets current turn */
	public void setCurrentTurn (int a)
	{
		m_currentTurn = a;
	}

	public void setModel(String model) { Board.setCodeObjective(model); }

	/** updates the code that the user provided
	 *  pre: a game is already taking place
	 *  post: the code is updated on the game board
	 */
	public abstract boolean update (String p_code);

	/** gets content of the board to view
	 * pre: a game is taking place
	 * post:
	 */
    public abstract ArrayList<Pair<String,String>> getBoardContents();
}
