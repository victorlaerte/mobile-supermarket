package com.victorlaerte.supermarket.util;

/**
 * Created by victoroliveira on 13/01/17.
 */

public interface Constants {

	String ACCESS_TOKEN = "access_token";
	String AUTHORIZATION = "Authorization";
	String BODY = "body";
	String CART = "cart";
	String CREATED_AT = "createdAt";
	String DESCRIPTION = "description";
	String EMAIL = "email";
	String ERROR = "error";
	String ERRORS = "errors";
	String ERROR_DESCRIPTION = "error_description";
	String FILENAME = "filename";
	String FILTER = "filter";
	String GRANT_TYPE = "grant_type";
	String HEIGHT = "height";
	String HTTP_PREFIX = "http://";
	String ID = "id";
	String ITEM = "item";
	String MARKET_ITEM_LIST = "marketItemList";
	String MESSAGE = "message";
	String NAME = "name";
	String PASSWORD = "password";
	String PRICE = "price";
	String RATING = "rating";
	String REASON = "reason";
	String REFRESH_TOKEN = "refresh_token";
	String SCOPE = "scope";
	String STATUS_CODE = "statusCode";
	String STATUS_MSG = "statusMsg";
	String TOKEN_TYPE = "token_type";
	String TYPE = "type";
	String USER = "user";
	String USERNAME = "username";
	String TITLE = "title";
	String TOKEN = "token";
	String WIDTH = "width";

	String DATA_BASE_URL = HTTP_PREFIX + "data.mobilesupermarket.wedeploy.io";
	String LOGIN_BASE_URL = HTTP_PREFIX + "auth.mobilesupermarket.wedeploy.io";
	String PUBLIC_BASE_URL = HTTP_PREFIX + "public.mobilesupermarket.wedeploy.io";

	String CART_ENDPOINT = "/cart";
	String IMAGES_ENDPOINT = "/assets/images/";
	String GET_PRODUCTS_ENDPOINT = "/products";
	String LOGIN_AUTH_ENDPOINT = "/oauth/token";
	String SIGN_UP_ENDPOINT = "/users";
	String USER_ENDPOINT = "/user";
}
