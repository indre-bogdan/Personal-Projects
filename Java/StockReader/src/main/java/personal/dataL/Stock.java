package personal.dataL;

import java.text.DecimalFormat;

public class Stock {
	private String name;
	private final static DecimalFormat pr = new DecimalFormat("#0.0000");
	private final static DecimalFormat vr = new DecimalFormat("#0.00");
	private float price; // current price of a stock
	private float var; // the variation

	public Stock(String name, float price, float var) {
		super();
		this.name = name;
		this.price = price;
		this.var = var;
	}

	public Stock(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public float getPrice() {
		return price;
	}


	public void setPrice(float price) {
		this.price = price;
	}


	public float getVar() {
		return var;
	}


	public void setVar(float var) {
		this.var = var;
	}

	public static DecimalFormat getPr() {
		return pr;
	}


	public static DecimalFormat getVr() {
		return vr;
	}

}
