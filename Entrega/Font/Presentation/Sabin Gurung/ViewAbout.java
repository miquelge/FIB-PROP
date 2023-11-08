package presentation;

import java.awt.*;
import javax.swing.*;


public class ViewAbout {

	public ViewAbout() {
	}

	/** display information about the software 
	 *  @pre: 
	 *  @post:  display information about the software
	 */
	public void makeVisible() {
		String strTitulo = "About";
		String[] strBotones = {"Ok"};
		String strTexto =
			"This game is a software project made for the subject Project of Programing" + "\n" +
			"of the Barcelona Faculty of Informatics. Completely developed by Miquel Gòmez," + "\n" +
			"Sabin Gurung and Guillem Rosselló" + "\n";
		JOptionPane optionPane =
			new JOptionPane(strTexto,JOptionPane.INFORMATION_MESSAGE);
		optionPane.setOptions(strBotones);
		JDialog dialogOptionPane = optionPane.createDialog(new JFrame(),strTitulo);
		dialogOptionPane.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialogOptionPane.pack();
		dialogOptionPane.setVisible(true);
	}

}
