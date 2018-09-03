package cn.edu.bupt.rsx.htmlparser.model.vo;

import java.io.Serializable;

public class CalculationFactor implements Serializable{
	
	private static final long serialVersionUID = 5504194978044752469L;

	private double distance;
	
	private String type;

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
