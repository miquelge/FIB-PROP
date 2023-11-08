package model;

import java.util.ArrayList;

/** this is a concrete implementation of the base class AGameMatch
 *  this class contains the functions necessary to carry out a match
 *  where the player role is codebreakerk
 */
public class GameMatchCodeBreaker extends AGameMatch
{
	public GameMatchCodeBreaker()
	{
		super();
	}

	/**
	 *  pre: a game match where user is code breaker is taking place
	 *       singletonboard already has objective code
	 *  post: updates the code and raises necessary flags such as
	 *        did game end or who won
	 */
	public boolean update (String p_code)
	{
		singletonBoard sb = singletonBoard.getInstance();
		// make sure that the candidate is valid
		assert p_code.length() == sb.getLength() : "Invalid Candidat";

		// generate BN and insert in board
		Code cd = new Code (p_code);
		Pair <Integer, Integer> BNCount = cd.getBN (sb.getCodeObjective());

		String BN = cd.getBNString (sb.getCodeObjective());
		sb.newHistoryEntry (cd, BN);

		m_currentTurn++;
		
		// game ends if the guess is correct or if no turns left
		if (BNCount.getKey() == sb.getLength())
			m_isGameEnded = true;
		else if (m_currentTurn > m_totalTurns)
			m_isGameEnded = true;
		else
		{
			// update point
			sb.decrementar_punt();
		}
		
		return true;
	}

    public ArrayList<Pair<String,String>> getBoardContents()
	{
		return singletonBoard.getInstance().getHistory();
	}
}
