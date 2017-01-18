package com.victorlaerte.supermarket;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.impl.MarketItemImpl;
import com.victorlaerte.supermarket.util.Constants;

import android.os.Parcel;

import junit.framework.Assert;

public class MarketItemUnitTest {

	private String id;
	private String title;
	private String description;
	private String type;
	private double price;
	private int rating;
	private String filename;
	private int width;
	private int height;

	private JSONObject marketItemJSON;

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

		marketItemJSON = new JSONObject();

		marketItemJSON.put(Constants.ID, id);
		marketItemJSON.put(Constants.TITLE, title);
		marketItemJSON.put(Constants.DESCRIPTION, description);
		marketItemJSON.put(Constants.TYPE, type);
		marketItemJSON.put(Constants.PRICE, price);
		marketItemJSON.put(Constants.RATING, rating);
		marketItemJSON.put(Constants.FILENAME, filename);
		marketItemJSON.put(Constants.WIDTH, width);
		marketItemJSON.put(Constants.HEIGHT, height);
	}

	@Test
	public void testToString() {

		MarketItem marketItem = new MarketItemImpl(id, title, description, type, price, rating, filename, width,
				height);

		try {

			assertEqualJSON(marketItem);

		} catch (JSONException e) {

			Assert.fail();
		}
	}

	@Test
	public void testFromJSON() {

		try {

			MarketItem marketItemFromJSON = new MarketItemImpl(marketItemJSON);

			assertEqualJSON(marketItemFromJSON);

		} catch (JSONException e) {

			Assert.fail();
		}
	}

	private void assertEqualJSON(MarketItem marketItem) throws JSONException {

		JSONObject jsonObject = new JSONObject(marketItem.toString());

		String id = jsonObject.getString(Constants.ID);
		String title = jsonObject.getString(Constants.TITLE);
		String description = jsonObject.getString(Constants.DESCRIPTION);
		String type = jsonObject.getString(Constants.TYPE);
		double price = jsonObject.getDouble(Constants.PRICE);
		int rating = jsonObject.getInt(Constants.RATING);
		String filename = jsonObject.getString(Constants.FILENAME);
		int width = jsonObject.getInt(Constants.WIDTH);
		int height = jsonObject.getInt(Constants.HEIGHT);

		// @formatter:off
		Assert.assertTrue(marketItemJSON.getString(Constants.ID).equals(id) &&
						  marketItemJSON.getString(Constants.TITLE).equals(title) &&
						  marketItemJSON.getString(Constants.DESCRIPTION).equals(description) &&
						  marketItemJSON.getString(Constants.TYPE).equals(type) &&
						  marketItemJSON.getDouble(Constants.PRICE) == price &&
						  marketItemJSON.getInt(Constants.RATING) == rating &&
						  marketItemJSON.getString(Constants.FILENAME).equals(filename) &&
						  marketItemJSON.getInt(Constants.WIDTH) == width &&
						  marketItemJSON.getInt(Constants.HEIGHT) == height);
		// @formatter:on
	}

}