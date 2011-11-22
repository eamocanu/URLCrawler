package view;

import model.MutexList;

/** Controller interface. Binds UI and Model together */
public interface Controller {
	/** Set number of threads in model
	 * 
	 * @param numThreads	the number of threads
	 */
	public void setModelNumThreads(int numThreads);	
	
	
	/** Start the UI. Called by UI thread to start UI */
	public void runUI();
	
	
	/** Update UI - threads must have finished so a repaint is in order */
	public void updateUI();
	
	
	/** Set number of links found on this page. Send this to the UI.
	 * 
	 * @param size	the number of links found
	 */
	public void setUINumLinks(int size);
	
	
	/** Send good and invalid URLs to the UI
	 *  
	 * @param badUrls	the good URLs list
	 * @param goodUrls	the invalid URLs list
	 */
	public void setUILists(MutexList badUrls, MutexList goodUrls);
	
	
	/** Set start page URL
	 * 
	 * @param pageUrl	the URL to use as start page
	 */
	public void setModelPageLink(String pageUrl);
	
	
	/** Terminate current model computation */
	public void restartModel();

}
