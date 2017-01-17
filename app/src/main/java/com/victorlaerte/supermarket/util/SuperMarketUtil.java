package com.victorlaerte.supermarket.util;

/**
 * Created by victoroliveira on 16/01/17.
 */

public class SuperMarketUtil {

	public static String getAuthString(String token) {

		return "Bearer " + token;
	}
}
