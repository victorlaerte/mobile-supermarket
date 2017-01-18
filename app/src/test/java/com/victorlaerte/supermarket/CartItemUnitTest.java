package com.victorlaerte.supermarket;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.victorlaerte.supermarket.model.CartItem;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.impl.CartItemImpl;
import com.victorlaerte.supermarket.model.impl.MarketItemImpl;

import junit.framework.Assert;

public class CartItemUnitTest {

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
	public void testGetCartItemId() {

		CartItem cartItem = new CartItemImpl(cartItemId, marketItem);

		Assert.assertEquals(cartItem.getId(), cartItemId);
	}

	@Test
	public void testAddOneMore() {

		CartItem cartItem = new CartItemImpl(cartItemId, marketItem);

		for (int i = 0; i < 20; i++) {

			cartItem.addOneMore(RandomStringUtils.randomNumeric(10));
		}

		Assert.assertEquals(cartItem.getQuantity(), ((CartItemImpl) cartItem).getCartItemIdList().size());

	}

	@Test
	public void testRemoveOne() {

		CartItem cartItem = new CartItemImpl(cartItemId, marketItem);
		List<String> cartItemListId = new ArrayList<String>();

		for (int i = 0; i < 20; i++) {

			String randomCartItemId = RandomStringUtils.randomNumeric(10);
			cartItemListId.add(randomCartItemId);
			cartItem.addOneMore(randomCartItemId);
		}

		// + 1 is because the CartItemImpl constructor, self add one item to cart;
		for (int i = 0; i < cartItemListId.size(); i++) {

			cartItem.removeOne(cartItemListId.get(i));
		}

		// It's the constructor one
		cartItem.removeOne(cartItemId);
		Assert.assertEquals(cartItem.getQuantity(), 0);
	}

}