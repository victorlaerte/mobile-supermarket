package com.victorlaerte.supermarket.util;

/**
 * Created by victoroliveira on 13/01/17.
 */

public interface Constants {

	String HTTP_PREFIX = "http://";
	String LOGIN_BASE_URL = HTTP_PREFIX + "auth.mobilesupermarket.wedeploy.io";
	String SIGN_UP_ENDPOINT = "/users";
	String USER_ENDPOINT = "/user";
	String LOGIN_AUTH_ENDPOINT = "/oauth/token";
	String DATA_BASE_URL = HTTP_PREFIX + "data.mobilesupermarket.wedeploy.io";

	String GET_PRODUCTS_ENDPOINT = "/products";
	String AUTHORIZATION = "Authorization";
	String CART_ENDPOINT = "/cart";

	String PUBLIC_BASE_URL = HTTP_PREFIX + "public.mobilesupermarket.wedeploy.io";
	String IMAGES_ENDPOINT = "/assets/images/";

	String ACCESS_TOKEN = "access_token";
	String TOKEN_TYPE = "token_type";
	String CART = "cart";
	String TYPE = "type";
	String USERNAME = "username";
	String USER = "user";
	String NAME = "name";
	String BODY = "body";
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
	String ITEM = "item";
	String FILTER = "filter";
	String TOKEN = "token";
	String REFRESH_TOKEN = "refresh_token";
	String SCOPE = "scope";
	String ID = "id";
	String MARKET_ITEM_LIST = "marketItemList";
}
