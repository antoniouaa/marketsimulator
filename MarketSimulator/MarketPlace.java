package MarketSimulator;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.Map.*;
import java.util.stream.*;
import javax.swing.*;
import javax.swing.table.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.axis.*;
import org.jfree.data.category.*;
import org.jfree.ui.*;
import org.jfree.data.*;


/**
*	The MarketPlace class serves as the environment in which the Agents, Stocks
*	Companies and their interactions exist and operate.
*	Its main purpose is to initialise instances of each of the main class objects and through 
*	performing operations enables for the definition of a time dimension.
*	After all the calculations are complete, it is responsible for the output
*	of results, in the form of graphs and tables.
*
*	Operations include the initialisation of a number of agents
*	Operations include the initialisation of a set of stocks and companies
*	Operations include the updating of stock prices
*	
*	@author Alexandros Antoniou
*/
@SuppressWarnings("serial")
public class MarketPlace extends JFrame implements ActionListener {

	public final static String TITLE_STRING = "MarketSimulator - 1606172";
	public static final int MARKETPLACE_TRADE_MAX = 100;

	private static int standardVolume = Integer.MAX_VALUE;
	private static int agentNumber = 50;
	private static int stockNumber = 10;
	private static int initialStockPerAgent = 5;
	private Random startingCashRandom = new Random(Agent.STANDARD_STARTING_CASH);
	private Random initialAgentStocks = new Random();
	private static int simulationLength = 500;
	ArrayList<Agent> agents;
	ArrayList<Stock> stocks;
	ArrayList<Company> companies;
	HashMap<Stock, Integer> agentStocks;
	ArrayList<Double> v;
	ArrayList<Double> index;
	ArrayList<Double> marketcap;

	ChartPanel stockIndex;
	ChartPanel marketcapIndex;

	JMenuItem makeNewSim;
	JMenuItem exportGraphs;
	JMenuItem exportTables;
	JMenuItem exitSim;
	JMenuItem menuHelp;

