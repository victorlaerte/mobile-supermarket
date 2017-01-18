package com.victorlaerte.supermarket.model.impl;

import org.json.JSONException;
import org.json.JSONObject;

import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.StringPool;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by victoroliveira on 16/01/17.
 */

public class MarketItemImpl implements MarketItem {

	private String id;
	private String title;
	private String description;
	private String type;
	private double price;
	private int rating;
	private String imageFileName;
	private int width;
	private int height;

	public MarketItemImpl(String id, String title, String description, String type, double price, int rating,
			String imageFileName, int width, int height) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.type = type;
		this.price = price;
		this.rating = rating;
		this.imageFileName = imageFileName;
		this.width = width;
		this.height = height;
	}

	public MarketItemImpl(JSONObject json) throws JSONException {

		fill(json);
	}

	private void fill(JSONObject json) throws JSONException {

		this.id = json.getString(Constants.ID);
		this.title = json.getString(Constants.TITLE);
		this.description = json.getString(Constants.DESCRIPTION);
		this.type = json.getString(Constants.TYPE);
		this.price = json.getDouble(Constants.PRICE);
		this.rating = json.getInt(Constants.RATING);
		this.imageFileName = json.getString(Constants.FILENAME);
		this.width = json.getInt(Constants.WIDTH);
		this.height = json.getInt(Constants.HEIGHT);
	}

	public String getId() {

		return id;
	}

	public String getTitle() {

		return title;
	}

	public String getDescription() {

		return description;
	}

	public String getType() {

		return type;
	}

	public double getPrice() {

		return price;
	}

	public int getRating() {

		return rating;
	}

	public String getImageFileName() {

		return imageFileName;
	}

	public int getWidth() {

		return width;
	}

	public int getHeight() {

		return height;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(type);
		dest.writeDouble(price);
		dest.writeInt(rating);
		dest.writeString(imageFileName);
		dest.writeInt(width);
		dest.writeInt(height);
	}

	public static final Parcelable.Creator<MarketItemImpl> CREATOR = new Parcelable.Creator<MarketItemImpl>() {

		@Override
		public MarketItemImpl createFromParcel(Parcel in) {

			return new MarketItemImpl(in);
		}

		@Override
		public MarketItemImpl[] newArray(int size) {

			return new MarketItemImpl[size];
		}
	};

	private MarketItemImpl(Parcel in) {

		id = in.readString();
		title = in.readString();
		description = in.readString();
		type = in.readString();
		price = in.readDouble();
		rating = in.readInt();
		imageFileName = in.readString();
		width = in.readInt();
		height = in.readInt();
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(StringPool.OPEN_CURLY_BRACE);
		sb.append("\"" + Constants.ID + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + id + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.TITLE + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + title + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.DESCRIPTION + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + description + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.TYPE + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + type + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.PRICE + "\"");
		sb.append(StringPool.COLON);
		sb.append(price);
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.RATING + "\"");
		sb.append(StringPool.COLON);
		sb.append(rating);
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.FILENAME + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + imageFileName + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.WIDTH + "\"");
		sb.append(StringPool.COLON);
		sb.append(width);
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.HEIGHT + "\"");
		sb.append(StringPool.COLON);
		sb.append(height);
		sb.append(StringPool.CLOSE_CURLY_BRACE);

		return sb.toString();
	}
}
