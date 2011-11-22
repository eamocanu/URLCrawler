package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

/** The statistics panel. Shows a pie chart.
 * 
 * @author Emanuel
 */
public class StatsPanel extends JPanel {
	private static final long serialVersionUID = -7246297329435825034L;

	/** Link status */
	public enum LINK_TYPE {DEAD, ALIVE, UNCHECKED}
	
	/** Pie radius */
	private final double RADIUS = 100;
	
	/** Number of valid links */
	private int numGoodLinks;
	
	/** Number of invalid links */
	private int numBadLinks;
	
	/** Number of total links */
	private int numTotalLinks;
	
	
	
	/** Create a new statistics panel ie: the pie chart */
	public StatsPanel(){
		numTotalLinks=1;
		numGoodLinks=0;
		numBadLinks=0;
		setPreferredSize(new Dimension((int)200,(int)200));
	}
	
	
	/** Create a new statistics panel to draw a specified number of links
	 * 
	 * @param numLinks	the maximum number of links on page
	 */
	public StatsPanel(int numLinks){
		this();
		setLinks(numLinks);
	}
	
	
	/** Set the total number of links.
	 * 
	 * @param numLinks	the total number of links
	 */
	public void setLinks(int numLinks){
		if (numLinks ==0) return;
		numTotalLinks= numLinks;
	}
	
	
	@Override
	public void paint(Graphics g){
		//draw background
		g.setColor(getColour(LINK_TYPE.UNCHECKED));
		g.fillArc(0,0, (int)RADIUS, (int)RADIUS, 0, 360);
		
		//draw valid links
		double angleEnd= drawSlice(g, 0, numGoodLinks, getColour(LINK_TYPE.ALIVE));

		//draw invalid links
		drawSlice(g, angleEnd, numBadLinks, getColour(LINK_TYPE.DEAD));
		
		//draw percentage done
		String pctDone=""+(100*(numBadLinks+numGoodLinks) / numTotalLinks) + "%";
		g.setXORMode(getBackground());
		FontMetrics fm = g.getFontMetrics(getFont());
		int strLen=fm.stringWidth(pctDone);//used to center the text 
		g.drawString(pctDone, (int)RADIUS/2-(int)(strLen/2), (int)RADIUS/2);
	}

	
	/** Draw a pie slice with the specified parameters.
	 * 
	 * @param g				the graphics context to use
	 * @param startAngle	start angle
	 * @param numLinks		the number of links out of total to draw
	 * @param colour		colour to draw in 
	 * 
	 */
	private double drawSlice(Graphics g, double startAngle, double numLinks, Color colour) {
		double arcAngle;
		double angleUnits=(100*numLinks/numTotalLinks);
		arcAngle= angleUnits*360/100;
		
		//fix rounding errors
		if (arcAngle>0  &&  arcAngle<1){
			arcAngle=1;
		}
		//fix rounding errors
		if ( startAngle+arcAngle>360){
			arcAngle=360-startAngle;
		}
		
		g.setColor(colour);
		g.fillArc(0,0, (int)RADIUS, (int)RADIUS, (int)startAngle, (int)arcAngle);
		return (startAngle+arcAngle);
	}


	/** Get colour based on link type
	 * 
	 * @param link
	 * @return
	 */
	private Color getColour(LINK_TYPE link) {
		if (link == LINK_TYPE.DEAD){
			return Color.RED;
		} else if (link == LINK_TYPE.ALIVE){
			return Color.GREEN;
		}
		
		return Color.GRAY;
	}


	/** Set number of valid and invalid URLs
	 * 
	 * @param numGoodLinks	valid links list
	 * @param numBadLinks	invalid links list
	 */
	public void setData(int numGoodLinks, int numBadLinks) {
		this.numGoodLinks=numGoodLinks;
		this.numBadLinks=numBadLinks;
	}
	
	
}
