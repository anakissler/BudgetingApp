package edu.cnm.deepdive.ak.budgetingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.ak.budgetingapp.entities.Budget;
import edu.cnm.deepdive.ak.budgetingapp.entities.Category;
import edu.cnm.deepdive.ak.budgetingapp.entities.Transaction;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper.OrmInteraction;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * An activity representing a list of Items. This activity has different presentations for handset
 * and tablet-size devices. On handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing item details. On tablets, the activity presents
 * the list of items and item details side-by-side using two vertical panes.
 */
public class CategoryListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, CategoryEditFragment.Callback,
    OrmInteraction {

  /**Whether or not the activity is in two-pane mode, i.e. running on a tablet device.*/
  private boolean mTwoPane;
  /**Uses the OrmLite Database*/
  private OrmHelper helper = null;
  /**Accessing the budget data*/
  private Dao budgetDao;
  /**Accessing the transaction data*/
  private Dao transactionDao;
  /**Bridge for data for Progress Bar View*/
  private CategoryProgressViewAdaptor adapter = null;
  /**Menu to pick a month*/
  private Spinner monthPicker;

  /**
   * Saves the data that has been created and passes it back to the activity
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

/** Adds the totals of all categories to form a total budget, adds all transactions to show how much
 *  user has spent out of the total budget.
 */
    try {
      budgetDao = CategoryListActivity.this.getHelper().getBudgetDao();
      transactionDao = CategoryListActivity.this.getHelper().getTransactionDao();
      Calendar today = Calendar.getInstance();
      Calendar startOfMonth = Calendar.getInstance();
      Calendar endOfMonth = Calendar.getInstance();
      startOfMonth.set(Calendar.DAY_OF_MONTH, startOfMonth.getMinimum(Calendar.DAY_OF_MONTH));
      endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getMaximum(Calendar.DAY_OF_MONTH));
      QueryBuilder<Budget, Integer> budgetQB = budgetDao.queryBuilder();
      QueryBuilder<Transaction, Integer> transactionQB = transactionDao.queryBuilder();
      budgetQB.selectRaw("SUM(AMOUNT)");
      budgetQB.where()
          .eq("YEAR", today.get(Calendar.YEAR))
          .and().eq("MONTH", today.get(Calendar.MONTH) + 1);
      String[] results = (String [])budgetDao.queryRaw(budgetQB.prepareStatementString()).getFirstResult();
      double totalBudget= (results != null && results.length > 0 && results[0] != null)
          ? Double.parseDouble(results[0])
          : 0;
      transactionQB.selectRaw("SUM(AMOUNT)");
      transactionQB.where()
          .between("DATE", startOfMonth.getTime(), endOfMonth.getTime());
      results = (String []) transactionDao.queryRaw(transactionQB.prepareStatementString())
          .getFirstResult();
      double totalTransactions = (results != null && results.length > 0 && results[0] != null)
          ? Double.parseDouble(results[0])
          : 0;
      ((TextView) findViewById(R.id.total)).setText(String.format("$%,.2f / $%,.2f", totalTransactions, totalBudget));
    } catch (SQLException e) {
      e.printStackTrace();
    }



    if (findViewById(R.id.item_detail_container) != null) {

      mTwoPane = true;
    }
    setupListView((ListView) findViewById(R.id.category_list));
    monthPicker = (Spinner) findViewById(R.id.monthspinner);
  }

  /**
   * sets up ListView with an "Add new Category" option.
   */

 private void setupListView(@NonNull ListView view) {
    try {
      Dao<Category, Integer> dao = getHelper().getCategoryDao();
      QueryBuilder<Category, Integer> builder = dao.queryBuilder();
      builder = builder.orderBy("NAME", true);
      List<Category> categories = dao.query(builder.prepare());
      Category dummy = new Category();
      dummy.setName("Add new category...");
      categories.add(dummy);
        adapter = new CategoryProgressViewAdaptor(categories);
      view.setAdapter(adapter);
      view.setOnItemClickListener(this);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets up a clickable View for my list
   * @param adapterView
   * @param view
   * @param i
   * @param l
   */
  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    Category category = (Category) adapterView.getItemAtPosition(i);
    if (category.getId() > 0) {
      if (mTwoPane) {
        Bundle args = new Bundle();
        args.putInt(ItemDetailFragment.ARG_CATEGORY_ID, category.getId());
        args.putString(ItemDetailFragment.ARG_CATEGORY_NAME, category.getName());
        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment)
            .commit();
      } else {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailFragment.ARG_CATEGORY_ID, category.getId());
        intent.putExtra(ItemDetailFragment.ARG_CATEGORY_NAME, category.getName());
        startActivity(intent);
      }
    } else {
      CategoryEditFragment fragment = new CategoryEditFragment();
      fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());

    }
  }

  /**
   * Populates my ListView with my categories.
   */
  @Override
  public void refreshList() {
    setupListView((ListView) findViewById(R.id.category_list));
  }

  public class CategoryProgressViewAdaptor
      extends ArrayAdapter<Category> {

    private Dao<Budget, Integer> budgetDao;
    private Dao<Transaction, Integer> transactionDao;
    private LayoutInflater inflater;

    public CategoryProgressViewAdaptor(List<Category> items) {
      super(CategoryListActivity.this, R.layout.category_progress_item, items);
      try {
        budgetDao = CategoryListActivity.this.getHelper().getBudgetDao();
        transactionDao = CategoryListActivity.this.getHelper().getTransactionDao();
        inflater = CategoryListActivity.this.getLayoutInflater();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }

    /**
     * Fills progress bar with transactions for specific category.
      * @param position
     * @param convertView
     * @param parent
     * @return layout
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      try {
        Category item = (Category) getItem(position);
        View layout = inflater.inflate(R.layout.category_progress_item, null, false);
        ((TextView) layout.findViewById(R.id.category_name)).setText(item.getName());
        ProgressBar progress = (ProgressBar) layout.findViewById(R.id.determinateBar);
        TextView categoryBudget = ((TextView) layout.findViewById(R.id.budget_amount));
        if (item.getId() > 0) {

          Calendar today = Calendar.getInstance();
          Calendar startOfMonth = Calendar.getInstance();
          Calendar endOfMonth = Calendar.getInstance();
          startOfMonth.set(Calendar.DAY_OF_MONTH, startOfMonth.getMinimum(Calendar.DAY_OF_MONTH));
          endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getMaximum(Calendar.DAY_OF_MONTH));
          QueryBuilder<Budget, Integer> budgetQB = budgetDao.queryBuilder();
          QueryBuilder<Transaction, Integer> transactionQB = transactionDao.queryBuilder();
          budgetQB.where().eq("CATEGORY_ID", item.getId())
              .and().eq("YEAR", today.get(Calendar.YEAR))
              .and().eq("MONTH", today.get(Calendar.MONTH) + 1);
          Budget budget = budgetDao.queryForFirst(budgetQB.prepare());
          transactionQB.selectRaw("SUM(AMOUNT)");
          transactionQB.where().eq("CATEGORY_ID", item.getId())
              .and().between("DATE", startOfMonth.getTime(), endOfMonth.getTime());
          String[] results = transactionDao.queryRaw(transactionQB.prepareStatementString())
              .getFirstResult();
          double total = (results != null && results.length > 0 && results[0] != null)
              ? Double.parseDouble(results[0])
              : 0;
          double budgetAmount = (budget != null) ? budget.getAmount() : total;
          categoryBudget.setText((budget != null) ? String.format("$%,.2f", budgetAmount) : "");

          progress.setMax((int) Math.round(budgetAmount));
          progress.setProgress((int) Math.round(total));
        } else {
          categoryBudget.setVisibility(View.INVISIBLE);
          progress.setVisibility(View.INVISIBLE);
        }
        return layout;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
  /**
   * Keeps Month Picked in Spinner when App is started.
   */
  @Override
  protected void onStart() {
    super.onStart();
    monthPicker.setSelection(getPreferences(MODE_PRIVATE).getInt("selectedMonth", 0));
    getHelper();
  }

  /**
   *Keeps Month Picked in Spinner when App is started.
   */
  @Override
  protected void onStop() {
    getPreferences(MODE_PRIVATE).edit().putInt("selectedMonth", monthPicker.getSelectedItemPosition()).apply();
    releaseHelper();
    super.onStop();
  }

  /**
   * creates an instance of the OrmHelper
   * @return helper
   */
  public synchronized OrmHelper getHelper() {
    if (helper == null) {
      helper = OpenHelperManager.getHelper(this, OrmHelper.class);
    }
    return helper;
  }

  /**
   *prevents memory leaks by setting the helper to null when not in use.
   */
  public synchronized void releaseHelper() {
    if (helper != null) {
      OpenHelperManager.releaseHelper();
      helper = null;
    }
  }
}
