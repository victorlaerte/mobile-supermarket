package com.victorlaerte.supermarket;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.victorlaerte.supermarket.model.Cart;
import com.victorlaerte.supermarket.model.CartItem;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.impl.CartItemImpl;
import com.victorlaerte.supermarket.model.impl.MarketItemImpl;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

@RunWith(AndroidJUnit4.class)
public class CartInstrumentedTest {

	private List<CartItem> cartItemList;
	private final int LIST_SIZE = 20;

	@Before
	public void setUp() throws Exception {

		cartItemList = new ArrayList<CartItem>();

		for (int i = 0; i < LIST_SIZE; i++) {

			String cartItemId = RandomStringUtils.randomNumeric(10);

			MarketItem marketItem = _createMarketItem();

			CartItem cartItem = new CartItemImpl(cartItemId, marketItem);

			cartItemList.add(cartItem);
		}
	}

	@Test
	public void testParcelable() {

		_addItemsToCart();

		Parcel parcel = Parcel.obtain();
		Cart.getInstance().writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		Cart.CREATOR.createFromParcel(parcel);

		Assert.assertTrue(Cart.getInstance().getMap().size() == LIST_SIZE);
	}

	private void _addItemsToCart() {

		for (CartItem cartItem : cartItemList) {

			Cart.getInstance().addItem(cartItem);
		}
	}

	private MarketItem _createMarketItem() {

		String id = RandomStringUtils.randomNumeric(10);
		String title = RandomStringUtils.randomAlphabetic(10);
		String description = RandomStringUtils.randomAlphabetic(10);
		String type = RandomStringUtils.randomAlphabetic(10);
		Double price = 10.0;
		int rating = 3;
		String filename = RandomStringUtils.randomAlphanumeric(10);
		int width = 300;
		int height = 300;

		return new MarketItemImpl(id, title, description, type, price, rating, filename, width, height);
	}
}
