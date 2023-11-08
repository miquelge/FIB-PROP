package model;

import java.util.*;

/** This is the singleton class that has 
 *  the responsibility of creation,
 *  saving and loading of GameMatch
 *  instances
 *  It hides the instanciation of the concrete gameMatch classes
 */

public class GameMatchManager
{
	private static GameMatchManager singletonObject;

	public static GameMatchManager getInstance()
	{
		if (singletonObject == null)
			singletonObject = new GameMatchManager() {
			};
		return singletonObject;
	}

	private GameMatchManager()
	{
	}

	/** creates a instance of game match
	 *  depending on the parameters given
	 *  pre: p_playerRole is either codebreaker or code maker
	 *       p_difficulty is either easy or hard
	 *  post: creates the necessary instance of game match and returns
	 */
	public AGameMatch createNewGame (String p_playerRole, String p_difficulty)
	{
		singletonBoard sb = singletonBoard.getInstance();
		sb.clearHistory();

		if (p_difficulty.equals ("easy"))
		{
			sb.setLength (4);
			sb.setRep (false);
			sb.setNum_colors (6);
		}
		else if (p_difficulty.equals ("medium"))
		{
			sb.setLength (4);
			sb.setRep (true);
			sb.setNum_colors (6);
		}
		else if (p_difficulty.equals ("hard"))
		{
			sb.setLength (5);
			sb.setRep (true);
			sb.setNum_colors (6);
		}

		if (p_playerRole.equals ("codebreaker"))
		{
			AGameMatch gm = new GameMatchCodeBreaker();
			sb.setPoints (10);
			sb.setPlayer_role (p_playerRole);
			CodeGenerator cg = new CodeGenerator();
			String objCode = cg.m_generateCodeObjective().getCode();
			System.out.println ("objcode is :" + objCode);
			sb.setCodeObjective (objCode);
			return gm;
		}
		else if (p_playerRole.equals ("codemaker"))
		{
			sb.setPoints (1);
			sb.setPlayer_role (p_playerRole);
			AGameMatch gm = new GameMatchCodeMaker();
			return gm;
		}

		return null;
	}

	/** generate savestate
	 *  pre: a game is currently taking place
	 *  post: returns a string containing all the information
	 *        about a game
	 */
	public String generateSaveState (AGameMatch p_curMatch)
	{
		StringBuilder saveState = new StringBuilder();
		singletonBoard sb = singletonBoard.getInstance();
		// add game settings
		saveState.append ("boardProperties: ");
		saveState.append (sb.getLength() + " ");
		saveState.append (sb.isRep() + " ");
		saveState.append (sb.getNum_colors() + " ");
		String playerRole = sb.getPlayer_role();
		saveState.append (playerRole + " ");
		saveState.append (sb.getCodeObjective().getCode() + "\n");
		// add history
		ArrayList <Pair <String, String>> history = sb.getHistory();
		for (Pair <String, String> p : history)
		{
			saveState.append ("historyEntry: " + p.getKey() + " " + p.getValue() + "\n");
		}

		// current points
		saveState.append ("puntuation: " + sb.getPoints() + "\n");
		saveState.append ("currentTurn: " + p_curMatch.getCurrentTurn() + "\n");

		if (playerRole.equals ("codemaker"))
		{ 
			saveState.append ("machineCode: " + ((GameMatchCodeMaker) p_curMatch).getMachineCandidat());
		}

		return saveState.toString();
	}

	/** generate an instace of a gamematch from a save file info
	 *  pre: 
	 *  post: returns an instance of gamematch based on the save state info
	 */
	public AGameMatch loadGame (String p_savedState)
	{
		// needs to check if save file is corrupted or not

		AGameMatch agm = null;
		Scanner scanner = new Scanner(p_savedState);
		singletonBoard sb = singletonBoard.getInstance();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] words = line.split("\\s+"); /* This splits by blank spaces, so if there's a space next to a tab it understand it as one unique space */
			switch(words[0]) {
				case ("boardProperties:"):
					sb.setLength (Integer.parseInt (words[1]));
					sb.setRep (Boolean.valueOf (words[2]));
					sb.setNum_colors (Integer.parseInt (words[3]));
					String playerRole = words[4];
					if (playerRole.equals ("codemaker"))
					{
						agm = new GameMatchCodeMaker();
					}
					else if (playerRole.equals ("codebreaker"))
					{
						agm = new GameMatchCodeBreaker();
					}
					sb.setPlayer_role (playerRole);
					sb.setCodeObjective (words[5]);
					sb.clearHistory();
					break;
				case ("historyEntry:"):
					String bn = "";
					if (words.length == 3)
					{
						bn = words[2];
					}
					sb.newHistoryEntry(new Code(words[1]), bn);
					//m_codeGenerator.newGuessesEntry(new Pair(words[1],stringBN_to_pairBN(words[2])));
					break;
				case ("puntuation: "):
					sb.setPoints (Integer.parseInt(words[1]));
					break;
				case ("machineCode:"):
					((GameMatchCodeMaker) agm).setMachineCandidat (words[1]);
					break;
				case ("currentTurn:"):
					agm.setCurrentTurn(Integer.parseInt(words[1]));
					break;
			}
		}

		scanner.close();

		return agm;
	}
}
