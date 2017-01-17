package com.victorlaerte.supermarket.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.victorlaerte.supermarket.model.CartItem;
import com.victorlaerte.supermarket.model.ProductStockContent;

/**
 * Created by victoroliveira on 16/01/17.
 */

public class ProductStockContentImpl implements ProductStockContent {

	private List<CartItem> itemList = new ArrayList<CartItem>();

	private Map<String, CartItem> itemMap = new HashMap<String, CartItem>();

	public ProductStockContentImpl(List<CartItem> itemsList) {

		fill(itemsList);

	}

	private void fill(List<CartItem> itemsList) {

		this.itemList = itemsList;

		for (CartItem carItem : itemsList) {

			itemMap.put(carItem.getId(), carItem);
		}
	}

	public void addItem(CartItem item) {

		itemList.add(item);
		itemMap.put(item.getId(), item);
	}

	public void setItemList(List<CartItem> itemList) {

		this.itemList = itemList;
	}

	public void setItemMap(Map<String, CartItem> itemMap) {

		this.itemMap = itemMap;
	}

	public List<CartItem> getItemList() {

		return itemList;
	}

	public Map<String, CartItem> getItemMap() {

		return itemMap;
	}

}
