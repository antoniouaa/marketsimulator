package MarketSimulator;

import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.io.FileNotFoundException;
import java.util.HashMap;
/**
*	This class is responsible for reading the CSVs containg information about the companies and stocks
*	It reads each CSV line by line and returns a HashMap of two arraylists mapping the companies to their stock
*
*	@author Alexandros Antoniou
*/
public class CSVParser {
	public final static String COMPANIES_CSV = "CSVs/companies.csv";
	public final static String STOCKS_CSV = "CSVs/stocks.csv";

	public static HashMap<ArrayList<Company>, ArrayList<Stock>> parseCSV(int numberOfStock) throws FileNotFoundException {
		File companyFile = new File(COMPANIES_CSV);
		File stocksFile = new File(STOCKS_CSV);

		ArrayList<Company> arrayC = new ArrayList<Company>();
		ArrayList<Stock> arrayS = new ArrayList<Stock>();

		Scanner scan = new Scanner(companyFile);
		scan.useDelimiter("\\n");
		while(scan.hasNextLine()) {
			String[] line = scan.next().split(",");
			if (Integer.parseInt(line[0])<=numberOfStock) {
				arrayC.add(new Company(line[1], Double.parseDouble(line[2])));
			}
		}

		scan = new Scanner(stocksFile);
		while(scan.hasNextLine()) {
			String[] line = scan.next().split(",");
			if (Integer.parseInt(line[0])<=numberOfStock) {
				Company c = arrayC.get(Integer.parseInt(line[0])-1);
				arrayS.add(new Stock(line[1], c, Double.parseDouble(line[2])));
			}
		}

		HashMap<ArrayList<Company>, ArrayList<Stock>> map = new HashMap<ArrayList<Company>, ArrayList<Stock>>();
		map.put(arrayC, arrayS);
		return map;
	}
}