package personal.StockReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import personal.dataL.Stock;

public class Controller {

	private ArrayList<Stock> stocks;
	private Lock lockObj = new ReentrantLock();

	public Controller() {
		this.stocks = new ArrayList<Stock>();
	}

	public Lock getLockObj() {
		return lockObj;
	}

	public void setLockObj(Lock lockObj) {
		this.lockObj = lockObj;
	}

	public ArrayList<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(ArrayList<Stock> stocks) {
		this.stocks = stocks;
	}

	/**
	 * This method is used to gather data from bvb. There surely must be a better
	 * way, but unfortunately I found no better method
	 * 
	 */
	public void read() {
		try {
			stocks.clear();
			URL url = new URL("http://www.bvb.ro/");
			URL url2 = new URL("http://www.bvb.ro/TradingAndStatistics/Trading/MarketsToday#");
			URLConnection urlC = url.openConnection();
			InputStreamReader inStream = new InputStreamReader(urlC.getInputStream());
			BufferedReader buff = new BufferedReader(inStream);
			String name;
			float price;
			float var;
			String line = buff.readLine();

			URLConnection urlC2 = url2.openConnection();
			InputStreamReader inStream2 = new InputStreamReader(urlC2.getInputStream());
			BufferedReader buff2 = new BufferedReader(inStream2);
			String line2 = buff2.readLine();

			while (line2 != null) {

				if (line2.equals(
						"                                                <td class=\"text-left\">BET</td>")) {
					line2 = buff2.readLine();
					String BetS;
					BetS = line2.substring(line2.indexOf("k\">") + 3, line2.indexOf("</"));
					BetS = BetS.replace(",", "");
					float BET = Float.parseFloat(BetS);
					float BETVar = Float.parseFloat(line2.substring(line2.indexOf("'>") + 2, line2.indexOf("%")));
					stocks.add(new Stock("BET", BET, BETVar)); // even though BET is an index, I decided to have it at
																// position 0 in the ArrayList. This is because its
																// functionality in this application resembles that of a
																// simple stock
					break;
				}
				line2 = buff2.readLine();
			}
			while (line != null) {
				if (line.equals("				</thead><tbody>")) {
					line = buff.readLine();
					while (!line.equals("					</tr>")) {

						line = buff.readLine();
						line = buff.readLine(); // name
						name = line.substring(line.lastIndexOf("=") + 1, line.lastIndexOf(">") - 1);
						line = buff.readLine();
						line = buff.readLine();
						line = buff.readLine(); // price, var
						price = Float.parseFloat(line.substring(line.indexOf("\">") + 2, line.lastIndexOf("</td><td")));
						var = Float.parseFloat(line.substring(line.lastIndexOf("\">") + 2, line.lastIndexOf("<")));
						stocks.add(new Stock(name, price, var));
						line = buff.readLine();

					}
					break;

				}
				line = buff.readLine();
			}
		} catch (MalformedURLException e) {
			System.out.println("Bad URL");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Cannot open connection");
			e.printStackTrace();
		}
	}
}
