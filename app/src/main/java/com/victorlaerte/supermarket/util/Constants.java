package com.victorlaerte.supermarket.util;

/**
 * Created by victoroliveira on 13/01/17.
 */

public interface Constants {

	String HTTP_PREFIX = "http://";
	String LOGIN_BASE_URL = HTTP_PREFIX + "auth.mobilesupermarket.wedeploy.io";
	String SIGN_UP_ENDPOINT = "/users";
	String LOGIN_AUTH_ENDPOINT = "/oauth/token";
	String LOGIN_GRANT_TYPE = "password";
}
