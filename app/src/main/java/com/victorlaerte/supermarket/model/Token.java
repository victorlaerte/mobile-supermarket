package com.victorlaerte.supermarket.model;

import android.os.Parcelable;

/**
 * Created by victoroliveira on 15/01/17.
 */

public interface Token extends Parcelable {

	String getAccessToken();

	String getRefresToken();

	String getScope();

	String getTokenType();

}
