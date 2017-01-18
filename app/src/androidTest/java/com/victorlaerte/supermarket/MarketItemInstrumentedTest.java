package com.victorlaerte.supermarket;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.impl.MarketItemImpl;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

@RunWith(AndroidJUnit4.class)
public class MarketItemInstrumentedTest {

	private String id;
	private String title;
	private String description;
	private String type;
	private double price;
	private int rating;
	private String filename;
	private int width;
	private int height;

	@Before
	public void setUp() throws Exception {

		id = RandomStringUtils.randomNumeric(10);
		title = RandomStringUtils.randomAlphabetic(10);
		description = RandomStringUtils.randomAlphabetic(10);
		type = RandomStringUtils.randomAlphabetic(10);
		price = 10.0;
		rating = 3;
		filename = RandomStringUtils.randomAlphabetic(10);
		width = 300;
		height = 300;
	}

	@Test
	public void testParcelable() {

		MarketItem marketItem = new MarketItemImpl(id, title, description, type, price, rating, filename, width,
				height);

		Parcel parcel = Parcel.obtain();
		marketItem.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		MarketItem parceledMarketItem = MarketItemImpl.CREATOR.createFromParcel(parcel);

		_assertEquals(marketItem, parceledMarketItem);
	}

	private void _assertEquals(MarketItem oldMarketItem, MarketItem newMarketItem) {

		// @formatter:off
		Assert.assertTrue(oldMarketItem.getId().equals(newMarketItem.getId()) &&
				oldMarketItem.getTitle().equals(newMarketItem.getTitle()) &&
				oldMarketItem.getDescription().equals(newMarketItem.getDescription()) &&
				oldMarketItem.getType().equals(newMarketItem.getType()) &&
				oldMarketItem.getPrice() == newMarketItem.getPrice() &&
				oldMarketItem.getImageFileName().equals(newMarketItem.getImageFileName()) &&
				oldMarketItem.getWidth() == newMarketItem.getWidth() &&
				oldMarketItem.getHeight() == newMarketItem.getHeight());
		// @formatter:on
	}
}
