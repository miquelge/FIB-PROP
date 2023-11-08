package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class singletonRanking {
    private ArrayList<Record> ranking;

    private static singletonRanking ourInstance = new singletonRanking();

    public static singletonRanking getInstance() {
        if (ourInstance == null)
            ourInstance = new singletonRanking() {
            };
        return ourInstance;
    }

    /** Constructor */
    private singletonRanking() {
        ranking = new ArrayList<>();
    }

    /** adds new record to the ranking
     * @Pre: r is not null
     * @Post: r is added to our ranking */
    public void addEntry(Record r) {
        if (ranking.size() == 10) {
            Collections.sort(ranking, new Descending());
            ranking.remove(ranking.size()-2);
        }
        ranking.add(r);
        Collections.sort(ranking, new Descending());
    }

    /** gets a concrete entry from the ranking
     * @Pre: i is a correct index
     * @Post: the i-th record is returned */
    public Record getIthEntry(int i) {
        return ranking.get(i);
    }

    /** getter
     * @Pre: -
     * @Post: return all the ranking in an ArrayList */
    public ArrayList<Record> getRanking() {
        return ranking;
    }

    /**
     * @Pre: -
     * @Post: the functions generates a string with all the records */
    public String generateSaveState() {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ranking.size(); ++i) {
            Record rc = getIthEntry(i);
            sb.append(rc.getPlayerName().toString() + "\n");
            sb.append(rc.getScore().toString() + "\n");
            sb.append(rc.getDate().toString() + "\n");
            sb.append(rc.getRole().toString() + "\n");
        }
        return sb.toString();
    }

    /**
     * @Pre: state is not null
     * @Post: the records stored in state are loaded to the system */
    public void loadSavedState(String state) {

        Scanner scanner = new Scanner(state);
        String name,score,date,role;
        while (scanner.hasNextLine()) {
            name = scanner.nextLine();
            score = scanner.nextLine();
            date = scanner.nextLine();
            role = scanner.nextLine();
            Record rc = new Record(name, Integer.valueOf(score), date, role);
            ranking.add(rc);
        }
    }

    class Descending implements Comparator<Record>
    {
        /** Used for sorting in ascending order of roll number */
        public int compare(Record a, Record b)
        {
            return b.getScore() - a.getScore();
        }
    }
}
