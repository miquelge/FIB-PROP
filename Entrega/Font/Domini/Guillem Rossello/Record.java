package model;

import java.util.Date;

public class Record {

    /** Atributes */
    private String playerName;
    private Integer score;
    private String date;
    private String role;

    /** Constructor */
    public Record(String playerName, Integer score, String date, String role) {
        this.playerName = playerName;
        this.score = score;
        this.date = date;
        this.role = role;
    }

    /** Getters & Setters*/
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setDate(String date) { this.date = date; }

    public String getPlayerName() { return playerName; }

    public Integer getScore() { return score; }

    public String getDate() { return date; }

    public String getRole() { return role; }
}
