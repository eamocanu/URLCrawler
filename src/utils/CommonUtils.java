/**
 * 
 */
package utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/** Helpful utilities with commonly used functions
 * 
 * @author Emanuel
 */
public class CommonUtils {
	
	/** Helper to show an error message.
	 * 
	 * @param window	parent window to whom this error message dialog belongs to
	 * @param msg		the message to display
	 */
	public static void showSaveErrorDialog(JFrame window, String msg) {
		JOptionPane.showMessageDialog(window, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
}
