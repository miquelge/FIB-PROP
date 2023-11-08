package data;

import model.Pair;

import java.io.*;
import java.util.Scanner;
import java.util.Scanner;

import java.util.List;
import java.util.LinkedList;

public class CtrlScore {

	private static CtrlScore singletonObject;

	public static CtrlScore getInstance() {
		if (singletonObject == null)
			singletonObject = new CtrlScore() {
			};
		return singletonObject;
	}


	private CtrlScore()
	{
	}

	public void updateRanking (String content) throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter("savedRanking.txt"));
			out.write(String.valueOf(content));
		} catch (IOException e) {
			System.out.println("Exception ");
		} finally {
			System.out.println("Record successfully saved!");
			out.close();
		}
	}

	public String readSavedRankingFile(String fileName) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}
}

