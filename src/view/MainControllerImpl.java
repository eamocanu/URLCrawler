/**
 * 
 */
package view;

import model.Model;
import model.MutexList;

/** Controller which binds the UI and model together.
 * 
 * @author Emanuel
 */
public class MainControllerImpl implements Controller {
	/** The model */
	private Model model=null;
	
	/** The UI */
	private UIComponents window=null;
	
	/** Thread on which the UI will run */
	private Thread uiThread=null;
	
	/** Lock used for synchronization between UI and model */
	private Object modelLock=null;
	
	/** Model variables: page URL to scan */
	private String pageUrl=null;
	
	/** Model variables: number of threads to use */
	private int numModelThreads=5;
	
	/** Runtime statistics data */
	private long startTime, endTime;
	
	/** Thread which starts model */
	Thread modelThread=null;
	
	/** Thread which updates time spend on processing in the UI component */
	RuntimeUpdateThread runtimeThread=null;
	
	
	/** Create a UI and put it on a new thread */
	public MainControllerImpl(){
		modelLock=new Object();
		model= new Model(this);
		window=new UIComponents(this);
		
		uiThread= new Thread(new UIRunnable( this ));
		uiThread.start();
		
		synchronized(modelLock){
			try {
				do {
					modelLock.wait();
					
					startTime= System.currentTimeMillis();
					
					runtimeThread= new RuntimeUpdateThread();
					runtimeThread.start();
					
					model.checkPageForDeadLinks(pageUrl,numModelThreads);
					endTime= System.currentTimeMillis();
					runtimeThread.terminate();
					window.setRuntime(endTime-startTime);
				} while (true);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	@Override
	/** Set number of threads in model
	 * 
	 * @param numThreads	the number of threads
	 */
	public void setModelNumThreads(int numThreads){
		this.numModelThreads= numThreads;
	}

	
	@Override
	/** Set start page URL
	 * 
	 * @param pageUrl	the URL to use as start page
	 */
	public void setModelPageLink(String pageUrl){
		this.pageUrl= pageUrl;
	}
	
	
	/** Restart model to validate links */
	public void restartModel(){
		//add a guard here in case the user goes crazy and keeps on clicking
		//instead of spawning a million threads and then terminating them
		//wait until the current one is finished terminating and start only one new thread
		if (modelThread==null ||(modelThread!=null && !modelThread.isAlive())){
			//put on a new thread because URL connection blocks - it can't be forcefully 
			//terminated in the model so we don't want the UI to block as well
			modelThread = new Thread(){
				public void run() {
					model.terminate();//blocks on URL connection
					startModel();
				}
			};
			modelThread.start();
		}
	}
	
	
	/** Resume running model */
	private void startModel(){
		synchronized(modelLock){
			modelLock.notify();
		}
	}
	
	
	@Override
	/** Update UI - threads must have finished so a repaint is in order */
	public void updateUI(){
		//also set how much time has passed up to now
		window.setRuntime(System.currentTimeMillis()-startTime);//TODO add on a new thread and update every second
		window.updateStats();
	}
	
	
	@Override
	/** Start the UI. Called by UI thread to start UI */
	public void runUI() {
		try {
			//allow model thread to catch up
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		window.setVisible(true);
		window.pack();
	}


	@Override
	/** Set number of links found on this page. Send this to the UI.
	 * 
	 * @param size	the number of links found
	 */
	public void setUINumLinks(int size) {
		window.setNumLinks(size);
	}


	@Override
	/** Send good and invalid URLs to the UI
	 *  
	 * @param badUrls	the good URLs list
	 * @param goodUrls	the invalid URLs list
	 */
	public void setUILists(MutexList badUrls, MutexList goodUrls) {
		window.setLists(badUrls, goodUrls);
	}
	
	
	/** This thread updates the runtime on the UI components */
	private final class RuntimeUpdateThread extends Thread {
		private boolean done;

		public void run() {
			while (!done){
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				window.setRuntime(System.currentTimeMillis()-startTime);
			}
		}
		
		public void terminate(){
			done= true;
		}
	}
	
}
