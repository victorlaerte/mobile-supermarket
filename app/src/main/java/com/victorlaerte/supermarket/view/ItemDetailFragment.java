package com.victorlaerte.supermarket.view;

import com.squareup.picasso.Picasso;
import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.SuperMarketUtil;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

	private MarketItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(Constants.ITEM)) {

			mItem = getArguments().getParcelable(Constants.ITEM);

			Activity activity = this.getActivity();
			CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
			if (appBarLayout != null) {
				appBarLayout.setTitle(mItem.getTitle());
			}

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.item_detail, container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {

			String url = Constants.PUBLIC_BASE_URL + Constants.IMAGES_ENDPOINT + mItem.getImageFileName();

			Picasso.with(getContext()).load(url).resize(mItem.getWidth(), mItem.getHeight()).into(
					(ImageView) rootView.findViewById(R.id.item_large_image));

			((RatingBar) rootView.findViewById(R.id.rating_bar)).setRating(mItem.getRating());

			ImageButton imageButtonAdd = (ImageButton) rootView.findViewById(R.id.item_add);

			if (SuperMarketUtil.checkTablet(
					getActivity()) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

				imageButtonAdd.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {

						Activity activity = getActivity();

						if (activity instanceof ItemListActivity) {

							((ItemListActivity) activity).saveItemToCart(view, mItem);
						}
					}
				});

				imageButtonAdd.setVisibility(View.VISIBLE);
			} else {
				imageButtonAdd.setVisibility(View.GONE);
			}

			((TextView) rootView.findViewById(R.id.item_price)).setText(
					getString(R.string.currency_symbol) + mItem.getPrice());

			((TextView) rootView.findViewById(R.id.item_description)).setText(mItem.getDescription());
		}

		return rootView;
	}
}
