package edu.cnm.deepdive.ak.budgetingapp;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper.OrmInteraction;

/**
 * Activity to add a category to list
 */
public class CategoryEditActivity extends AppCompatActivity implements OrmInteraction {

  /**Assigns a category ID to added category*/
  private int categoryId;
  /**Uses the OrmLite database*/
  private OrmHelper helper;

  /**
   * Saves the data that has been created
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category_detail);
    Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
//    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_transactions);
    fab.setVisibility(View.GONE);

    // Show the Up button in the action bar.
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
    categoryId = (getIntent().getExtras().getInt(ItemDetailFragment.ARG_CATEGORY_ID));

    CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
        findViewById(R.id.toolbar_layout);
    if (appBarLayout != null) {
      appBarLayout.setTitle("newCategory");
    }
    CategoryEditFragment fragment = new CategoryEditFragment();
    fragment.setArguments(getIntent().getExtras());
    getSupportFragmentManager().beginTransaction().add(R.id.transactions, fragment).commit();
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

  /**Starts the helper*/
  @Override
  protected void onStart() {
    super.onStart();
    getHelper();
  }

  /**Stops the helper*/
  @Override
  protected void onStop() {
    releaseHelper();
    super.onStop();
  }

  /**
   *creates an instance of the OrmHelper
   */

  @Override
  public synchronized OrmHelper getHelper() {
    if (helper == null) {
      helper = OpenHelperManager.getHelper(this, OrmHelper.class);
    }
    return helper;
  }

  /**
   * prevents memory leaks by setting the helper to null when not in use
   */
  public synchronized void releaseHelper() {
    if (helper != null) {
      OpenHelperManager.releaseHelper();
      helper = null;
    }
  }
}
