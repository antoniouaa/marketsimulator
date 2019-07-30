package MarketSimulator;

/**
*	Company class is an object modelled after companies in the stock exchange.
*	
*	A company object contains all the information a stock trader would require
*	to evaluate the performance of a stock in the market and base their decisions
*	regarding purchasing or selling of the stock.
*	This information includes:
*	- the company name
*	- the market capitalisation value
*	- the net revenue of the company
*	- the volume of stocks in the marketplace
*	- the income of the company
*	- the earnings of the company per stock
*
*	The company object is entirely passive in the marketplace, it does not interact with
*	any other object. 
*	The operations it performs simply update the Company class variables so that agents 
*	have current and correct information that will influence their decision making.
*		
*	@author Alexandros Antoniou
*/
public class Company {

	public static final int BILLION_MODIFIER = 1000000000;

	private String name;
	private double marketCap;
	private int volume;
	private double income;
	private double earningsPerStock;

	/**
	*	Initialises a company object instance
	*	@param newName the name of the company
	*	@param newVolume the number of stocks the company has in the marketplace
	*	@param newIncome the company's income
	*/
	Company(String newName, double newIncome) {
		this.name = newName;
		this.volume = 0;
		this.income = newIncome;
		this.calcEarningsPerStock();
	}

	public String getCompanyName() {
		return this.name;
	}

	void calcMarketCap(Stock s) {
		this.marketCap = (this.volume * s.getStockPrice()) / Company.BILLION_MODIFIER;
	}

	public double getMarketCap() {
		return this.marketCap;
	}

	public int getVolume() {
		return this.volume;
	}

	public double getIncome() {
		return this.income;
	}

	/**
	*	The Earnings per stock ratio is calculated by dividing the company's income by the stock volume
	*	A billion modifier is used to simplify calculations
	*/
	public void calcEarningsPerStock() {
		this.earningsPerStock = (this.income * Company.BILLION_MODIFIER) / this.volume;
	}

	public double getEarningsPerStock() {
		return this.earningsPerStock;
	}

	/**
	*	After each agent is given a random amount of stocks, the total volume of stocks of a
	*	particular company need to be updated
	*	@param vol number of stocks to be added to the total
	*/
	public void updateStockVolume(int vol) {
		this.volume += vol;
		calcEarningsPerStock();
	}

}