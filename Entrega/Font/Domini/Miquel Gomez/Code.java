package model;

import java.util.*;

public class Code {

    /** Atributes */
    private String code;
    private singletonBoard board;

    /** Constructor */
    public Code (String code) {
        this.code = code;
        board = singletonBoard.getInstance();
    }

    /** Getters & Setters */
    public String getCode() { return code; }
    public int getLength() {
        return board.getLength();
    }
    public boolean isRep() {
        return board.isRep();
    }
    public Integer getNum_colors() {
        return board.getNum_colors();
    }
    public char getIthElm(int i) { return code.charAt(i); }
    public void changeRepeatedElms(ArrayList<Integer> rep, Set<Integer> colors) {
        Random random = new Random();
        for (int i = 0; i < rep.size(); ++i) {
            Integer subs = random.nextInt(board.getNum_colors());
            while (!colors.add(subs)) subs = random.nextInt(board.getNum_colors());
            String newChar = subs.toString();
            Integer rpos = rep.get(i);
            if (rpos == 0) code = newChar + code.substring(1);
            else code = code.substring(0,rpos) + newChar + code.substring(rpos+1);
        }
    }

    /** Returns a pair with the BN (feedback) of comparing code to the model, formed by #black pegs and #white pegs*/
    public Pair getBN(Code model) {
        int negres = 0;
        int blanques = 0;
        boolean[] used_code = new boolean[board.getLength()];
        boolean[] used_model = new boolean[board.getLength()];
        //busquem negres
        for (int i = 0; i < board.getLength(); i++) {
            if (model.getCode().charAt(i) == code.charAt(i)) {
                used_code[i] = true;
                used_model[i] = true;
                negres++;
            }
        }
        //busquem blanques
        for (int i = 0; i < board.getLength(); i++) {
            if (! used_code[i]) {
                for (int j = 0; j < board.getLength(); j++) {
                    if (!used_model[j] && code.charAt(i) == model.getCode().charAt(j)) {
                        used_model[j] = true;
                        blanques++;
                        break;
                    }
                }
            }
        }
        return new Pair(negres, blanques);
    }

	/** Returns the BN code in String format */
    public String getBNString(Code model) {
    	Pair <Integer, Integer> bncount = getBN (model);
    	StringBuilder sb = new StringBuilder();

    	for (int i = 0; i < bncount.getKey(); ++i)
    		sb.append ('N');
    	for (int i = 0; i < bncount.getValue(); ++i)
    		sb.append ('B');

    	return sb.toString();
    }
}
