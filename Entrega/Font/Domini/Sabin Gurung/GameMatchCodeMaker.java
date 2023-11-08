package model;

import java.util.ArrayList;

/** this is a concrete implementation of the base class AGameMatch
 *  this class contains the functions necessary to carry out a match
 *  where the player role is codemaker
 */
public class GameMatchCodeMaker extends AGameMatch
{
	// code generator that uses genetic algorithm under the hood
	private CodeGenerator m_codeGenerator;
	// helper data for code generator
	private int m_maxSons;
	private int m_maxGenerations;
	// string containg the machine candidate code for current turn
	private Code m_machineCandidat;

	public GameMatchCodeMaker()
	{
		super();
		m_codeGenerator = new CodeGenerator();
		m_maxSons = 100;
		m_maxGenerations = 60;
		m_currentTurn = 0;
	}

	/** updates the game with the given candidat game
	 *  pre: a game match where user is code maker is taking place
	 *  post: updates the code and raises necessary flags such as
	 *        did game end or who won
	 */
	public boolean update (String p_code) {
		// beginning of game so the first code is the objective code
		if (m_currentTurn == 0)
		{
			Board.setCodeObjective (p_code);
			m_currentTurn++;

			m_machineCandidat = m_codeGenerator.m_generateCodeTest (m_machineCandidat, new Pair (0, 0) , m_maxSons, m_maxGenerations);
			if (!Board.isRep()) {
				while (m_codeGenerator.hasRepetitions(m_machineCandidat).size() != 0) {
					m_codeGenerator.m_generateCodeTest (m_machineCandidat, new Pair (0, 0) , m_maxSons, m_maxGenerations);
				}
			}
		}
		else
		{
			// code is BN
			// key: #Negre(black) value: #Blanco(white)
			Pair <Integer, Integer> BNcount = m_machineCandidat.getBN (Board.getCodeObjective());
			int Bcount = 0;
			int Ncount = 0;
			for (char i : p_code.toCharArray())
			{
				if (i == 'N')
					Ncount++;
				else if (i == 'B')
					Bcount++;
			}

			System.out.println(m_machineCandidat.getCode());
			System.out.println(BNcount.getKey() + " " + BNcount.getValue());
			// check invalid feedback
			if (!(BNcount.getValue() == Bcount) || !(BNcount.getKey() == Ncount))
			{
				System.out.println ("Error: BN (feedback) is not valid");
				return false;
			}

			singletonBoard.getInstance().newHistoryEntry (m_machineCandidat, p_code);
			m_currentTurn++;

			// generate machine code for next turn
			m_machineCandidat = m_codeGenerator.m_generateCodeTest (m_machineCandidat, BNcount, m_maxSons, m_maxGenerations);
			int count = 0;
			while (m_machineCandidat.getCode().equals ("notWorking") && count < 10) {
				System.out.println(m_machineCandidat.getCode());
				m_machineCandidat = m_codeGenerator.m_generateCodeTest (m_machineCandidat, BNcount, m_maxSons*2, m_maxGenerations/2);
				++count;
			}
			if (count == 10) m_machineCandidat = m_codeGenerator.codi_random();

			// check game won or lost
			if (BNcount.getKey().equals(singletonBoard.getInstance().getLength()))
				m_isGameEnded = true;
			else if (m_currentTurn > m_totalTurns)
				m_isGameEnded = true;
			else
				singletonBoard.getInstance().incrementar_punt();
		}
		return true;
	}

    public ArrayList<Pair<String,String>> getBoardContents() {
		ArrayList <Pair <String, String>> bc = singletonBoard.getInstance().getHistory();
		if (!m_isGameEnded)
			bc.add (new Pair <String, String> (m_machineCandidat.getCode(), null));
		return bc;
	}

	/** sets m_machineCandidat */
	public void setMachineCandidat (String a)
	{
		m_machineCandidat = new Code (a);
	}

	/** gets m_machineCandidat */
	public String getMachineCandidat ()
	{
		return m_machineCandidat.getCode();
	}
}
