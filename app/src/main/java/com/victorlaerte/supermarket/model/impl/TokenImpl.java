package com.victorlaerte.supermarket.model.impl;

import com.victorlaerte.supermarket.model.Token;
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
}
