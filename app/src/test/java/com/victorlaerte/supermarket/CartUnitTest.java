package com.victorlaerte.supermarket;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.victorlaerte.supermarket.model.Cart;
import com.victorlaerte.supermarket.model.CartItem;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.impl.CartItemImpl;
import com.victorlaerte.supermarket.model.impl.MarketItemImpl;

import junit.framework.Assert;

public class CartUnitTest {

	private List<CartItem> cartItemList;

	@Before
	public void setUp() throws Exception {

		cartItemList = new ArrayList<CartItem>();

		for (int i = 0; i < 20; i++) {

			String cartItemId = RandomStringUtils.randomNumeric(10);

			MarketItem marketItem = _createMarketItem();

			CartItem cartItem = new CartItemImpl(cartItemId, marketItem);

			cartItemList.add(cartItem);
		}
	}

	@Test
	public void testClearCart() {

		_addItemsToCart();

		Cart.getInstance().clear();

		Assert.assertEquals(Cart.getInstance().getMap().size(), 0);
	}

	@Test
	public void testAddDifferentItemsToCart() {

		_addItemsToCart();

		Assert.assertTrue(!Cart.getInstance().getMap().isEmpty());

		Cart.getInstance().clear();
	}

	@Test
	public void testAddEqualsItems() {

		MarketItem marketItem = _createMarketItem();

		CartItem cartItem = new CartItemImpl(RandomStringUtils.randomNumeric(10), marketItem);
		CartItem cartItem2 = new CartItemImpl(RandomStringUtils.randomNumeric(10), marketItem);
		CartItem cartItem3 = new CartItemImpl(RandomStringUtils.randomNumeric(10), marketItem);

		Cart.getInstance().addItem(cartItem);
		Cart.getInstance().addItem(cartItem2);
		Cart.getInstance().addItem(cartItem3);

		Assert.assertEquals(Cart.getInstance().getMap().size(), 1);

		Cart.getInstance().clear();
	}

	@Test
	public void testRemoveItems() {

		_addItemsToCart();

		for (int i = 0; i < cartItemList.size(); i++) {

			CartItem cartItem = cartItemList.get(i);

			Cart.getInstance().removeItem(cartItem);
		}

		Assert.assertEquals(Cart.getInstance().getMap().size(), 0);
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