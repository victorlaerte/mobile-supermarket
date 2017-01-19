package com.victorlaerte.supermarket.view;

import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.service.SaveItemToCartTask;
import com.victorlaerte.supermarket.util.AndroidUtil;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.DialogUtil;
import com.victorlaerte.supermarket.util.Validator;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of itemsList
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

	private MarketItem marketItem;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);

		Parcelable parcelableMarketItem = getIntent().getParcelableExtra(Constants.ITEM);
		Parcelable parcelableUser = getIntent().getParcelableExtra(Constants.USER);

		if (Validator.isNotNull(parcelableMarketItem)) {

			marketItem = (MarketItem) parcelableMarketItem;
		}

		if (Validator.isNotNull(parcelableUser)) {

			user = (User) parcelableUser;
		}

		Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				saveItemToCart(view);
			}
		});

		// Show the Up button in the action bar.
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putParcelable(Constants.ITEM, marketItem);

			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
		}
	}

	public void saveItemToCart(View view) {

		if (AndroidUtil.isNetworkAvaliable(getApplicationContext())) {

			new SaveItemToCartTask(ItemDetailActivity.this, user, marketItem).execute();

		} else {

			DialogUtil.showAlertDialog(ItemDetailActivity.this, getString(R.string.error),
					getString(R.string.error_cart_offline_mode));
		}

		Snackbar.make(view, getString(R.string.item_added_to_cart), Snackbar.LENGTH_LONG)
				.setAction("Action", null)
				.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == android.R.id.home) {

			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putParcelable(Constants.ITEM, marketItem);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		marketItem = savedInstanceState.getParcelable(Constants.ITEM);
	}
}
