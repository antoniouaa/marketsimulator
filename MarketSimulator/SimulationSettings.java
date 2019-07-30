package MarketSimulator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;

/**
*	The SimulationSettings class is responsible for creating
*	the window holding all the settings available to the user in 
*	order to customise the simulation to fit their needs
*
*	Settings available:
*	- Number of agents
*	- Number of stocks
*	- Length of simulation
*	- Overcommitment traits for agents	
*
*	@author Alexandros Antoniou
*/
@SuppressWarnings("serial")
public class SimulationSettings extends JFrame implements ActionListener, ChangeListener, DocumentListener {

	public static final int AGENTS_MIN = 2;
	public static final int AGENTS_MAX = 50;
	public static final int AGENTS_INIT = 5;
	public static final int STOCKS_MIN = 1;
	public static final int STOCKS_MAX = 10;
	public static final int STOCKS_INIT = 10;
	public static final int DAYS_INIT = 50;

	AdvancedSettings advSettings;
	JPanel settingsPanel;
	JPanel buttonsPanel;
	JPanel preview;
	JPanel agentPanel;
	JPanel stockPanel;
	JPanel daysPanel;
	JPanel previewAgentP;
	JPanel previewStockP;
	JPanel previewDaysP;
	JPanel descriptionDays;
	JLabel agentNumber;
	JLabel stockNumber;
	JLabel daysNumber;
	JLabel daysNumberDescription;
	JLabel previewAgent;
	JLabel previewStock;
	JLabel previewDays;
	JTextField previewAgentVal;
	JTextField previewStockVal;
	JTextField previewDaysVal;
	JSlider agents;
	JSlider stocks;
	JTextField days;
	JButton advancedSettings;
	JButton startSim;
	JButton resetValues;
	JButton exitSim;
	TitledBorder settingsTitleBorder;
	TitledBorder previewTitleBorder;
	EmptyBorder settingsMargins;
	EmptyBorder smallPanelMargins;
	GridLayout settingLayout;
	Font previewFont;
	private int agentsPop, stocksPop, simLength, overcommitA, overcommitB, overcommitC;	
	private Set<Integer> overcommitSet;

	SimulationSettings() {
		setTitle(MarketPlace.TITLE_STRING);
		showFrame();
		advSettings = new AdvancedSettings(this);
	}

