package model;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Emanuel
 *
 * Parses page for links.
 */
public class PageParser {
	final String [] allowedPrefixes= {"www", "http://", "file://", "ftp://", "fttp://", "https://"};//etc more can be added
	
	/** Parses contents for valid links and returns them if found. 
	 * This method can be extended to parse more combinations
	 * of allowed links. For the purpose of this exercise
	 * I parse links embedded into <a href...> tags
	 * 
	 * @param contents	the contents to parse
	 * @param pageUrl	link of where the contents were taken from
	 * 					used for forming full links from relative links
	 * @return			list of valid links on page contents
	 */
	public List<String> parsePageIntoLinks(String contents, String pageUrl){
		List<String> links= new ArrayList<String>();
		
		String newContents=contents.toLowerCase();//account for case variations
		final Pattern startLinkPattern = Pattern.compile("href=");
	    String[] linkChunks= startLinkPattern.split(newContents);
	    
	    //prepare url for relative links
		if (pageUrl.endsWith("/")){//remove / so we don't have double //
			pageUrl=pageUrl.substring(0, pageUrl.length()-1);
		} else {
			pageUrl= pageUrl.substring(0, pageUrl.lastIndexOf('/'));
		}
		
		//parse
	    for(int i=0; i<linkChunks.length; i++){
	    	final String endStringDelimiter=""+linkChunks[i].charAt(0);//+">";
	    	String crtLink=null;
	    	try {
	    		crtLink= linkChunks[i].substring(1, linkChunks[i].indexOf(endStringDelimiter,1) );

	    		//create full link for relative link
	    		if(isRelativeLink(crtLink)){
	    			crtLink = pageUrl+crtLink;
	    		}
	    		
	    		if (!allowedLinkPrefix(crtLink)){
	    			//link can start w www, http, ftp, etc...
	    			//otherwise this could be javascripts/css/etc which don't need to be loaded
	    			continue;
	    		}
	    		
	    		//starts w wrong prefix, but valid link
	    		if (canBeFixed(crtLink)){
	    			crtLink= fix(crtLink);
	    		}
	    	} catch (Exception e){
	    		//invalid link
	    		continue;
	    	}
	    	links.add(crtLink);
	    }
	    
		return links;
	}

	
	/** Fix an invalid link by adding proper prefix 
	 * 
	 * @param crtLink	the link to fix
	 * @return			the fixed link
	 */
	private String fix(String crtLink) {
		return "http://"+crtLink;
	}


	/** Checks to see if a link can be fixed
	 * 
	 * @param crtLink	the link to check
	 * @return			true if it can be fixed
	 * 					false, otherwise
	 */
	private boolean canBeFixed(String crtLink) {
		return crtLink.startsWith("www.");
	}


	/** Checks if a given link is a relative link as opposed to full link
	 * 
	 * @param crtLink	the link to check
	 * @return			true if the link is relative
	 * 					false, otherwise
	 */
	private boolean isRelativeLink(String crtLink) {
		if(crtLink.startsWith("/")){
			return true;
		}
		return false;
	}

	
	/** Checks to see if given link starts with a valid URL prefix.
	 * ie: http, file, ftp, etc
	 * 
	 * @param crtLink	the link to be checked
	 * @return			true if link has allowed prefix
	 * 					false, otherwise
	 */					
	private boolean allowedLinkPrefix(String crtLink) {
		for (int i=0; i<allowedPrefixes.length; i++){
			if (crtLink.startsWith(allowedPrefixes[i])){//some start w java script; some are #
				return true;
			}
		}
		return false;
	}
	
}
