package data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class CtrlSaveGame {

	private static CtrlSaveGame singletonObject;

	public static CtrlSaveGame getInstance() {
		if (singletonObject == null)
			singletonObject = new CtrlSaveGame() {
			};
		return singletonObject;
	}


	private CtrlSaveGame()
	{
	}

	public void saveGameToDisk (String gameData, String filename) throws FileNotFoundException,
		   IOException
		   {
			   FileWriter out = null;
			   try 
			   {
				   out = new FileWriter (filename);
				   out.write (gameData);
			   }
			   finally
			   {
				   if (out != null)
				   {
					   out.close();
				   }
			   }

		   }

	public String getSaveGameData (String filename) throws FileNotFoundException,
		   IOException
		   {
			   StringBuilder sb = new StringBuilder();

			   FileReader in = null;
			   try 
			   {
				   in = new FileReader (filename);
				   Scanner sc = new Scanner (in);

				   while (sc.hasNextLine())
				   {
					   sb.append (sc.nextLine() + "\n");
				   }
			   }
			   finally
			   {
				   if (in != null)
				   {
					   in.close();
				   }
			   }
			   return sb.toString();
		   }
}

