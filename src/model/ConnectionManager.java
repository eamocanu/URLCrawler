package model;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 */

/** Manager of URL connection. Used to connect to URLs and read page contents
 *  
 * @author Emanuel
 */
public class ConnectionManager {
	/** Connection status */
	public enum STATUS {SUCCESSFUL, TIMEDOUT, FAILED, WAITING, FAILED_READING_PAGE }
	
	/** URL to perform connection on */
	private URL url=null;
	
	/** Holds connection status */
	private STATUS connectionStatus;
	
	/** Timeout timer for current connection */
	private Timer timer=null;
	
	/** Countdown task - for connection timeout */
	private TimeoutTask timeoutTask;
	
	/** Max link timeout value */
	private final int TIMEOUT= 10000;
	
	/** Indication that link checking should be terminated or not */
	private volatile boolean done;
	
	
	
	public ConnectionManager(){
		connectionStatus=STATUS.WAITING;
	}
	
	
	/** Attempt a connection on the URL passed in
	 * 
	 * @param stringUrl		the URL to connect to
	 * @return				a URL object if a valid URL is passed int
	 * 						null object, otherwise
	 */
	@SuppressWarnings("finally")
	public URL connect(String stringUrl){
		//wait 10 secs... if no reply then timed out
		startTimer();
		done=false;
		
		url= null;
		try {
			url = new URL(stringUrl);
		} catch (MalformedURLException e) {
			//url format problem
			connectionStatus=STATUS.FAILED;
		} finally {
			stopTimer();
			connectionStatus=STATUS.SUCCESSFUL;
			return url;
		}
		
	}
	

	/** Read contents of page at location given by url 
	 * 
	 * @param url	the page location URL
	 * @return		string of contents of the page
	 * 				null if it can't read page
	 */
	public String readPage(URL url){
		//wait 10 secs... if no reply then timed out
		startTimer();
		done=false;
		
		InputStream is=null;
		StringBuffer pageContents=null;
		try {
			
			URLConnection conn= url.openConnection();
			conn.setReadTimeout(1000);

			is= conn.getInputStream();//this blocks
			BufferedReader bf= new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));

			String lineContents;
			pageContents = new StringBuffer();

			while (!done && (lineContents = bf.readLine()) != null){
				pageContents.append(lineContents + "\n");
			}

		} catch (Exception e) {
			//can't open input stream
			connectionStatus=STATUS.FAILED;
		} finally {

			try {
				is.close();
			} catch (final Exception e){
				connectionStatus=STATUS.FAILED_READING_PAGE;
				stopTimer();
				return null;
			}
			
			if (done){
				stopTimer();//stop it ASAP
				connectionStatus=STATUS.TIMEDOUT;
				return null;
			} else {
				stopTimer();
				connectionStatus=STATUS.SUCCESSFUL;
				return pageContents.toString();
			}
		}
		
	}
	
	
	/** Cancel trying to get contents from connection */
	private void cancel() {
		done = true;
	}
	
	
	/** Terminates current attempt to read from connection */
	public void terminate(){
		stopTimer();
	}
	
	
	/** Get connection status
	 * 
	 * @return	the status of the connection
	 */
	public STATUS getConnectionStatus(){
		return connectionStatus;
	}
	
	
	/** Stop timeout timer */
	private void stopTimer() {
		if (timer!=null){
			timer.cancel();
			timer.purge();
		}
		if (timeoutTask!=null){
			timeoutTask.terminate();
		}
		timer=null;
		timeoutTask=null;
	}

	
	/** Start timeout timer */
	private void startTimer() {
		timeoutTask= new TimeoutTask(this, TIMEOUT);
		timer= new Timer();
		timer.schedule(timeoutTask, 0, 100);
	}


	/** A timeout task which expires in 10 seconds if not manually canceled before that */
	private final class TimeoutTask extends TimerTask {
		//time values
		private long start, end, maxTimeout;
		
		/** Indicates when the thread is done */
		private boolean done;
		
		private ConnectionManager connectionManager=null;
		
		
		
		/** 
		 * @param connectionManager 	connection manager which owns this task
		 * @param maxTimeout			how long until it times out (self cancels)
		 */
		public TimeoutTask(ConnectionManager connectionManager, long maxTimeout){
			start= System.currentTimeMillis();
			this.maxTimeout=maxTimeout;
			this.connectionManager= connectionManager;
		}
		
		
		/** Terminate timer task*/
		public void terminate(){
			connectionManager.cancel();
			done= true;
			this.cancel();
		}
		
		
		@Override
		public void run() {
			if (!done){
				connectionStatus=STATUS.TIMEDOUT;
				end= System.currentTimeMillis();
				
				if (end-start >= maxTimeout){
					terminate();
				}
			}
		}

		
	}




	
}
