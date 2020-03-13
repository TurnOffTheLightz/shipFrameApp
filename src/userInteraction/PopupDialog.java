package userInteraction;

import javax.swing.JOptionPane;
/*
 *    Plik: PopupDialog.java
 *          
 *   Autor: Miron Oskroba
 *    Data: listopad 2019 r.
 */
public class PopupDialog {
	/*
	 * 		grants to pop message windows when needed 
	 */
	public void popMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}
	public void popMessage(int message) {//for problem detecting
		JOptionPane.showMessageDialog(null, Integer.toString(message));
	}
}
