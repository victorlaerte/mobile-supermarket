package com.victorlaerte.supermarket.model.impl;

import java.util.ArrayList;
import java.util.List;

import com.victorlaerte.supermarket.model.CartItem;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.util.StringPool;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by victoroliveira on 17/01/17.
 */

public class CartItemImpl implements CartItem {

	private List<String> cartItemIdList = new ArrayList<String>();;
	private MarketItem marketItem;
	private int quantity;

	public CartItemImpl(String cartItemId, MarketItem marketItem) {

		cartItemIdList.add(cartItemId);
		this.marketItem = marketItem;
		this.quantity++;
	}

	@Override
	public String getId() {

		if (!cartItemIdList.isEmpty()) {
			return cartItemIdList.get(0);
		}

		return StringPool.BLANK;
	}

	public int addOneMore(String cartItemId) {

		cartItemIdList.add(cartItemId);
		quantity++;
		return quantity;
	}

	public int removeOne(String cartItemId) {

		cartItemIdList.remove(cartItemId);
		quantity--;
		return quantity;
	}

	public MarketItem getMarketItem() {

		return marketItem;
	}

	public List<String> getCartItemIdList() {

		return cartItemIdList;
	}

	public int getQuantity() {

		return quantity;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeList(cartItemIdList);
		dest.writeParcelable(marketItem, flags);
		dest.writeInt(quantity);
	}

	public static final Parcelable.Creator<CartItemImpl> CREATOR = new Parcelable.Creator<CartItemImpl>() {

		@Override
		public CartItemImpl createFromParcel(Parcel in) {

			return new CartItemImpl(in);
		}

		@Override
		public CartItemImpl[] newArray(int size) {

			return new CartItemImpl[size];
		}
	};

	private CartItemImpl(Parcel in) {

		in.readList(cartItemIdList, String.class.getClassLoader());
		marketItem = in.readParcelable(MarketItemImpl.class.getClassLoader());
		quantity = in.readInt();
	}
}
