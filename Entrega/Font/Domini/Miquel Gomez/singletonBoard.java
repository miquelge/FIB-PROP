package model;

import java.util.ArrayList;

public class singletonBoard {

    /** Atributes */
    private int length;
    private boolean rep;
    private int num_colors;
    private String Player_role;
    private int Points;
    public Code codeObjective;
    public ArrayList<Code> historyCode = new ArrayList<>();
    public ArrayList<String> historyBN = new ArrayList<>();

    private static singletonBoard instance = null;

    /** Constructor 1*/
    public singletonBoard() {
        this.Points = 0;
    }

    /** Constructor 2*/
    public singletonBoard(int l, boolean r, int n, String Player_role) {
        this.length = l;
        this.rep = r;
        this.num_colors = n;
        this.Player_role = Player_role;
        this.Points = 0;
    }

    public static singletonBoard getInstance() {
        if (instance == null)
            instance = new singletonBoard() {
            };
        return instance;
    }

    /** Getters & Setters */
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }

    public boolean isRep() {
        return rep;
    }
    public void setRep(boolean rep) {
        this.rep = rep;
    }

    public int getNum_colors() {
        return num_colors;
    }
    public void setNum_colors(int num_colors) {
        this.num_colors = num_colors;
    }

    public String getPlayer_role() {
        return Player_role;
    }
    public void setPlayer_role(String player_role) {
        Player_role = player_role;
    }

    public int getPoints() {
        return Points;
    }

    public int setPoints (int points) {
        return Points = points;
    }

    public Code getCodeObjective() {
        return codeObjective;
    }
    public void setCodeObjective(String codeObjective) {
        this.codeObjective = new Code(codeObjective);
    }

    /** Increments the score */
    public void incrementar_punt() {
        Points++;
    }

    public void decrementar_punt() {
        Points--;
    }

    /** Adds a new element to the games history, which is formed by a code and its BN */
    public void newHistoryEntry(Code code, String BN) {
        historyCode.add(code);
        historyBN.add(BN);
    }

    /** Return an Arraylist with all the history elements */
    public ArrayList<Pair<String,String>> getHistory() {
        ArrayList<Pair<String,String>> history = new ArrayList<>();
        for (int i = 0; i < historyCode.size(); ++i) {
            Pair pair = new Pair(historyCode.get(i).getCode(), historyBN.get(i).toString());
            history.add(pair);
        }
        return history;
    }

	/** clears the history board */
    public void clearHistory()
	{
		historyCode.clear();
		historyBN.clear();
	}

	/*
    public boolean updateLastFeedBack(Pair<Integer,Integer> P) {
        Code code = historyCode.get(historyCode.size()-1);
        String BN = "";
        for (int i = 0; i < P.getValue().intValue(); ++i) BN.concat("N");
        for (int i = 0; i < P.getKey().intValue(); ++i) BN.concat("B");
        for (int i = BN.length(); i < length; ++i) BN.concat("0");
        if ( BN != code.getBNString(codeObjective)) return false;
        historyBN.set(historyBN.size()-1, BN);
        return true;
    }
    */
}
