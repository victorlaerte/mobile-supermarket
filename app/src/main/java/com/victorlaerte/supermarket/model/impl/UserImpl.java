package com.victorlaerte.supermarket.model.impl;

import com.victorlaerte.supermarket.model.Token;
import com.victorlaerte.supermarket.model.User;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by victoroliveira on 15/01/17.
 */

public class UserImpl implements User {

	private String userName;
	private String email;
	private Token token;

	public UserImpl(String userName, String email, Token token) {

		fill(userName, email, token);
	}

	public void fill(String userName, String email, Token token) {

		this.userName = userName;
		this.email = email;
		this.token = token;
	}

	@Override
	public String getUserName() {

		return userName;
	}

	@Override
	public String getEmail() {

		return email;
	}

	@Override
	public Token getToken() {

		return token;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(userName);
		dest.writeString(email);
		dest.writeParcelable(token, flags);
	}

	public static final Parcelable.Creator<UserImpl> CREATOR = new Parcelable.Creator<UserImpl>() {

		@Override
		public UserImpl createFromParcel(Parcel in) {

			return new UserImpl(in);
		}

		@Override
		public UserImpl[] newArray(int size) {

			return new UserImpl[size];
		}
	};

	private UserImpl(Parcel in) {

		userName = in.readString();
		email = in.readString();
		token = in.readParcelable(TokenImpl.class.getClassLoader());
	}
}
