package com.victorlaerte.supermarket.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.squareup.picasso.Picasso;
import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.TypeFilter;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.util.Constants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by victoroliveira on 16/01/17.
 */

public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

	private final List<MarketItem> marketItemListNotFiltered = new ArrayList<MarketItem>();
	private List<MarketItem> marketItemList;
	private final ItemListActivity activity;
	private User user;
	private boolean twoPane;
	private boolean isTypeFilterActive;

	public SimpleItemRecyclerViewAdapter(ItemListActivity context, List<MarketItem> items, User user, boolean twoPane) {

		this.marketItemListNotFiltered.addAll(items);
		this.marketItemList = items;
		this.activity = context;
		this.user = user;
		this.twoPane = twoPane;
		this.isTypeFilterActive = isTypeFilterActive;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {

		holder.mItem = marketItemList.get(position);

		String url = Constants.PUBLIC_BASE_URL + Constants.IMAGES_ENDPOINT + marketItemList	.get(position)
																							.getImageFileName();

		int width = (int) activity.getResources().getDimension(R.dimen.small_image_width);
		int height = (int) activity.getResources().getDimension(R.dimen.small_image_height);
		Picasso.with(holder.mView.getContext()).load(url).resize(width, height).into(holder.imageView);

		holder.mContentView.setText(marketItemList.get(position).getTitle());

		holder.price.setText(
				activity.getString(R.string.currency_symbol) + String.valueOf(marketItemList.get(position).getPrice()));

		holder.mView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (twoPane) {

					Bundle arguments = new Bundle();

					arguments.putParcelable(Constants.ITEM, holder.mItem);
					ItemDetailFragment fragment = new ItemDetailFragment();
					fragment.setArguments(arguments);
					activity.getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.item_detail_container, fragment)
							.commit();
				} else {

					Context context = v.getContext();

					Intent intent = new Intent(context, ItemDetailActivity.class);
					intent.putExtra(Constants.USER, user);
					intent.putExtra(Constants.ITEM, holder.mItem);

					context.startActivity(intent);
				}
			}
		});
	}

	public void setTwoPane(boolean twoPane) {

		this.twoPane = twoPane;
	}

	@Override
	public int getItemCount() {

		return marketItemList.size();
	}

	public void activeTypeFilter(List<MarketItem> filteredMarketItemList) {

		isTypeFilterActive = true;

		marketItemList.clear();
		marketItemList.addAll(filteredMarketItemList);

		notifyDataSetChanged();
	}

	public void activeOfflineTypeFilter(TypeFilter typeFilter) {

		isTypeFilterActive = true;

		marketItemList.clear();

		for (MarketItem item : marketItemListNotFiltered) {

			if (item.getType().equals(typeFilter.getLabel())) {

				marketItemList.add(item);
			}
		}

		notifyDataSetChanged();
	}

	public void deactivateTypeFilter() {

		isTypeFilterActive = false;

		marketItemList.clear();
		marketItemList.addAll(marketItemListNotFiltered);

		notifyDataSetChanged();
	}

	public void filter(String text) {

		marketItemList.clear();

		if (text.isEmpty()) {

			marketItemList.addAll(marketItemListNotFiltered);

		} else {

			text = text.toLowerCase();

			for (MarketItem item : marketItemListNotFiltered) {

				// @formatter:off
				if (StringUtils.stripAccents(item.getTitle().toLowerCase()).contains(StringUtils.stripAccents(text)) || 
					StringUtils	.stripAccents(item.getDescription().toLowerCase()).contains(StringUtils.stripAccents(text))) {

					marketItemList.add(item);
				}
				// @formatter:on

			}
		}
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		public final View mView;
		public final ImageView imageView;
		public final TextView mContentView;
		public final TextView price;
		public MarketItem mItem;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			imageView = (ImageView) view.findViewById(R.id.small_image);
			mContentView = (TextView) view.findViewById(R.id.content);
			price = (TextView) view.findViewById(R.id.price);
		}
	}
}