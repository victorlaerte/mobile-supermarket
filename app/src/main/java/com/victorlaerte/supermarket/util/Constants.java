package com.victorlaerte.supermarket.util;

/**
 * Created by victoroliveira on 13/01/17.
 */

public interface Constants {

	String HTTP_PREFIX = "http://";
	String LOGIN_BASE_URL = HTTP_PREFIX + "auth.mobilesupermarket.wedeploy.io";
	String SIGN_UP_ENDPOINT = "/users";
	String LOGIN_AUTH_ENDPOINT = "/oauth/token";

	String ACCESS_TOKEN = "access_token";
	String TOKEN_TYPE = "token_type";
	String USERNAME = "username";
	String NAME = "name";
	String EMAIL = "email";
	String PASSWORD = "password";
	String GRANT_TYPE = "grant_type";
	String CREATED_AT = "createdAt";
	String STATUS_CODE = "statusCode";
	String STATUS_MSG = "statusMsg";
	String ERROR = "error";
	String ERRORS = "errors";
	String ERROR_DESCRIPTION = "error_description";
	String REASON = "reason";
	String MESSAGE = "message";

}
