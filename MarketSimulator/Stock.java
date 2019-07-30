package MarketSimulator;

import java.util.Random;
import java.util.ArrayList;

/**
*	Stock class is an object modelled after stocks put forth for trade in a stock exchange by companies.
*	
*	A Stock object contains all the information a stock would require
*	to exist and complete operations in the marketplace, albeit simplified.
*	This information includes:
*	- the stock's name
*	- the stock's price
*	- a Company type to represent the company that owns it
*	- the stock's price change
*	- a tick size, defined as the minimum amount a stock's price is allowed to change
*	
*	Operations include returning the values of these properties of the stocks and the calculation of
*	a new stock price
*	
*	@author Alexandros Antoniou
*/
public class Stock {

	public static final double TICK_SIZE = 0.01;

	private String name;
	private double price;
	protected Company company;
	private double priceChange;
	private ArrayList<Double> pastStockPrices;
	private double volatility;

	/**
	*	Initialises a stock object instance
	*	@param newName the name of the stock
	*	@param newCompany the Company object that owns the stock
	*	@param startingPrice the initial price of the stock
	*/
	Stock(String newName, Company newCompany, double startingPrice) {
		this.name = newName;
		this.company = newCompany;
		this.price = startingPrice;
		this.pastStockPrices = new ArrayList<Double>();
		this.pastStockPrices.add(this.price);
	}

	public Company getStockCompany() {
		return this.company;
	}

	public String getStockCompanyName() {
		return this.company.getCompanyName();
	}

	public String getStockName() {
		return this.name;
	}

	public double getStockPrice() {
		return this.price;
	}

	public double getStockPriceChange() {
		return this.priceChange;
	}

	/**
	*	A random number is generated with Gaussian distribution with mean of 0.0 and standard deviation of 1.0
	*	The sign of the random number decides whether the price change is positive or negative
	*	The price change is then multiplied and the ticksize is added or subtracted
	*	This new price change is added to the old stock price
	*/
	public void updateStockPrice() {
		double chance;
		Random updt = new Random();
		chance = updt.nextDouble();
		this.priceChange = TICK_SIZE * 1000 * chance + TICK_SIZE;
		if (chance < 0.7) {
			this.priceChange = - this.priceChange;
		}
		this.price += this.priceChange;
		if (this.price < 0) {
			this.price = TICK_SIZE;
		}
		this.pastStockPrices.add(this.price);
	}

	/**
	*	Calculates the volatility of a stock
	*	Volatility is defined as the standard deviation of the returns of a stock over a 
	*	given time period
	*	We calculate it by first finding the average value of the past prices of the stock
	*	Then we calculate the difference between each past price and the mean we just got
	*	We square these deviations and add them together
	*	Finally we divide this new sum by the number of past stock prices
	*	@return the stock's volatility as a Double
	*/
	public double calcVolatility() {
		double tempMean = 0;
		double sqrDev = 0;
		for (Double p : this.pastStockPrices) {
			tempMean+=p / this.pastStockPrices.size();
		}

		for (Double p : this.pastStockPrices) {
			sqrDev += Math.pow((p - tempMean), 2);
		}

		double variance = sqrDev / this.pastStockPrices.size();
		
		this.volatility = Math.sqrt(variance);

		return Math.sqrt(variance);
	}

	/**
	*	Calculates the Earnings/Price ratio of the stock
	*	Price/Earnings ratio is defined as the ratio of the current price of the stock
	*	to the company's earnings per stock
	*	@return the stock's volatility as a Double
	*/
	public double calcPERatio() {
		double epsRatio = this.company.getEarningsPerStock();
		return this.price / epsRatio;
	}

	public ArrayList<Double> getPastPrices() {
		return this.pastStockPrices;
	}

	public double getStockVolatility() {
		return this.volatility;
	}
}