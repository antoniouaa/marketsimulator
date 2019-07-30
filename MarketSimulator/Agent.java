package MarketSimulator;

import java.util.*;
import java.util.stream.*;
import java.util.Map.*;
import java.security.*;

/**
*	Agent class is an object modelled after stock traders in the stock exchange.
*	
*	An agent object contains all the information a stock trader would require
*	to exist and complete operations in the marketplace, albeit simplified.
*	This information includes:
*	- an amount of cash
*	- a list or portfolio of stocks and the number of each stock they hold
*	- a risk factor, random for each agent
*	- 2 additional weight variables for the two remaining indices
*	
*	Operations which return the prices of stocks
*	Operations which calculate the total assets of an agent instance based on the
*	amount of cash in hand and the stocks they are currently holding
*	Operations which return the names of all stocks an agent instance is currently holding
*	Operations which calculate index values for all the stocks
*	Operations which retrieve the lowest and highest of those stocks for each index
*	Operations which calculate the best stock to invest in
*	
*	@author Alexandros Antoniou
*/
public class Agent {

	public static final int STANDARD_STARTING_CASH = 100000;
	public static final int LOWER_BOUND_STARTING_CASH = 70000;
	public static final int UPPER_BOUND_STARTING_CASH = 120000;
	public static final int RISK_LOWER_LIMIT = 1;
	public static final int RISK_UPPER_LIMIT = 1000;

	private Random rand;
	public int transactions;
	private int risk;
	private int peWeight;
	private int epsWeight;
	private int overcommitTier;
	private double cash;
	private Stock tradeStock;
	private HashMap<Stock, Integer> stocks = new HashMap<Stock, Integer>();
	private HashMap<Stock, Double> volatilities = new HashMap<Stock, Double>();
	private HashMap<Stock, Double> EPSRatio = new HashMap<Stock, Double>();
	private HashMap<Stock, Double> PERatio = new HashMap<Stock, Double>();
	private HashMap<Stock, Double> totalIndex = new HashMap<Stock, Double>();
	private ArrayList<SortedStock> sortedStocks;

	/**
	*	Initialises agent instance with standard amount of cash and weights for the three indices
	*/
	Agent() {
		this.transactions = 0;
		this.cash = Agent.STANDARD_STARTING_CASH;
		this.stocks = new HashMap<Stock, Integer>();
		calcAgentRiskAndWeights();
	}

	/**
	*	Initialises agent instance with specified amount of cash and weights for the three indices
	*	@param startingCash cash of agent when initialised
	*/
	Agent(double startingCash) {
		this.transactions = 0;
		this.cash = startingCash;
		stocks = new HashMap<Stock, Integer>();
		calcAgentRiskAndWeights();
	}

	/**
	*	Initialises agent instance with specified amount of cash and
	*	specified set of stocks and weights for the three indices
	*	@param startingCash cash of agent instance when initialised
	*	@param startingStocks stocks of agent instance when initialised
	*/
	Agent(double startingCash, HashMap<Stock, Integer> startingStocks) {
		this.transactions = 0;
		this.cash = startingCash;
		stocks = startingStocks;
		calcAgentRiskAndWeights();
	}

	public void calcAgentRiskAndWeights() {
		rand = new Random();
		this.risk = RISK_LOWER_LIMIT + rand.nextInt(RISK_UPPER_LIMIT + 1);
		this.peWeight = RISK_LOWER_LIMIT + rand.nextInt(RISK_UPPER_LIMIT + 1);
		this.epsWeight = RISK_LOWER_LIMIT + rand.nextInt(RISK_UPPER_LIMIT + 1);
	}

	public double getAgentCash() {
		return this.cash;
	}

	public HashMap<Stock, Integer> getAgentPortfolio() {
		return this.stocks;
	}

	public ArrayList<SortedStock> getSortedPortfolio() {
		return this.sortedStocks;
	}

	public double getOvercommitTier() {
		switch(this.overcommitTier) {
			case 1: return 0.4;
			case 2: return 0.7;
			case 3: return 0.95;
			default: return 0.0;
		}
	}

	public int getAgentStockVolume(Stock s) {
		return stocks.get(s);
	}

	/**
	*	Returns the total assets of an agent object
	*	Total assets are defined as the amount of cash of the agent object
	*	plus the number of each stock multiplied by the price of the stock
	*	that the object is currently holding
	*	@return total assets of agent as defined above as a Double
	*/
	public double getAgentAssets() {
		double totalStockValue = 0.0;
		if (this.stocks == null && this.cash == 0.0) {
			totalStockValue = 0.0;
		} else if (this.stocks == null && this.cash != 0.0) {
			totalStockValue = this.cash;
		} else{
			for (Map.Entry<Stock, Integer> stock : this.stocks.entrySet()) {
				double stockVal = stock.getKey().getStockPrice() * stock.getValue();
				totalStockValue += stockVal;
			}
		}
		return totalStockValue + cash;
	}

