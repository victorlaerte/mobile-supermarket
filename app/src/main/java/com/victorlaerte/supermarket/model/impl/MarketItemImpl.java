package com.victorlaerte.supermarket.model.impl;

import org.json.JSONException;
import org.json.JSONObject;

import com.victorlaerte.supermarket.model.MarketItem;

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

		this.id = json.getString("id");
		this.title = json.getString("title");
		this.description = json.getString("description");
		this.type = json.getString("type");
		this.price = json.getDouble("price");
		this.rating = json.getInt("rating");
		this.imageFileName = json.getString("filename");
		this.width = json.getInt("width");
		this.height = json.getInt("height");
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
}
