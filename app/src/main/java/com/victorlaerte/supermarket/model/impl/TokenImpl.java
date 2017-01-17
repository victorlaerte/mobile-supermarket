package com.victorlaerte.supermarket.model.impl;

import org.json.JSONException;
import org.json.JSONObject;

import com.victorlaerte.supermarket.model.Token;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.StringPool;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by victoroliveira on 15/01/17.
 */

public class TokenImpl implements Token {

	private String accessToken;
	private String refreshToken;
	private String scope;
	private String tokenType;

	public TokenImpl(String accessToken, String refreshToken, String scope, String tokenType) {

		fill(accessToken, refreshToken, scope, tokenType);
	}

	public TokenImpl(String accessToken, String tokenType) {

		fill(accessToken, StringPool.BLANK, StringPool.BLANK, tokenType);
	}

	public TokenImpl(JSONObject json) throws JSONException {

		fill(json);
	}

	private void fill(JSONObject json) throws JSONException {

		fill(json.getString(Constants.ACCESS_TOKEN), json.getString(Constants.REFRESH_TOKEN),
				json.getString(Constants.SCOPE), json.getString(Constants.TOKEN_TYPE));
	}

	public void fill(String accessToken, String refreshToken, String scope, String tokenType) {

		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.scope = scope;
		this.tokenType = tokenType;
	}

	@Override
	public String getAccessToken() {

		return accessToken;
	}

	@Override
	public String getRefresToken() {

		return refreshToken;
	}

	@Override
	public String getScope() {

		return scope;
	}

	@Override
	public String getTokenType() {

		return tokenType;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(accessToken);
		dest.writeString(refreshToken);
		dest.writeString(scope);
		dest.writeString(tokenType);

	}

	public static final Parcelable.Creator<TokenImpl> CREATOR = new Parcelable.Creator<TokenImpl>() {

		@Override
		public TokenImpl createFromParcel(Parcel in) {

			return new TokenImpl(in);
		}

		@Override
		public TokenImpl[] newArray(int size) {

			return new TokenImpl[size];
		}
	};

	private TokenImpl(Parcel in) {

		accessToken = in.readString();
		refreshToken = in.readString();
		scope = in.readString();
		tokenType = in.readString();
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(StringPool.OPEN_CURLY_BRACE);
		sb.append("\"" + Constants.ACCESS_TOKEN + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + accessToken + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.REFRESH_TOKEN + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + refreshToken + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.SCOPE + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + scope + "\"");
		sb.append(StringPool.COMMA);
		sb.append("\"" + Constants.TOKEN_TYPE + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + tokenType + "\"");
		sb.append(StringPool.CLOSE_CURLY_BRACE);

		return sb.toString();
	}
}
