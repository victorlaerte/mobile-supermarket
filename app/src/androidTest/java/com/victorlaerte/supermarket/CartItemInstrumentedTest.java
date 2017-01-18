package com.victorlaerte.supermarket;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.victorlaerte.supermarket.model.CartItem;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.impl.CartItemImpl;
import com.victorlaerte.supermarket.model.impl.MarketItemImpl;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

@RunWith(AndroidJUnit4.class)
public class CartItemInstrumentedTest {

	private String cartItemId;
	private MarketItem marketItem;

	@Before
	public void setUp() throws Exception {

		cartItemId = RandomStringUtils.randomNumeric(10);

		String id = RandomStringUtils.randomNumeric(10);
		String title = RandomStringUtils.randomAlphabetic(10);
		String description = RandomStringUtils.randomAlphabetic(10);
		String type = RandomStringUtils.randomAlphabetic(10);
		Double price = 10.0;
		int rating = 3;
		String filename = RandomStringUtils.randomAlphanumeric(10);
		int width = 300;
		int height = 300;

		marketItem = new MarketItemImpl(id, title, description, type, price, rating, filename, width, height);
	}

	@Test
	public void testParcelable() {

		CartItem cartItem = new CartItemImpl(cartItemId, marketItem);

		Parcel parcel = Parcel.obtain();
		cartItem.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		CartItem parceledCartItem = CartItemImpl.CREATOR.createFromParcel(parcel);

		Assert.assertEquals(cartItem.getId(), parceledCartItem.getId());
	}

}
