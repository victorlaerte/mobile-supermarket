package com.victorlaerte.supermarket.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.squareup.picasso.Picasso;
import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.CartItem;
import com.victorlaerte.supermarket.util.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by victoroliveira on 16/01/17.
 */

public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

	private final List<CartItem> cartItemListNotFiltered = new ArrayList<CartItem>();
	private final List<CartItem> cartItemList;
	private final ItemListActivity activity;
	private boolean twoPane;

	public SimpleItemRecyclerViewAdapter(ItemListActivity context, List<CartItem> items, boolean twoPane) {

		this.cartItemListNotFiltered.addAll(items);
		this.cartItemList = items;
		this.activity = context;
		this.twoPane = twoPane;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {

		holder.mItem = cartItemList.get(position);

		String url = Constants.PUBLIC_BASE_URL + Constants.IMAGES_ENDPOINT + cartItemList	.get(position)
																							.getImageFileName();

		int width = (int) activity.getResources().getDimension(R.dimen.small_image_width);
		int height = (int) activity.getResources().getDimension(R.dimen.small_image_height);
		Picasso.with(holder.mView.getContext()).load(url).resize(width, height).into(holder.imageView);
		holder.mContentView.setText(cartItemList.get(position).getTitle());

		if (!twoPane && activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

			int maxLength = 28;
			holder.price.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
		}

		holder.price.setText(
				activity.getString(R.string.currency_symbol) + String.valueOf(cartItemList.get(position).getPrice()));

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
					intent.putExtra(Constants.ITEM, holder.mItem);

					context.startActivity(intent);
				}
			}
		});
	}

	@Override
	public int getItemCount() {

		return cartItemList.size();
	}

	public void filter(String text) {

		cartItemList.clear();

		if (text.isEmpty()) {

			cartItemList.addAll(cartItemListNotFiltered);

		} else {

			text = text.toLowerCase();

			for (CartItem item : cartItemListNotFiltered) {

				// @formatter:off
				if (StringUtils.stripAccents(item.getTitle().toLowerCase()).contains(StringUtils.stripAccents(text)) || 
					StringUtils	.stripAccents(item.getDescription().toLowerCase()).contains(StringUtils.stripAccents(text))) {

					cartItemList.add(item);
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
		public CartItem mItem;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			imageView = (ImageView) view.findViewById(R.id.small_image);
			mContentView = (TextView) view.findViewById(R.id.content);
			price = (TextView) view.findViewById(R.id.price);
		}

		@Override
		public String toString() {

			return super.toString() + " '" + mContentView.getText() + "'";
		}
	}
}