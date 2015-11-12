package pl.looksok.listviewdemo;

import java.io.Serializable;

public class AtomPayment implements Serializable {
	private static final long serialVersionUID = -5435670920302756945L;
	
	private String name = "";
	private String dateStr = "";
	private double value = 0;

	public AtomPayment(String name,String dateStrf, double value) {
		this.setName(name);
		this.setDatestr(dateStrf);
		this.setValue(value);
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDatestr() {
		return dateStr;
	}
	public void setDatestr(String dateStr) {
		this.dateStr = dateStr;
	}
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}