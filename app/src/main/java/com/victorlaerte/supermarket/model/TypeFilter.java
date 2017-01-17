package com.victorlaerte.supermarket.model;

/**
 * Created by victoroliveira on 17/01/17.
 */

public enum TypeFilter {

	BAKERY("bakery"), DAIRY("dairy"), FRUIT("fruit"), VEGETABLE("vegetable"), MEAT("meat");

	private String label;

	TypeFilter(String label) {

		this.label = label;
	}

	public String getLabel() {

		return label;
	}
}
