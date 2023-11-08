package presentation;

import java.awt.*;
import javax.swing.*;


public class ViewHelp {

	public ViewHelp() {
	}

	/** display information about the game 'Mastermind'
	 *  @pre: 
	 *  @post:  display information about the game 'Mastermind'
	 */
	public void makeVisible() {
		String strTitulo = "Help";
		String[] strBotones = {"Ok"};
		String strTexto =
			"Mastermind is a board game played by two players, the codebreaker and the codemaker. " + "\n" +
			"The aim of the codebreaker is to find a hidden code, while the codemaker is the one " + "\n" +
			"in charge of setting this code. Each turn consists of a guess by the codebreaker " + "\n" +
			"and an answer from the codemaker, that will help later the codebreaker figure out" + "\n" +
			"the next guess. The answer of the codemaker consists of black and white pegs:" + "\n" +
			"Each black peg means there is a correct color in the correct position in the codebreaker " + "\n" +
			"code, while a white peg means that there is a color correct, although it is not located in" + "\n" +
			"the correct position. The codebreaker has 10 turns (10 guesses) to find the hidden code," + "\n" +
			"after that the codemaker wins." + "\n";
		JOptionPane optionPane =
			new JOptionPane(strTexto,JOptionPane.INFORMATION_MESSAGE);
		optionPane.setOptions(strBotones);
		JDialog dialogOptionPane = optionPane.createDialog(new JFrame(),strTitulo);
		dialogOptionPane.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialogOptionPane.pack();
		dialogOptionPane.setVisible(true);
	}
	/**
	 * Mastermind is a board game played by two players, the codebreaker and the codemaker.
	 * The aim of the codebreaker is to find a hidden code, while the codemaker is the one
	 * in charge of setting this code. Each turn consists of a guess by the codebreaker
	 * and an answer from the codemaker, that will help later the codebreaker figure out
	 * the next guess. The answer of the codemaker consists of black and white pegs:
	 * Each black peg means there is a correct color in the correct position in the codebreaker
	 * code, while a white peg means that there is a color correct, though it is not located in
	 * the correct position. The codebreaker has 10 turns (10 guesses) to find the hidden code,
	 * after that the codemaker wins. */

}
