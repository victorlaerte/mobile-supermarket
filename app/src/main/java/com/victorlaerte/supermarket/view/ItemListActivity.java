package com.victorlaerte.supermarket.view;

import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.ProductStockContent;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.model.impl.UserImpl;
import com.victorlaerte.supermarket.service.GetProductsTask;
import com.victorlaerte.supermarket.util.AndroidUtil;
import com.victorlaerte.supermarket.util.DialogUtil;
import com.victorlaerte.supermarket.util.StringPool;
import com.victorlaerte.supermarket.util.Validator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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
	private boolean twoPane;
	private User user;
	private SimpleItemRecyclerViewAdapter simpleItemRecyclerViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		user = bundle.getParcelable(UserImpl.class.getName());

		setContentView(R.layout.activity_item_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				DialogUtil.showAlertDialog(ItemListActivity.this, getString(R.string.shopping_cart), StringPool.BLANK);
			}
		});

		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-w900dp).
			// If this view is present, then the
			// activity should be in two-pane mode.
			twoPane = true;
		}

		loadContent();
	}

	private void loadContent() {

		if (AndroidUtil.isNetworkAvaliable(this)) {

			showProgress(true);

			GetProductsTask getProductsTask = new GetProductsTask(this, user);
			getProductsTask.execute((Void) null);

		} else {

			DialogUtil.showAlertDialog(this, getString(R.string.error),
					getString(R.string.erro_no_internet_connection));
		}
	}

	public void setupRecyclerView(boolean sucess, String errorMsg, ProductStockContent productStockContent) {

		View view = findViewById(R.id.item_list);

		showProgress(false);

		if (Validator.isNotNull(view)) {

			RecyclerView recyclerView = (RecyclerView) view;

			if (sucess) {

				simpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(ItemListActivity.this,
						productStockContent.getItemList(), twoPane);

				recyclerView.setAdapter(simpleItemRecyclerViewAdapter);

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
		}
		return super.onOptionsItemSelected(item);
	}

	private void logout() {

		/*TODO: Do logout on server if possible*/

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		SharedPreferences.Editor edit = preferences.edit();

		edit.clear();
		boolean commited = edit.commit();

		if (commited) {

			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);

			finish();
		}
	}
}
