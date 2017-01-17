package com.victorlaerte.supermarket.model;

import java.util.List;
import java.util.Map;

public interface ProductStockContent {

    public void addItem(CartItem item);

    public void setItemList(List<CartItem> itemList);

    public void setItemMap(Map<String, CartItem> itemMap);

    public List<CartItem> getItemList();

    public Map<String, CartItem> getItemMap();

}
