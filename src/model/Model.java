package model;


import java.net.URL;
import java.util.List;

import view.Controller;



/**
 * @author Emanuel
 *
 */
public class Model {
	/** Holds list of links to check */
	private List<String> pageLinks=null;
	
	/** Stores all worker threads */
	private WorkerThread [] threads=null;
	
	/** The controller which binds model to UI */
	private Controller controller=null;
	
	/** Containers for soring valid and invalid URLs */
	private MutexList goodUrls, badUrls;
	
	/** Lock for synchronizing between model and worker threads */
	private Object lock=new Object();
	
	
	/**
	 * @param window	the controller which allows this model to communicate with the UI
	 */
	public Model(Controller window) {
		this.controller=window;
		goodUrls=new MutexList ();
		badUrls=new MutexList ();
	}

	
	/** Check page links at the URL passed in.
	 * Returns only when all threads are done.
	 * 
	 * @param pageUrl	the URL to load and check for links
	 */
	public void checkPageForDeadLinks(String pageUrl, int numThreads){
		threads= new WorkerThread[numThreads];
		ConnectionManager cm= new ConnectionManager();
		String contents = "";
		
		URL url=cm.connect(pageUrl);
		PageParser p= new PageParser();

		try {
			if (url!=null){
				contents = cm.readPage(url);
				pageLinks= p.parsePageIntoLinks(contents, pageUrl);
				if (pageLinks.size()<1) return;//early exit
				
				//set UI parameter
				controller.setUINumLinks(pageLinks.size());

				//use a monitor list
				MutexList mutexLinkList= new MutexList();
				mutexLinkList.setContent(pageLinks);

				//make the threads
				for (int i=0; i<threads.length; i++){
					threads[i]= new WorkerThread("T"+i, goodUrls, badUrls);
					threads[i].setWork(mutexLinkList);
				}

				//start threads
				for (int i=0; i<threads.length; i++){
					threads[i].start();
				}

				controller.setUILists(badUrls, goodUrls);
				controller.updateUI();

				//update UI as threads finish parts
				synchronized(lock){
					do {
						try {
							lock.wait();
							controller.setUILists(badUrls, goodUrls);
							controller.updateUI();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} while(!isDone());
				}

				controller.setUILists(badUrls, goodUrls);
				controller.updateUI();

				//clean up resources
				terminate();
			}//if
		} catch(Exception e){
			
		}
	}
	

	/** Checks if all threads are done checking the URLs
	 * 
	 * @return 	true if all threads are done
	 * 			false, otherwise
	 */
	public synchronized boolean isDone(){
		if (threads!=null)
		for (int i=0; i<threads.length; i++){
			if (threads[i]!=null && threads[i].isAlive()){
				return false;
			}
		}
		return true;
	}
	
	
	/** Terminate all worker threads, clean up resources and get ready for new run.
	 * Used when user clicks the check links button before the
	 * current link checking is done. 
	 */
	public void terminate() {
		if (threads!=null)
			for (int i=0; i<threads.length && threads[i]!=null; i++){
				threads[i].terminate();
				threads[i]=null;
			}
		threads=null;
		
		goodUrls.discard();
		badUrls.discard();
		
		goodUrls=new MutexList();
		badUrls=new MutexList();
	}
	
	
	
	
	/** URL checker worker thread */
	private final class WorkerThread extends Thread {
		/** what the thread consumes */
		private MutexList workList;
		
		/** where the thread puts results */
		private MutexList goodUrls, badUrls;
		
		/** Indicates when the thread is done */
		private boolean done;
		
		/** Used for initiating connections and reading page contents */
		private ConnectionManager cm=null;
		
		
		/**
		 * @param threadName	thread name
		 * @param goodUrls		where to save valid URLs
		 * @param badUrls		where to save invalid URLs
		 */
		public WorkerThread(String threadName, MutexList goodUrls, MutexList badUrls) {
			super(threadName);
			saveQueuesLocally(goodUrls, badUrls);
		}
		
		
		/** Keep pointers to where to store results locally.
		 * 
		 * @param goodUrls	where to store valid URLs
		 * @param badUrls	where to store invalid URLs
		 */
		private void saveQueuesLocally(MutexList goodUrls, MutexList badUrls) {
			this.goodUrls=goodUrls;
			this.badUrls=badUrls;
		}
		
		
		/** Set work for current thread */
		public void setWork(MutexList pageLinks){
			this.workList=pageLinks;
		}
		
		
		@Override
		public void run(){
			//wait for dispatcher to dispatch all threads
			//and reach waiting point before notifying
			try {
				sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			cm= new ConnectionManager();
			
			//let thread work until there is no more work or until a terminate call is made
			while (!done && !workList.isEmpty()){
				String crtLink= workList.get();
				URL url= cm.connect(crtLink);
				String contents = null;
				
				if (url!=null){
					contents = cm.readPage(url);
					
					if (contents!=null){
						//success :)
						goodUrls.add(url.toString());
					} else {
						//fail
						badUrls.add(crtLink);
					}
				} else {
					//fail
					badUrls.add(crtLink);
				}
				
				//let UI know 1 URL is done so repaint
				synchronized(lock){
					lock.notifyAll();
				}

			}//while
			
			terminate();
			
			//let model know the thread is done
			synchronized(lock){
				lock.notifyAll();
			}
		}//run
		
		
		/** Premature termination of worker thread is requested.
		 * Used when user clicks the check links button before the
		 * current link checking is done. 
		 */
		public void terminate(){
			done= true;
			if (cm!=null){
				cm.terminate();
			}
		}
		
	}//WorkerThread

	
	

}
