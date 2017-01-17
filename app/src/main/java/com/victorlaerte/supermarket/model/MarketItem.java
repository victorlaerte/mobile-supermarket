package com.victorlaerte.supermarket.model;

import android.os.Parcelable;

/**
 * Created by victoroliveira on 16/01/17.
 */

public interface MarketItem extends Parcelable {

	public String getId();

	public String getTitle();

	public String getDescription();

	public String getType();

	public double getPrice();

	public int getRating();

	public String getImageFileName();

	public int getWidth();

	public int getHeight();
}
