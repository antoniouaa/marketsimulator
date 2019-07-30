package MarketSimulator;

/**
*	Custom class to help sort the stocks by holding them in an arraylist instead of a hashmap
*
*	@author Alexandros Antoniou
*/
public class SortedStock {
	Stock stock;
	Double totalIndex;

	public SortedStock(Stock s, Double tIndex) {
		this.stock = s;
		this.totalIndex = tIndex;
	}

	public Stock getStock() {
		return this.stock;
	}

	public Double getTotalIndex() {
		return this.totalIndex;
	}
}