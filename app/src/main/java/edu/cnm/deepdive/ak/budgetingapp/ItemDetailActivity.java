package edu.cnm.deepdive.ak.budgetingapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper.OrmInteraction;


/**
 * An activity representing a single Item detail screen. This activity is only used narrow width
 * devices. On tablet-size devices, item details are presented side-by-side with a list of items in
 * a {@link CategoryListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity implements Button.OnClickListener,
    OrmInteraction {

  private String categoryName;
  private int categoryId;
  private OrmHelper helper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category_detail);
    Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_transactions);
    fab.setOnClickListener(this);

    // Show the Up button in the action bar.
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
    categoryName = (getIntent().getExtras().getString(ItemDetailFragment.ARG_CATEGORY_NAME));
    categoryId = (getIntent().getExtras().getInt(ItemDetailFragment.ARG_CATEGORY_ID));

    CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
        findViewById(R.id.toolbar_layout);
    if (appBarLayout != null) {
      appBarLayout.setTitle(categoryName);
    }
    TransactionsListFragment fragment = new TransactionsListFragment();
    fragment.setArguments(getIntent().getExtras());
    getSupportFragmentManager().beginTransaction().add(R.id.transactions, fragment).commit();

    // savedInstanceState is non-null when there is fragment state
    // saved from previous configurations of this activity
    // (e.g. when rotating the screen from portrait to landscape).
    // In this case, the fragment will automatically be re-added
    // to its container so we don't need to manually add it.
    // For more information, see the Fragments API guide at:
    //
    // http://developer.android.com/guide/components/fragments.html
    //
//    if (savedInstanceState == null) {
//
//      Bundle arguments = new Bundle();
//      arguments.putString(ItemDetailFragment.ARG_CATEGORY_ID,
//          getIntent().getStringExtra(ItemDetailFragment.ARG_CATEGORY_ID));
//      ItemDetailFragment fragment = new ItemDetailFragment();
//      fragment.setArguments(arguments);
//      getSupportFragmentManager().beginTransaction()
//          .add(R.id.item_detail_container, fragment)
//          .commit();
//    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      // This ID represents the Home or Up button. In the case of this
      // activity, the Up button is shown. For
      // more details, see the Navigation pattern on Android Design:
      //
      // http://developer.android.com/design/patterns/navigation.html#up-vs-back
      //
      navigateUpTo(new Intent(this, CategoryListActivity.class));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClick(View view) {
    FrameLayout transactions = (FrameLayout)findViewById(R.id.transactions);
    BudgetFragment budgetFragment = new BudgetFragment();
    Bundle bundle = new Bundle();
    bundle.putString(ItemDetailFragment.ARG_CATEGORY_NAME, categoryName);
    bundle.putInt(ItemDetailFragment.ARG_CATEGORY_ID,categoryId);
    budgetFragment.setArguments(bundle);
    getSupportFragmentManager().beginTransaction().replace(R.id.transactions, budgetFragment).addToBackStack("budget").commit();
//    getSupportFragmentManager().beginTransaction().replace(R.id.transactions, budgetFragment).commit();
  }
  @Override
  protected void onStart() {
    super.onStart();
    getHelper();
  }

  @Override
  protected void onStop() {
    releaseHelper();
    super.onStop();
  }

  // creates an instance of the OrmHelper
  public synchronized OrmHelper getHelper() {
    if (helper == null) {
      helper = OpenHelperManager.getHelper(this, OrmHelper.class);
    }
    return helper;
  }

  // prevents memory leaks by setting the helper to null when not in use
  public synchronized void releaseHelper() {
    if (helper != null) {
      OpenHelperManager.releaseHelper();
      helper = null;
    }
  }
}