	/**
	*	All the components that are shown in the window are initialised here
	*	and subsequently added to the container
	*	@param pane reference to the container holding the components
	*/
	void addComponents(Container pane) {

		settingLayout = new GridLayout(1, 2);

		settingsTitleBorder = new TitledBorder(new LineBorder(Color.BLACK), "Simulation Settings");
		previewTitleBorder = new TitledBorder(new LineBorder(Color.BLACK), "Settings Preview");
		settingsMargins = new EmptyBorder(10, 10, 10, 10);
		smallPanelMargins = new EmptyBorder(5, 10, 5, 10);

		settingsPanel = new JPanel();
		buttonsPanel = new JPanel();
		preview = new JPanel();

		settingsPanel.setLayout(new GridLayout(4, 1, 10, 10));
		preview.setLayout(new GridLayout(3, 2));

		previewFont = new Font("Arial", Font.BOLD, 22);

		agentPanel = new JPanel();
		stockPanel = new JPanel();
		daysPanel = new JPanel();
		descriptionDays = new JPanel();

		agentPanel.setLayout(settingLayout);
		stockPanel.setLayout(settingLayout);
		daysPanel.setLayout(settingLayout);

		previewAgentP = new JPanel();
		previewStockP = new JPanel();
		previewDaysP = new JPanel();

		previewAgentP.setLayout(settingLayout);
		previewStockP.setLayout(settingLayout);
		previewDaysP.setLayout(settingLayout);

		agentNumber = new JLabel("\tNumber of Agents");
		stockNumber = new JLabel("\tNumber of Stocks");
		daysNumber = new JLabel("<html>\tNumber of days<br> the simulation will run</html>");
		daysNumberDescription = new JLabel("<html>One day equals one transaction cycle</html>");
		daysNumberDescription.setHorizontalTextPosition(SwingConstants.LEFT);
		daysNumberDescription.setFont(new Font("Arial", Font.ITALIC, 12));
		daysNumberDescription.setBorder(new EmptyBorder(0, 0, 0, 50));
		
		agents = new JSlider(JSlider.HORIZONTAL, AGENTS_MIN, AGENTS_MAX, AGENTS_INIT);
		agents.setMajorTickSpacing(10);
		agents.setMinorTickSpacing(1);
		agents.setPaintTicks(true);
		agents.setPaintLabels(true);
		agents.setSnapToTicks(true);
		agents.addChangeListener(this);

		stocks = new JSlider(JSlider.HORIZONTAL, STOCKS_MIN, STOCKS_MAX, STOCKS_INIT);
		stocks.setMajorTickSpacing(2);
		stocks.setMinorTickSpacing(1);
		stocks.setPaintTicks(true);
		stocks.setPaintLabels(true);
		stocks.setSnapToTicks(true);
		stocks.addChangeListener(this);

		days = new JTextField(""+DAYS_INIT);
		days.setFont(new Font("Arial", Font.PLAIN, 18));
		days.getDocument().addDocumentListener(this);
		days.setHorizontalAlignment(JTextField.RIGHT);

		previewAgent = new JLabel("<html>The simulation will <br>contain this many agents:</html>");
		previewStock = new JLabel("<html>The agents will <br>trade this many stocks:</html>");
		previewDays = new JLabel("<html>The simulation will run <br>for this many days:</html>");

		previewAgentVal = new JTextField();
		previewStockVal = new JTextField();
		previewDaysVal = new JTextField();

		previewAgentVal.setHorizontalAlignment(JTextField.RIGHT);
		previewStockVal.setHorizontalAlignment(JTextField.RIGHT);
		previewDaysVal.setHorizontalAlignment(JTextField.RIGHT);

		previewAgentVal.setFont(previewFont);
		previewStockVal.setFont(previewFont);
		previewDaysVal.setFont(previewFont);

		previewAgentVal.setText(""+agents.getValue());
		previewStockVal.setText(""+stocks.getValue());
		previewDaysVal.setText(days.getText());

		previewAgentVal.setEditable(false);
		previewStockVal.setEditable(false);
		previewDaysVal.setEditable(false);

		advancedSettings = new JButton("Advanced Settings");
		startSim = new JButton("Start Simulation");
		resetValues = new JButton("Reset Values");
		exitSim = new JButton("Exit Simulation");

		advancedSettings.addActionListener(this);
		startSim.addActionListener(this);
		resetValues.addActionListener(this);
		exitSim.addActionListener(this);

		agentPanel.add(agentNumber);
		agentPanel.add(agents);
		stockPanel.add(stockNumber);
		stockPanel.add(stocks);
		daysPanel.add(daysNumber);
		daysPanel.add(days);
		descriptionDays.add(daysNumberDescription);
		descriptionDays.add(advancedSettings);

		settingsPanel.add(agentPanel);
		settingsPanel.add(stockPanel);
		settingsPanel.add(daysPanel);
		settingsPanel.add(descriptionDays);

		previewAgentP.add(previewAgent);
		previewAgentP.add(previewAgentVal);
		previewStockP.add(previewStock);
		previewStockP.add(previewStockVal);
		previewDaysP.add(previewDays);
		previewDaysP.add(previewDaysVal);

		preview.add(previewAgentP);
		preview.add(previewStockP);
		preview.add(previewDaysP);

		preview.setPreferredSize(new Dimension(450, 285));

		buttonsPanel.add(startSim);
		buttonsPanel.add(resetValues);
		buttonsPanel.add(exitSim);

		agentPanel.setBorder(smallPanelMargins);
		stockPanel.setBorder(smallPanelMargins);
		daysPanel.setBorder(smallPanelMargins);
		descriptionDays.setBorder(smallPanelMargins);
		previewAgentP.setBorder(smallPanelMargins);
		previewStockP.setBorder(smallPanelMargins);
		previewDaysP.setBorder(smallPanelMargins);

		settingsPanel.setBorder(new CompoundBorder(settingsMargins, settingsTitleBorder));
		preview.setBorder(new CompoundBorder(settingsMargins, previewTitleBorder));

		pane.add(settingsPanel, BorderLayout.WEST);
		pane.add(preview, BorderLayout.EAST);
		pane.add(buttonsPanel, BorderLayout.SOUTH);
	}

	void showFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponents(this.getContentPane());
		this.pack();
		this.setVisible(true);
	}

	public void changedUpdate(DocumentEvent e) {
		updateTextField(e);
	}

	public void insertUpdate(DocumentEvent e) {
		updateTextField(e);
	}

	public void removeUpdate(DocumentEvent e) {
		updateTextField(e);
	}

	public void updateTextField(DocumentEvent e) {
		previewDaysVal.setText(""+days.getText());
	}

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (source==agents && !source.getValueIsAdjusting()) {
			previewAgentVal.setText(""+source.getValue());
		}
		if (source==stocks && !source.getValueIsAdjusting()) {
			previewStockVal.setText(""+source.getValue());
		}
	}

	public void setOvercommitRisks(int ocA, int ocB, int ocC) {
		overcommitA = ocA;
		overcommitB = ocB;
		overcommitC = ocC;
	}

	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		if (source==startSim) {
			agentsPop = agents.getValue();
			stocksPop = stocks.getValue();
			simLength = Integer.parseInt(days.getText());
			overcommitSet = new HashSet<Integer>();
			overcommitSet.add(overcommitA);
			overcommitSet.add(overcommitB);
			overcommitSet.add(overcommitC);
			new MarketPlace(agentsPop, stocksPop, simLength, overcommitSet);
			this.dispose();
		}
		if (source==resetValues) {
			agents.setValue(AGENTS_INIT);
			stocks.setValue(STOCKS_INIT);
			days.setText(""+DAYS_INIT);
			overcommitA = 33;
			overcommitB = 33;
			overcommitC = 33;
			advSettings.resetSliders();
		}
		if (source==exitSim) {
			System.exit(-1);
		}
		if (source==advancedSettings) {
			if (!advSettings.isVisible()) {
				advSettings.setVisible(true);
			} else {
				advSettings.toFront();
			}
		}
	}
}