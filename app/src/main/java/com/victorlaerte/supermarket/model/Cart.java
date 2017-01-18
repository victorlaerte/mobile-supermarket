package com.victorlaerte.supermarket.model;

import java.util.HashMap;
import java.util.Map;

import com.victorlaerte.supermarket.model.impl.CartItemImpl;
import com.victorlaerte.supermarket.util.Constants;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by victoroliveira on 17/01/17.
 */

public class Cart implements Parcelable {

	private static Cart INSTANCE;
	private Map<String, CartItem> map;

	private Cart() {

		map = new HashMap<String, CartItem>();
	}

	public Map<String, CartItem> getMap() {

		return map;
	}

	public void addItem(String cartItemId, MarketItem marketItem) {

		if (map.containsKey(marketItem.getId())) {

			CartItem cartItem = map.get(marketItem.getId());

			cartItem.addOneMore(cartItemId);

		} else {

			map.put(marketItem.getId(), new CartItemImpl(cartItemId, marketItem));
		}
	}

	public void removeItem(String cartItemId, MarketItem marketItem) {

		if (map.containsKey(marketItem.getId())) {

			CartItem cartItem = map.get(marketItem.getId());

			int remaining = cartItem.removeOne(cartItemId);

			if (remaining == 0) {

				map.remove(marketItem.getId());
			}
		}
	}

	public void removeAll() {

		map.clear();
	}

	public static synchronized Cart getInstance() {

		if (INSTANCE == null) {

			INSTANCE = new Cart();
		}

		return INSTANCE;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeMap(map);
	}

	public static final Parcelable.Creator<Cart> CREATOR = new Parcelable.Creator<Cart>() {

		@Override
		public Cart createFromParcel(Parcel in) {

			return new Cart(in);
		}

		@Override
		public Cart[] newArray(int size) {

			return new Cart[size];
		}
	};

	private Cart(Parcel in) {

		in.readMap(getInstance().getMap(), CartItem.class.getClassLoader());
	}

	public void save(Bundle outState) {

		outState.putParcelable(Constants.CART, this);
	}

	public static void restore(Bundle savedInstanceState) {

		if (savedInstanceState != null && savedInstanceState.containsKey(Constants.CART)) {
			if (INSTANCE == null)
				INSTANCE = savedInstanceState.getParcelable(Constants.CART);
		}
	}
}