	/**
	*	Returns a string containing all the names of each stock that an 
	*	agent owns currently
	*	@return all names of stocks in possession of agent as String
	*/
	public String getAgentStockNames() {
		String stockName = "";
		System.out.println(this.stocks.size());
		if (this.stocks == null) {
			return "No Stocks";
		} else{
			for (Map.Entry<Stock, Integer> stock : this.stocks.entrySet()) {
				stockName += stock.getKey().getStockName();
			}
			return stockName;
		}
	}

	/**
	*	Iterates through the agent's stocks and calculates all the volatilities of the stocks in the agent's portfolio
	*	Takes into account the risk variable for each agent
	*/
	public void calcPortfolioVolatilities() {
		for (Map.Entry<Stock, Integer> stock : this.stocks.entrySet()) {
			double stockVolat = stock.getKey().calcVolatility();
			volatilities.put(stock.getKey(), stockVolat);
		}
		normaliseVolatilities();
	}

	/**
	*	Iterates through the agent's stocks and normalises all the volatilities of the stocks in the agent's portfolio
	*	The normalised values range from 0 to 50 and replace the raw volatilities in the hashmap
	*/
	public void normaliseVolatilities() {
		double a = Collections.min(volatilities.entrySet(), 
			Map.Entry.comparingByValue()).getValue();
		double b = Collections.max(volatilities.entrySet(), 
			Map.Entry.comparingByValue()).getValue();
		if (b==0.0) {
			b = 1;
		}
		double a_norm = 1;
		double b_norm = 1000;
		for (Map.Entry<Stock, Double> stock : this.volatilities.entrySet()) {
			double stockVolatNormalised = a_norm + (stock.getValue() - a)*(b_norm - a_norm)/(b - a);
			volatilities.put(stock.getKey(), stockVolatNormalised*this.risk);
		}
		volatilities.entrySet().stream().sorted(Entry.<Stock, Double>comparingByValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2)->e1, HashMap::new));
	}

	/**
	*	Iterates through the agent's stocks and calculates all the EPS ratios for the companies of the stocks in the agent's portfolio
	*/
	public void calcPortfolioEPS() {
		for (Map.Entry<Stock, Integer> stock : this.stocks.entrySet()) {
			double stockEPS = stock.getKey().company.getEarningsPerStock();
			EPSRatio.put(stock.getKey(), stockEPS);
		}
		normaliseEPS();
	}

	/**
	*	Iterates through the agent's stocks and normalises all the earnings per stock of the stocks in the agent's portfolio
	*	The normalised values range from 0 to 2 and replace the raw earnings per stock in the hashmap
	*/
	public void normaliseEPS() {
		double a = Collections.min(EPSRatio.entrySet(), 
			Map.Entry.comparingByValue()).getValue();
		double b = Collections.max(EPSRatio.entrySet(), 
			Map.Entry.comparingByValue()).getValue();
		double a_norm = 1;
		double b_norm = 1000;
		for (Map.Entry<Stock, Double> stock : this.EPSRatio.entrySet()) {
			double stockEPSNormalised = a_norm + (stock.getValue() - a)*(b_norm - a_norm)/(b - a);
			EPSRatio.put(stock.getKey(), stockEPSNormalised*this.epsWeight);
		}
		EPSRatio.entrySet().stream().sorted(Entry.<Stock, Double>comparingByValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2)->e1, HashMap::new));
	}

	/**
	*	Iterates through the agent's stocks and calculates all the Price/Earnings ratios of the stocks in the agent's portfolio
	*/
	public void calcPortfolioPE() {
		for (Map.Entry<Stock, Integer> stock : this.stocks.entrySet()) {
			double stockPE = stock.getKey().calcPERatio();
			PERatio.put(stock.getKey(), stockPE);
		}
		normalisePE();
	}

	/**
	*	Iterates through the agent's stocks and normalises all the price-earnings ratios of the stocks in the agent's portfolio
	*	The normalised values range from 0 to 2 and replace the raw price-earnings ratios in the hashmap
	*/
	public void normalisePE() {
		double a = Collections.min(PERatio.entrySet(), 
			Map.Entry.comparingByValue()).getValue();
		double b = Collections.max(PERatio.entrySet(), 
			Map.Entry.comparingByValue()).getValue();
		double a_norm = 1;
		double b_norm = 1000;
		for (Map.Entry<Stock, Double> stock : PERatio.entrySet()) {
			double stockPENormalised = a_norm + (stock.getValue() - a)*(b_norm - a_norm)/(b - a);
			PERatio.put(stock.getKey(), stockPENormalised *this.peWeight);
		}
		PERatio.entrySet().stream().sorted(Entry.<Stock, Double>comparingByValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2)->e1, HashMap::new));
	}

	/**
	*	Iterates through the agent's stocks and adds all of the indices for each stock together
	*	The total index value is added into a totalIndex hashmap along with a reference to its stock
	*/
	public void calcStockValue() {
		double total;
		for (Map.Entry<Stock, Integer> stockEntry : this.stocks.entrySet()) {
			total = 0;
			total += volatilities.get(stockEntry.getKey());
			total += EPSRatio.get(stockEntry.getKey());
			total += PERatio.get(stockEntry.getKey());
			totalIndex.put(stockEntry.getKey(), total);
		}
	}

	public void printAllIndices() {
		// System.out.print("Weights used:\tRisk: " + this.risk + " EPS: " + this.epsWeight+" PE: " + this.peWeight);
		System.out.println(sortedStocks);
	}

	public Map.Entry<Stock, Double> getBestStockInvestment() {
		Map.Entry<Stock, Double> bestIndex = Collections.max(this.totalIndex.entrySet(), 
			Map.Entry.comparingByValue());
		return bestIndex;
	}

	public Map.Entry<Stock, Double> getWorstStockInvestment() {
		Map.Entry<Stock, Double> worstIndex = Collections.min(this.totalIndex.entrySet(), Map.Entry.comparingByValue());
		return worstIndex;
	}

	/**
	*	Sorts the agent's stocks by value from highest to lowest
	*/
	public void sortStocksByValue() {
		sortedStocks = new ArrayList<SortedStock>();
		totalIndex.entrySet().stream()
			.sorted((e1, e2)->-e1.getValue().compareTo(e2.getValue()))
			.forEach(e->sortedStocks.add(new SortedStock(e.getKey(), e.getValue())));
		// custom comparator to sort the stocks in descending order
		Collections.sort(sortedStocks, new Comparator<SortedStock>() {
			@Override
			public int compare(SortedStock s1, SortedStock s2) {
				if (s1.getTotalIndex()>s2.getTotalIndex()) {
					return 1;
				}
				if (s1.getTotalIndex()<s2.getTotalIndex()) {
					return -1;
				}
				return 0;
			}
		});
	}
	
	/**
	*	Receives arraylist of all agents and finds an agent among them whose best investment
	*	matches the stock this agent wants to buy, and returns that agent
	*	@return agent to engage in transaction
	*/
	public Agent findTradeAgent(ArrayList<Agent> agents) {
		Iterator<SortedStock> it = this.sortedStocks.iterator();
		while(it.hasNext()) {
			SortedStock ent = it.next();
			Stock sCandidate = ent.getStock();
			for (Agent a : agents) {
				Stock sChecked = a.getSortedPortfolio().iterator().next().getStock();
				if (sCandidate.equals(sChecked)) {
					this.tradeStock = sCandidate;
					return a;
				}
			}
		}
		return null;
	}
	/**
	*	Complete the transaction by removing cash from this agent, adding the cash to the chosen
	*	agent, then removing the stocks from the chosen agent and adding them to this agent
	*	The transaction only goes through if the chosen agent has enough of the stock and this agent
	*	has enough cash for the purchase
	*	@param a chosen agent for the transaction 
	*/
	public void makeTrade(Agent a) {
		double calcCash = (this.tradeStock.getStockPrice() + Stock.TICK_SIZE) * MarketPlace.MARKETPLACE_TRADE_MAX;
		
		if (this.cash>calcCash && a.getAgentStockVolume(this.tradeStock)>MarketPlace.MARKETPLACE_TRADE_MAX) {
			//take cash from agent making trade give him stock
			this.setAgentStock(this.tradeStock, MarketPlace.MARKETPLACE_TRADE_MAX);
			this.setAgentCash(-calcCash);
			//take stock from agent accepting trade give him cash 
			a.setAgentStock(this.tradeStock, -MarketPlace.MARKETPLACE_TRADE_MAX);
			a.setAgentCash(calcCash);
			this.transactions++;
		}
	}
	

	public int getRisk() {
		return this.risk;
	}

	public int getEPS() {
		return this.epsWeight;
	}

	public int getPE() {
		return this.peWeight;
	}

	public void setAgentStock(Stock s, int numberOfStocks) {
		this.stocks.put(s, this.stocks.get(s)+numberOfStocks);
	}

	public void setAgentCash(double addedCash) {
		this.cash += addedCash;
	}
}