	/**
	*	Initialises list of Companies, Stocks and finally Agents
	*/
	MarketPlace(int newAgentNumber, int newStockNumber, int newDuration, Set<Integer> overcommitS) {
		super("MarketPlace");
		agentNumber = newAgentNumber;
		stockNumber = newStockNumber;
		simulationLength = newDuration;
		HashMap<ArrayList<Company>, ArrayList<Stock>> map = null;
		index = new ArrayList<Double>();
		marketcap = new ArrayList<Double>();
		try {
			map = CSVParser.parseCSV(stockNumber);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		for (Map.Entry<ArrayList<Company>, ArrayList<Stock>> e : map.entrySet()) {
			companies = (e).getKey();
			stocks = (e).getValue();
		}

		initialiseAgents();
		updateVolume();

		updateMarketPlace();
		// testPrints();
	}

	public ArrayList<Agent> getAgents() {
		return agents;
	}

	/**
	*	A random amount of starting cash is calculated, between the two boundaries and
	*	together with a HashMap<Stock, Integer> object retrieved from giveAgentStocks()
	*	a number of Agent instances is created and added them to an ArrayList object
	*/
	void initialiseAgents() {
		agents = new ArrayList<Agent>();
		for (int i = 0; i < agentNumber; i++) {
			int agentCash = startingCashRandom.nextInt((Agent.UPPER_BOUND_STARTING_CASH - Agent.LOWER_BOUND_STARTING_CASH) + 1) + Agent.LOWER_BOUND_STARTING_CASH;
			agents.add(new Agent(agentCash, giveAgentStocks()));
		}
	}

	/**
	*	Creates the HashMap that will be used to initialise agent objects with a predetermined amount of stocks
	*	A random integer decides how many stocks of each Stock an agent will own
	*	@return agentStocks a HashMap<Stock, Integer> object with stocks and the number of each stock the agent owns
	*/
	HashMap<Stock, Integer> giveAgentStocks() {
		agentStocks = new HashMap<Stock, Integer>();
		for (int i = 0; i < stocks.size(); i++) {
			int r = Math.abs(initialAgentStocks.nextInt(1000));
			agentStocks.put(stocks.get(i), r);
		}
		return agentStocks;
	}

	void updateVolume() {
		for (Agent a : agents) {
			for (Map.Entry<Stock, Integer> e : a.getAgentPortfolio().entrySet()) {
				e.getKey().getStockCompany().updateStockVolume(e.getValue());
			}
		}
	}

	/**
	*	Iterates through the ArrayList holding the agents and prints to the console their
	*	- Current Cash
	*	- Current Total Assets
	*/
	public void printAgents() {

		for (Agent a : agents) {
			String agentCashString = String.format("%.2f", a.getAgentCash());
			String agentAssetsString = String.format("%.2f", a.getAgentAssets());
			System.out.println(agentCashString + " + " + agentAssetsString);
		}
	}

	/**
	*	Iterates through the ArrayList holding the stocks and prints to the console their
	*	- Name
	*	- Company's Name
	*	- Current Stock Price
	*	- Current Stock Price Change
	*/
	public void printStocks() {

		for (Stock s : stocks) {
			double stockPrice = s.getStockPrice();
			double stockPriceChange = s.getStockPriceChange();
			String newStockPriceString = String.format("%.2f", stockPrice);
			String newStockPriceChangeString = String.format("%.2f", stockPriceChange);
			System.out.println(s.getStockName() + " " + s.getStockCompanyName() + " /New Price: " + newStockPriceString + " /New Price Change: " + newStockPriceChangeString);
		}
	}

	/**
	*	Iterates through the ArrayList holding the companies and prints to the console their
	*	- Current Market Capitalisation
	*	- Current Earnings per Stock
	*/
	public void printCompanyInfo() {

		for (int i = 0; i < companies.size(); i++) {
			companies.get(i).calcMarketCap(stocks.get(i));
			companies.get(i).calcEarningsPerStock();
			System.out.println(String.format("Market Cap: %.2fB", companies.get(i).getMarketCap()) + " " + String.format("EPS: %.2f", companies.get(i).getEarningsPerStock()));
		}
	}

	/**
	*	Iterates through the ArrayList holding the stocks and for each stock, updates their stock price
	*/
	public void updateMarketPlace() {
		for (int i = 0; i < simulationLength; i++) {
			for (Stock s : stocks) {
				for (Company c : companies) {
					if (s.getStockCompanyName().equals(c.getCompanyName())) {
						c.calcMarketCap(s);
					}
				}
				s.updateStockPrice();
			}
			
			for (Agent a : agents) {
				a.calcPortfolioVolatilities();
				a.calcPortfolioEPS();
				a.calcPortfolioPE();
				a.calcStockValue();
				a.sortStocksByValue();
			}
			Collections.shuffle(agents);
			for (Agent a : agents) {
				Agent tradeAgent = a.findTradeAgent(agents);
				a.makeTrade(tradeAgent);
			}

			calcIndex();
		}

		createGUI();
	}

	void testPrints() {
		printStocks();
		System.out.println("------------");
		printAgents();
		System.out.println("------------");
		printCompanyInfo();
		System.out.println("------------");
		for (Agent a : agents) {
			a.printAllIndices();
			System.out.println("\t" + a.getBestStockInvestment().getKey().getStockName()+
				"\t"+a.getWorstStockInvestment().getKey().getStockName());
		}
	}

	/**
	*	Calculates the two marketplace indices by aggregating the values of 
	*	each stock price and the companies' market capitalisation values
	*/
	private void calcIndex() {
		double totalPrice = 0;
		double totalmarketcap = 0;
		for (Stock s : stocks) {
			totalPrice += s.getStockPrice();
		}
		for (Company c : companies) {
			totalmarketcap += c.getMarketCap();
		}
		marketcap.add(totalmarketcap);
		index.add(totalPrice);
	}

	/**
	*	The UI is created in this method
	*/
	void createGUI() {
		Font infoFont = null;
		try {
			infoFont = getFont("SulphurPoint-Bold.otf");
		} catch(Exception e) {
			e.printStackTrace();
		}
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenu help = new JMenu("Help");
		GridLayout tableLayout = new GridLayout(2, 1);
		stockIndex = createChart("Price Index", index);
		marketcapIndex = createChart("Market capitalisation Index", marketcap);
		
		JPanel generalPanel = new JPanel(tableLayout);
		JPanel tablePanel = new JPanel(tableLayout);
		JPanel infoPanel = new JPanel(new GridLayout(1, 3)); //in generalPanel
		JTabbedPane graphPane = new JTabbedPane(); //in generalPanel
		JPanel agentTPanel = new JPanel(); //in tablePanel
		JPanel stockTPanel = new JPanel(); //in tablePanel
		JScrollPane agentPane = createAgentTable(); //in agentTPanel
		JScrollPane stockPane = createStockTable(); //in stockTPanel
		JLabel title = new JLabel(); //in infoPanel
		JPanel details = new JPanel(); //in infoPanel
		JLabel high = new JLabel(); //in details
		JLabel low = new JLabel(); //in details

		//set up the menu bar
		menuBar.add(menuFile);
		menuBar.add(help);

		//make new simulation
		makeNewSim = new JMenuItem("New Simulation");
		exportGraphs = new JMenuItem("Export Graphs");
		exportTables = new JMenuItem("Export Tables");
		exitSim = new JMenuItem("Close");
		menuHelp = new JMenuItem("About");
		makeNewSim.addActionListener(this);
		exportGraphs.addActionListener(this);
		exportTables.addActionListener(this);
		exitSim.addActionListener(this);
		menuHelp.addActionListener(this);
		menuFile.add(makeNewSim);
		menuFile.addSeparator();
		menuFile.add(exportGraphs);
		menuFile.add(exportTables);
		menuFile.addSeparator();
		menuFile.add(exitSim);
		help.addSeparator();
		help.add(menuHelp);

		/*TODO: make 2 menuFile menuItems
		*/
		title.setFont(infoFont.deriveFont(36.0f));
		title.setText("MarketPlace  "+ String.format("\t%.2f", index.get(index.size()-1)));
		high.setFont(infoFont.deriveFont(24.0f));
		high.setText("High: "+String.format("%.2f", Collections.max(index)));
		low.setFont(infoFont.deriveFont(24.0f));
		low.setText("Low: "+String.format("%.2f", Collections.min(index)));
		infoPanel.add(title);
		infoPanel.add(details);
		details.add(high);
		details.add(low);
		graphPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
		title.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 0));
		details.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 5));
		high.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 15));
		low.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
		tablePanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 10));		
		agentTPanel.add(agentPane);
		stockTPanel.add(stockPane);
		tablePanel.add(agentTPanel);
		tablePanel.add(stockTPanel);
		graphPane.addTab("Price Index", null, stockIndex, "Current market price index");
		graphPane.addTab("Market Capitalisation", null, marketcapIndex, "Current market capitalisation");
		this.setJMenuBar(menuBar);
		add(infoPanel, BorderLayout.NORTH);
		add(graphPane, BorderLayout.WEST);
		add(tablePanel, BorderLayout.EAST);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}

	/**
	*	Using the free 3rd party library JFreeChart, the graphs for the indices are made
	*
	*	@param title title of the graph
	*	@param values arraylist of values to be plotted
	*	@return chartpanel containing the graph
	*/
	private ChartPanel createChart(String title, ArrayList<Double> values) {
		v = values;
		JFreeChart chart = ChartFactory.createLineChart(title, 
			"iterations", 
			"USD", 
			createDataset(), 
			PlotOrientation.VERTICAL, 
			false, 
			false, 
			false);
		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		CategoryAxis domain = plot.getDomainAxis();
		domain.setVisible(true);
		domain.setTickLabelsVisible(false);
		NumberAxis range = (NumberAxis)plot.getRangeAxis();
		range.setVisible(true);
		range.setAutoRangeIncludesZero(false);
		if (title.equals("Market capitalisation Index")) {
			range.setLabel("USD in billions");	
		}
		ChartPanel cp = new ChartPanel(chart);
		cp.setPreferredSize(new Dimension(600, 400));
		return cp;
	}

	/**
	*	@return defaultcategorydataset to be used in the graph
	*/
	private DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset ds = new DefaultCategoryDataset();
		for (int i=0; i<v.size(); i++) {
			ds.addValue(v.get(i), "Value1", i+"");
		}
		return ds;
	}

	/**
	*	Tables are created through the standard library using the JTable module
	*	@return JScrollPane containing the table for agent info
	*/
	private JScrollPane createAgentTable() {
		DefaultTableModel model = new DefaultTableModel();
		String[] columns = {"AgentID", "Cash", "Assets", "Vol Risk", "EPS Risk", "PE Risk"};
		for (String s : columns) {
			model.addColumn(s);
		}
		for (Agent a : agents) {
			Vector<Object> row = new Vector<Object>();
			String id = a.toString().split("@")[1];
			row.add("Agent@"+id);
			row.add(String.format("%.2f", a.getAgentCash()));
			row.add(String.format("%.2f", a.getAgentAssets()));
			row.add(a.getRisk());
			row.add(a.getEPS());
			row.add(a.getPE());
			model.addRow(row);
		}
		JTable agentTable = new JTable(model);
		agentTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		JScrollPane tablePane = new JScrollPane(agentTable);
		agentTable.setPreferredScrollableViewportSize(new Dimension(463, 150));

		return tablePane;
	}

	/**
	*	Tables are created through the standard library using the JTable module
	*	@return JScrollPane containing the table for stock info
	*/
	private JScrollPane createStockTable() {
		DefaultTableModel model = new DefaultTableModel();
		String[] columns = {"Stock", "Price", "Company", "Price Change", "Volatility", "Volume"};
		for (String s : columns) {
			model.addColumn(s);
		}
		for (Stock s : stocks) {
			Vector<Object> row = new Vector<Object>();
			row.add(s.getStockName());
			row.add(String.format("%.2f", s.getStockPrice()));
			row.add(s.getStockCompanyName());
			if (s.getStockPriceChange() > 0) {
				row.add(String.format("+%.4f%%", s.getStockPriceChange()/s.getStockPrice()*100));
			} else {
				row.add(String.format("%.4f%%", s.getStockPriceChange()/s.getStockPrice()*100));
			}
			row.add(String.format("%.4f", s.calcVolatility()));
			row.add(String.format("%d", s.getStockCompany().getVolume()));
			model.addRow(row);
		}
		JTable stockTable = new JTable(model);
		stockTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane tablePane = new JScrollPane(stockTable);
		stockTable.setPreferredScrollableViewportSize(new Dimension(463, 150));

		return tablePane;
	}

	public Font getFont(String name) throws FontFormatException, IOException {
		String fontPath = "font/sulphur-point/"+name;
		return Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
	}

	/**
	*	Saves both graphs as JPEG images
	*	@param price reference to stock price graph
	*	@param markCap reference to market cap graph
	*/
	private void captureJPEG(ChartPanel price, ChartPanel markCap) throws IOException {
		File marketPrice = new File("marketplace_priceindex.jpeg");

		File marketCapitalisation = new File("marketplace_capitalisationindex.jpeg");
		ChartUtilities.saveChartAsJPEG(marketPrice, price.getChart(), 600, 400);
		ChartUtilities.saveChartAsJPEG(marketCapitalisation, markCap.getChart(), 600, 400);
	}

	public void actionPerformed(ActionEvent ev) {
		JMenuItem src = (JMenuItem)ev.getSource();
		if (src==makeNewSim) {
			this.dispose();
			new SimulationSettings();
		}
		if (src==exportGraphs) {
			try {
				captureJPEG(stockIndex, marketcapIndex);
			} catch (IOException e) {
				System.out.println("File not found: "+e);
			}
		}
		if (src==exportTables) {

		}
		if (src==exitSim) {
			System.exit(0);
		}
		if (src==menuHelp) {
			System.out.println("flag");
			String url = "https://cseegit.essex.ac.uk/ce301/antoniou_a/capstone_project";
			try {
				Desktop.getDesktop().browse(new URL(url).toURI());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}