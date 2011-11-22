package view;



/** Used to put the UI on a new thread.
 * 
 * @author Emanuel
 */
public class UIRunnable implements Runnable {
	private Controller controller=null;

	/** 
	 * @param ui	the controller to use to start the UI
	 */
	public UIRunnable(Controller ui) {
		this.controller=ui;
	}
	
	@Override
	public void run() {
		controller.runUI();
	}

}
