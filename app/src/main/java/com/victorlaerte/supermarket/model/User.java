package com.victorlaerte.supermarket.model;

import android.os.Parcelable;

/**
 * Created by victoroliveira on 15/01/17.
 */

public interface User extends Parcelable {

	public String getUserName();

	public String getEmail();

	public Token getToken();

}
