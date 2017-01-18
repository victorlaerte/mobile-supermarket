package com.victorlaerte.supermarket.model.impl;

import org.json.JSONException;
import org.json.JSONObject;

import com.victorlaerte.supermarket.model.Token;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.StringPool;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by victoroliveira on 15/01/17.
 */

public class UserImpl implements User {

	private String id;
	private String name;
	private String userName;
	private String email;
	private Token token;

	public UserImpl(String id, String name, String userName, String email, Token token) {

		fill(id, name, userName, email, token);
	}

	public UserImpl(JSONObject json) throws JSONException {

		fill(json);
	}

	private void fill(JSONObject json) throws JSONException {

		String id = json.getString(Constants.ID);
		String name = json.getString(Constants.NAME);
		String userName = json.getString(Constants.USERNAME);
		String email = json.getString(Constants.EMAIL);
		Token token = new TokenImpl(json.getJSONObject(Constants.TOKEN));

		fill(id, name, userName, email, token);
	}

	public void fill(String id, String name, String userName, String email, Token token) {

		this.id = id;
		this.name = name;
		this.userName = userName;
		this.email = email;
		this.token = token;
	}

	@Override
	public String getId() {

		return id;
	}

	@Override
	public String getName() {

		return name;
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

		dest.writeString(id);
		dest.writeString(name);
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

		id = in.readString();
		name = in.readString();
		userName = in.readString();
		email = in.readString();
		token = in.readParcelable(TokenImpl.class.getClassLoader());
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(StringPool.OPEN_CURLY_BRACE);
		sb.append("\"" + Constants.ID + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + id + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.NAME + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + name + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.USERNAME + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + userName + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.EMAIL + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + email + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.TOKEN + "\"");
		sb.append(StringPool.COLON);
		sb.append(token.toString());
		sb.append(StringPool.CLOSE_CURLY_BRACE);

		return sb.toString();
	}
}
