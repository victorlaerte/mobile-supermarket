package com.victorlaerte.supermarket.util;

import java.util.List;

import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.MarketItem;

import android.content.Context;

/**
 * Created by victoroliveira on 16/01/17.
 */

public class SuperMarketUtil {

    public static String getAuthString(String token) {

        return "Bearer " + token;
    }

    public static String buildCheckoutString(Context context, double total) {

        return context.getString(
                R.string.checkout) + StringPool.SPACE + StringPool.DASH + StringPool.SPACE + context.getString(
                R.string.currency_symbol) + String.format("%.2f", total);
    }

    public static String getStringLikeJSONArray(List<MarketItem> marketItemList) {

        StringBuilder sb = new StringBuilder(StringPool.OPEN_BRACKET);

        for (int i = 0; i < marketItemList.size(); i++) {

            MarketItem marketItem = marketItemList.get(i);

            sb.append(marketItem.toString());

            if (!(i == marketItemList.size() - 1)) {

                sb.append(StringPool.COMMA);
            }
        }

        sb.append((StringPool.CLOSE_BRACKET));

        return sb.toString();
    }
}
