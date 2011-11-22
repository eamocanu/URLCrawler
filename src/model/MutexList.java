package model;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Monitor List of String type for concurrent operations
 * @author Emanuel
 */
public class MutexList {
	private volatile List<String> list;
	
	
	public MutexList(){
		list=new ArrayList<String>();
	}
	
	
	/** Add a new element to this list
	 * 
	 * @param content	the element to add
	 */
	public synchronized void add(String content){
		if (list==null)
			return;
		
		list.add(content);
	}
	
	
	/** Returns element at the head of this list.
	 * 
	 * @return	head of list
	 * 			null, if list is empty
	 */
	public synchronized String get(){
		if (list==null  &&  list.size()<0){
			return null;
		}

		String result=list.get(0);
		list.remove(0);

		return result;
	}


	/** Checks is list is empty
	 * 
	 * @return	true, if list is empty
	 * 			false otherwise
	 */
	public synchronized boolean isEmpty(){
		if (list==null) return true;
		return list.size()<1;
	}
	
	
	/** Create a monitor list from list passed in
	 * 
	 * @param newList	the list to use as source
	 */
	public synchronized void setContent(List<String> newList){
		list= newList;
	}


	/** String representation of this list
	 * 
	 * @return	string representation of this list
	 */
	public synchronized String toStrings() {
		StringBuffer result=new StringBuffer("");
		if (list!=null)
			for (ListIterator<String> it=list.listIterator(); it.hasNext();){
				result.append(it.next());
				result.append("\n");
			}
		return result.toString();
	}

	
	/** Get size of this list
	 * 
	 * @return	the list size
	 */
	public synchronized int getSize(){
		if (list==null)return 0;
		return list.size();
	}


	/** Clean up this list */
	public void discard() {
		
		if (list!=null && list.size()>0){
			list.removeAll(list);
			list=null;
		}
	}
	
}
