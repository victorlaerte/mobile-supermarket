package com.victorlaerte.supermarket.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.Cart;
import com.victorlaerte.supermarket.model.CartItem;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.TypeFilter;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.model.impl.MarketItemImpl;
import com.victorlaerte.supermarket.service.DeleteItemFromCartTask;
import com.victorlaerte.supermarket.service.GetCartItemsTask;
import com.victorlaerte.supermarket.service.GetMarketItemsTask;
import com.victorlaerte.supermarket.service.SaveItemToCartTask;
import com.victorlaerte.supermarket.util.AndroidUtil;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.DialogUtil;
import com.victorlaerte.supermarket.util.StringPool;
import com.victorlaerte.supermarket.util.SuperMarketUtil;
import com.victorlaerte.supermarket.util.Validator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of itemsList, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of itemsList and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private static final String TAG = ItemListActivity.class.getName();
	private boolean twoPane;
	private User user;
	private SimpleItemRecyclerViewAdapter simpleItemRecyclerViewAdapter;
	private AlertDialog cartDialog;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		user = bundle.getParcelable(Constants.USER);

		setContentView(R.layout.activity_item_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		/* I didn't put any badge over fab showing shopping cart count like in http://public.mobilesupermarket.wedeploy.io/
		 * only because material design documentation says:
		 * Floating action buttons don’t contain app bar icons or status bar notifications.
		 * Don’t layer badges or other elements over a floating action button.  */
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View view) {

				buildCartDialog(view);
			}
		});

		if (SuperMarketUtil.checkTablet(ItemListActivity.this)) {

			twoPane = true;
		}

		loadContent();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	public void saveItemToCart(View view, MarketItem marketItem) {

		if (AndroidUtil.isNetworkAvaliable(getApplicationContext())) {

			new SaveItemToCartTask(ItemListActivity.this, user, marketItem).execute();

		} else {

			DialogUtil.showAlertDialog(ItemListActivity.this, getString(R.string.error),
					getString(R.string.error_cart_offline_mode));
		}

		Snackbar.make(view, getString(R.string.item_added_to_cart), Snackbar.LENGTH_LONG)
				.setAction("Action", null)
				.show();
	}

	private void buildCartDialog(final View view) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ItemListActivity.this);
		LayoutInflater inflater = LayoutInflater.from(ItemListActivity.this);
		View dialogView = inflater.inflate(R.layout.dialog_cart, null);

		LinearLayout linearLayout = (LinearLayout) dialogView.findViewById(R.id.dialog_layout);

		double total = createCartView(inflater, linearLayout);

		String strCheckout = SuperMarketUtil.buildCheckoutString(ItemListActivity.this, total);

		builder.setTitle(R.string.cart);
		builder.setView(dialogView).setPositiveButton(strCheckout, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {

				Snackbar.make(view, getString(R.string.checkout_done), Snackbar.LENGTH_LONG)
						.setAction("Action", null)
						.show();

				if (AndroidUtil.isNetworkAvaliable(ItemListActivity.this)) {

					for (Map.Entry<String, CartItem> entry : Cart.getInstance().getMap().entrySet()) {

						String marketItemId = entry.getKey();
						CartItem cartItem = entry.getValue();

						new DeleteItemFromCartTask(ItemListActivity.this, user, cartItem).execute();
					}
				}

				dialog.dismiss();
				cartDialog = null;
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				dialog.dismiss();
				cartDialog = null;
			}
		});

		cartDialog = builder.create();

		cartDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialogInterface) {

				cartDialog.dismiss();
				cartDialog = null;
			}
		});

		cartDialog.show();
	}

	private double createCartView(final LayoutInflater inflater, final LinearLayout linearLayout) {

		Map<String, CartItem> map = Cart.getInstance().getMap();

		linearLayout.removeAllViews();

		if (map.size() == 0) {

			TextView emptyTextView = new TextView(this);

			emptyTextView.setText(getString(R.string.cart_empty));
			emptyTextView.setGravity(Gravity.CENTER);

			linearLayout.addView(emptyTextView);
		}

		double total = 0;

		for (Map.Entry<String, CartItem> entry : map.entrySet()) {

			final CartItem cartItem = entry.getValue();

			final MarketItem marketItem = cartItem.getMarketItem();
			int quantity = cartItem.getQuantity();

			View dialogItemView = inflater.inflate(R.layout.dialog_item, null);
			final LinearLayout row = (LinearLayout) dialogItemView.findViewById(R.id.item_cart_row);

			ImageView imageView = (ImageView) row.findViewById(R.id.item_cart_image);
			String url = Constants.PUBLIC_BASE_URL + Constants.IMAGES_ENDPOINT + marketItem.getImageFileName();
			Picasso.with(ItemListActivity.this).load(url).resize(100, 100).into(imageView);

			((TextView) row.findViewById(R.id.item_cart_title)).setText(marketItem.getTitle());

			((TextView) row.findViewById(R.id.item_cart_quantity)).setText(String.valueOf(quantity));

			double price = marketItem.getPrice() * quantity;
			total += price;

			((TextView) row.findViewById(R.id.item_cart_price)).setText(
					getString(R.string.currency_symbol) + String.format("%.2f", price));

			final ImageButton deleteButton = (ImageButton) row.findViewById(R.id.item_cart_remove);

			deleteButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {

					if (AndroidUtil.isNetworkAvaliable(ItemListActivity.this)) {

						row.findViewById(R.id.item_cart_progress).setVisibility(View.VISIBLE);
						deleteButton.setVisibility(View.GONE);

						new DeleteItemFromCartTask(ItemListActivity.this, user, cartItem).execute();

					} else {

						DialogUtil.showAlertDialog(ItemListActivity.this, getString(R.string.error),
								getString(R.string.error_cart_offline_mode));
					}
				}
			});

			linearLayout.addView(row);
		}

		if (Validator.isNotNull(cartDialog)) {

			Button button = cartDialog.getButton(DialogInterface.BUTTON_POSITIVE);

			String strCheckout = SuperMarketUtil.buildCheckoutString(ItemListActivity.this, total);

			button.setText(strCheckout);
		}

		return total;
	}

	public void refreshCartView() {

		if (cartDialog != null) {

			createCartView(cartDialog.getLayoutInflater(), (LinearLayout) cartDialog.findViewById(R.id.dialog_layout));
		}

	}

	private void loadContent() {

		if (AndroidUtil.isNetworkAvaliable(this)) {

			showProgress(true);

			GetCartItemsTask getCartItemsTask = new GetCartItemsTask(this, user);
			getCartItemsTask.execute();

			GetMarketItemsTask getMarketItemsTask = new GetMarketItemsTask(this, user, null);
			getMarketItemsTask.execute();

		} else {

			tryOfflineNavigation();
		}
	}

	private void tryOfflineNavigation() {

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String marketItemListStr = preferences.getString(Constants.MARKET_ITEM_LIST, null);

		List<MarketItem> marketItemList = new ArrayList<MarketItem>();

		if (Validator.isNotNull(marketItemListStr)) {

			try {
				JSONArray jArray = new JSONArray(marketItemListStr);

				for (int i = 0; i < jArray.length(); i++) {

					JSONObject curJson = jArray.getJSONObject(i);

					marketItemList.add(new MarketItemImpl(curJson));
				}

			} catch (JSONException e) {

				Log.e(TAG, e.getMessage());
			}
		}

		if (!marketItemList.isEmpty()) {

			setupRecyclerView(true, StringPool.BLANK, marketItemList);

		} else {

			DialogUtil.showAlertDialog(getApplicationContext(), getString(R.string.error),
					getString(R.string.error_offline_mode));
		}
	}

	public void setupRecyclerView(boolean success, String errorMsg, List<MarketItem> marketItemList) {

		View view = findViewById(R.id.item_list);

		showProgress(false);

		if (Validator.isNotNull(view)) {

			RecyclerView recyclerView = (RecyclerView) view;

			if (success) {

				if (Validator.isNull(simpleItemRecyclerViewAdapter)) {

					simpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(ItemListActivity.this,
							marketItemList, user, twoPane);
				} else {

					simpleItemRecyclerViewAdapter.activeTypeFilter(marketItemList);
					simpleItemRecyclerViewAdapter.setTwoPane(twoPane);
				}

				recyclerView.setAdapter(simpleItemRecyclerViewAdapter);

				AndroidUtil.saveToSharedPreferences(ItemListActivity.this, Constants.MARKET_ITEM_LIST,
						SuperMarketUtil.getStringLikeJSONArray(marketItemList));

			} else {

				if (errorMsg.equals(StringPool.BLANK)) {

					errorMsg = getString(R.string.error_unknown_error) + "\n" + getString(
							R.string.error_contact_administrator);

				}

				DialogUtil.showAlertDialog(ItemListActivity.this, getString(R.string.error), errorMsg);
			}
		}
	}

	public void onGetProductsCanceled() {

		showProgress(false);
	}

	private void showProgress(final boolean show) {

		final View progressView = findViewById(R.id.item_list_progress);

		progressView.setVisibility(show ? View.VISIBLE : View.GONE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		final MenuItem searchItem = menu.findItem(R.id.action_search);
		final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				if (Validator.isNotNull(simpleItemRecyclerViewAdapter)) {
					simpleItemRecyclerViewAdapter.filter(query);
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				if (Validator.isNotNull(simpleItemRecyclerViewAdapter)) {
					simpleItemRecyclerViewAdapter.filter(newText);
				}

				return false;
			}
		});

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == R.id.logout) {

			logout();
			return true;

		} else if (id == R.id.filter_no_filter) {

			if (Validator.isNotNull(simpleItemRecyclerViewAdapter)) {
				simpleItemRecyclerViewAdapter.deactivateTypeFilter();
			}

			return true;
		} else if (id == R.id.filter_bakery) {

			filterMarketItemList(TypeFilter.BAKERY);
			return true;
		} else if (id == R.id.filter_dairy) {

			filterMarketItemList(TypeFilter.DAIRY);
			return true;
		} else if (id == R.id.filter_fruit) {

			filterMarketItemList(TypeFilter.FRUIT);
			return true;
		} else if (id == R.id.filter_vegetable) {

			filterMarketItemList(TypeFilter.VEGETABLE);
			return true;
		} else if (id == R.id.filter_meat) {

			filterMarketItemList(TypeFilter.MEAT);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void filterMarketItemList(TypeFilter typeFilter) {

		if (AndroidUtil.isNetworkAvaliable(getApplicationContext())) {

			showProgress(true);

			GetMarketItemsTask getMarketItemsTask = new GetMarketItemsTask(this, user, typeFilter);
			getMarketItemsTask.execute((Void) null);

		} else {

			if (Validator.isNotNull(simpleItemRecyclerViewAdapter)) {

				simpleItemRecyclerViewAdapter.activeOfflineTypeFilter(typeFilter);
			}
		}
	}

	private void logout() {

		/*TODO: Do logout on server if possible*/

		if (AndroidUtil.clearSharedPreferences(getApplicationContext())) {

			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);

			finish();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putParcelable(Constants.USER, user);
		Cart.getInstance().save(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		user = savedInstanceState.getParcelable(Constants.USER);
		Cart.restore(savedInstanceState);
	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public Action getIndexApiAction() {

		Thing object = new Thing.Builder()	.setName("ItemList Page") // TODO: Define a title for the content shown.
											// TODO: Make sure this auto-generated URL is correct.
											.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
											.build();
		return new Action.Builder(Action.TYPE_VIEW)	.setObject(object)
													.setActionStatus(Action.STATUS_TYPE_COMPLETED)
													.build();
	}

	@Override
	public void onStart() {

		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {

		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client, getIndexApiAction());
		client.disconnect();
	}
}
