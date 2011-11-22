package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import utils.CommonUtils;

import model.MutexList;


/**
 * @author Emanuel
 * 
 * Main UI window. The UI code I generated with a UI building tool.
 */
public class UIComponents extends JFrame {
	private static final long serialVersionUID = -4853853432434583170L;
	private Controller controller=null;
	private UIComponents instance;
	
	
	/** Create components, initialize them, add listeners */
	public UIComponents(Controller controller) {
		instance= this;
		this.controller=controller;
		setTitle("Link Checker");
		
		initComponents();
		parentPanel.setPreferredSize(new Dimension(500,400));
		parentPanel.setMinimumSize(new Dimension(500,400));
		parentPanel.setMaximumSize(new Dimension(500,400));
		
		topLeftPanel.setPreferredSize(new Dimension(300,110));
		topLeftPanel.setMinimumSize(new Dimension(300,110));
		topLeftPanel.setMaximumSize(new Dimension(300,110));
		
		pack();
		addListeners();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	
	/** Add listeners to this UI */
	private void addListeners() {
		startButton.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			        	
			        	String txt = numThreadsField.getText();
			        	try {
			        		//check URL validity
			        		if (urlTextField.getText().trim().length() == 0){
			        			CommonUtils.showSaveErrorDialog(instance, "Please enter a URL.");
			        		}
			        		
			        		//reset UI text
			        		numTotalLinksLabel.setText("0");
			        		setRuntime(0);
			        					        		
			        		//set number of threads
			        		int textToNum= Integer.parseInt(txt);
			        		controller.setModelNumThreads( textToNum );
			        		
			        		//set URL
			        		controller.setModelPageLink(urlTextField.getText());
			        			
			        		//start
			        		controller.restartModel();
			        	}  catch (NumberFormatException ex){
			        		//number of threads expects an int
			        		CommonUtils.showSaveErrorDialog(instance, "Number of threads has to be a number.");
			        	}  catch (Exception ex){
			        		CommonUtils.showSaveErrorDialog(instance, "Something went very wrong.");
			        	}
			        	
			        }
			    }
			);
	}


	/** Update runtime in mili seconds
	 * 
	 * @param time	the runtime in mili seconds
	 */
	public void setRuntime(long time) {
		numRuntimeLabel.setText(""+time+"ms");
	}
	
	
	/** Update status of this UI */
	public void updateStats() {
		topRightStatsPanel.repaint();
	}
	
	
	/** Set total number of links found on webpage
	 * 
	 * @param size
	 */
	public void setNumLinks(int size) {
		topRightStatsPanel.setLinks(size);
		numTotalLinksLabel.setText(""+size);
	}
	

	/** Update UI fields with good and bad URLs as well as the pie chart.
	 * 
	 * @param badUrls	list of bad URLs
	 * @param goodUrls	list of good URLs
	 */
	public void setLists(MutexList badUrls, MutexList goodUrls) {
		workingLinksPane.setText(goodUrls.toStrings());
		brokenLinksPane.setText(badUrls.toStrings());
		
		topRightStatsPanel.setData(goodUrls.getSize(), badUrls.getSize());
//		topPanel.setPreferredSize(new Dimension( this.getMaximumSize()) );
		topRightStatsPanel.repaint();
		
	}
	
	
	///////////////////////////////////////////////////////////
	//The code which follows was generated with a UI tool
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		parentPanel = new JPanel();
		topPanel = new JPanel();
		topLeftPanel = new JPanel();
		topLeftPanel1 = new JPanel();
		urlLabel = new JLabel();
		urlTextField = new JTextField();
		threadsLabel = new JLabel();
		numThreadsField = new JTextField();
		numThreadsField.setText("5");
		
		topLeftPanel2 = new JPanel();
		startButton = new JButton();
		
		topRightStatsPanel = new StatsPanel(); //new JPanel(); //XXX
			
		midPanel = new JPanel();
		midLeftPanel = new JPanel();
		workingLinksLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		workingLinksPane = new JEditorPane();
		midRightPanel = new JPanel();
		brokenLinksLabel = new JLabel();
		scrollPane2 = new JScrollPane();
		brokenLinksPane = new JEditorPane();
		botPanel = new JPanel();
		botLeftPanel = new JPanel();
		totalLinksLabel = new JLabel();
		numTotalLinksLabel = new JLabel();
		botRightPanel = new JPanel();
		runtimeLabel = new JLabel();
		numRuntimeLabel = new JLabel();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());

		//======== parentPanel ========
		{

//			// JFormDesigner evaluation mark
//			parentPanel.setBorder(new javax.swing.border.CompoundBorder(
//				new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
//					"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
//					javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
//					java.awt.Color.red), parentPanel.getBorder())); parentPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

			parentPanel.setLayout(new BoxLayout(parentPanel, BoxLayout.Y_AXIS));

			//======== topPanel ========
			{
				topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

				//======== topLeftPanel ========
				{
					topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.Y_AXIS));

					//======== topLeftPanel1 ========
					{
						topLeftPanel1.setLayout(new GridLayout(2, 2));

						//---- urlLabel ----
						urlLabel.setText("URL");
						topLeftPanel1.add(urlLabel);
						topLeftPanel1.add(urlTextField);
						

						//---- threadsLabel ----
						threadsLabel.setText("Max threads");
						topLeftPanel1.add(threadsLabel);
						topLeftPanel1.add(numThreadsField);
					}
					topLeftPanel.add(topLeftPanel1);

					//======== topLeftPanel2 ========
					{
						topLeftPanel2.setLayout(null);

						//---- startButton ----
						startButton.setText("Check Links");
						topLeftPanel2.add(startButton);
						startButton.setBounds(20, 5, 200, startButton.getPreferredSize().height);

						{ // compute preferred size
							Dimension preferredSize = new Dimension();
							for(int i = 0; i < topLeftPanel2.getComponentCount(); i++) {
								Rectangle bounds = topLeftPanel2.getComponent(i).getBounds();
								preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
								preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
							}
							Insets insets = topLeftPanel2.getInsets();
							preferredSize.width += insets.right;
							preferredSize.height += insets.bottom;
							topLeftPanel2.setMinimumSize(preferredSize);
							topLeftPanel2.setPreferredSize(preferredSize);
						}
					}
					topLeftPanel.add(topLeftPanel2);
				}
				topPanel.add(topLeftPanel);

				//======== topRightStatsPanel ========
				{
					topRightStatsPanel.setLayout(new FlowLayout());
				}
				topPanel.add(topRightStatsPanel);
			}
			parentPanel.add(topPanel);

			//======== midPanel ========
			{
				midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));

				//======== midLeftPanel ========
				{
					midLeftPanel.setLayout(new BoxLayout(midLeftPanel, BoxLayout.Y_AXIS));

					//---- workingLinksLabel ----
					workingLinksLabel.setText("Working Links");
					midLeftPanel.add(workingLinksLabel);

					//======== scrollPane1 ========
					{
						scrollPane1.setViewportView(workingLinksPane);
					}
					midLeftPanel.add(scrollPane1);
				}
				midPanel.add(midLeftPanel);

				//======== midRightPanel ========
				{
					midRightPanel.setLayout(new BoxLayout(midRightPanel, BoxLayout.Y_AXIS));

					//---- brokenLinksLabel ----
					brokenLinksLabel.setText("Broken Links");
					midRightPanel.add(brokenLinksLabel);

					//======== scrollPane2 ========
					{
						scrollPane2.setViewportView(brokenLinksPane);
					}
					midRightPanel.add(scrollPane2);
				}
				midPanel.add(midRightPanel);
			}
			parentPanel.add(midPanel);

			//======== botPanel ========
			{
				botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.X_AXIS));

				//======== botLeftPanel ========
				{
					botLeftPanel.setLayout(new FlowLayout());

					//---- totalLinksLabel ----
					totalLinksLabel.setText("Total Links");
					botLeftPanel.add(totalLinksLabel);

					//---- numTotalLinksLabel ----
					numTotalLinksLabel.setText("0");
					botLeftPanel.add(numTotalLinksLabel);
				}
				botPanel.add(botLeftPanel);

				//======== botRightPanel ========
				{
					botRightPanel.setLayout(new FlowLayout());

					//---- runtimeLabel ----
					runtimeLabel.setText("Runtime");
					botRightPanel.add(runtimeLabel);

					//---- numRuntimeLabel ----
					numRuntimeLabel.setText("0");
					botRightPanel.add(numRuntimeLabel);
				}
				botPanel.add(botRightPanel);
			}
			parentPanel.add(botPanel);
		}
		contentPane.add(parentPanel);
		
		parentPanel.setPreferredSize(new Dimension(600,300));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		
		
		
	}

	
	private JPanel parentPanel;
	private JPanel topPanel;
	private JPanel topLeftPanel;
	private JPanel topLeftPanel1;
	private JLabel urlLabel;
	private JTextField urlTextField;
	private JLabel threadsLabel;
	private JTextField numThreadsField;
	private JPanel topLeftPanel2;
	private JButton startButton;
	private StatsPanel topRightStatsPanel;
	private JPanel midPanel;
	private JPanel midLeftPanel;
	private JLabel workingLinksLabel;
	private JScrollPane scrollPane1;
	private JEditorPane workingLinksPane;
	private JPanel midRightPanel;
	private JLabel brokenLinksLabel;
	private JScrollPane scrollPane2;
	private JEditorPane brokenLinksPane;
	private JPanel botPanel;
	private JPanel botLeftPanel;
	private JLabel totalLinksLabel;
	private JLabel numTotalLinksLabel;
	private JPanel botRightPanel;
	private JLabel runtimeLabel;
	private JLabel numRuntimeLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables




}
