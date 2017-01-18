package com.victorlaerte.supermarket.model;

import android.os.Parcelable;

/**
 * Created by victoroliveira on 17/01/17.
 */

public interface CartItem extends Parcelable {

	public String getId();

	public int addOneMore(String cartItemId);

	public int removeOne(String cartItemId);

	public MarketItem getMarketItem();

	public int getQuantity();
}
