package edu.cnm.deepdive.ak.budgetingapp;

import android.content.ClipData.Item;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.ak.budgetingapp.entities.Budget;
import edu.cnm.deepdive.ak.budgetingapp.entities.Category;
import edu.cnm.deepdive.ak.budgetingapp.entities.Transaction;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * An activity representing a list of Items. This activity has different presentations for handset
 * and tablet-size devices. On handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing item details. On tablets, the activity presents
 * the list of items and item details side-by-side using two vertical panes.
 */
public class CategoryListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
   */
  private boolean mTwoPane;
  private OrmHelper helper = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

//    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//    fab.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show();
//      }
//    });

    if (findViewById(R.id.item_detail_container) != null) {
//     The detail container view will be present only in the
//     large-screen layouts (res/values-w900dp).
//     If this view is present, then the
//     activity should be in two-pane mode.
      mTwoPane = true;
    }
    setupListView((ListView) findViewById(R.id.category_list));
  }

  private void setupListView(@NonNull ListView view) {
    try {
      Dao<Category, Integer> dao = getHelper().getCategoryDao();
      QueryBuilder<Category, Integer> builder = dao.queryBuilder();
      builder = builder.orderBy("NAME", true);
      view.setAdapter(new CategoryProgressViewAdaptor(dao.query(builder.prepare())));
      view.setOnItemClickListener(this);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    Category category = (Category) adapterView.getItemAtPosition(i);
    if (mTwoPane) {
      Bundle args = new Bundle();
      args.putInt(ItemDetailFragment.ARG_CATEGORY_ID, category.getId());
      args.putString(ItemDetailFragment.ARG_CATEGORY_NAME, category.getName());
      ItemDetailFragment fragment = new ItemDetailFragment();
      fragment.setArguments(args);
      getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
    } else {
      Intent intent = new Intent(this, ItemDetailActivity.class);
      intent.putExtra(ItemDetailFragment.ARG_CATEGORY_ID, category.getId());
      intent.putExtra(ItemDetailFragment.ARG_CATEGORY_NAME, category.getName());
      startActivity(intent);
    }
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      try {
        Category item = (Category) getItem(position);
        View layout = inflater.inflate(R.layout.category_progress_item, null, false);
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
        ((TextView) layout.findViewById(R.id.category_name)).setText(item.getName());
        ((TextView) layout.findViewById(R.id.budget_amount))
            .setText((budget != null) ? String.format("$%,.2f", budgetAmount) : "");
        ProgressBar progress = (ProgressBar) layout.findViewById(R.id.determinateBar);
        progress.setMax((int) Math.round(budgetAmount));
        progress.setProgress((int) Math.round(total));
        return layout;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
//  public void openBudgetFragment(View v) {
//    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//    BudgetFragment budgetFragment = new BudgetFragment();
//    Bundle arguments = new Bundle();
//    arguments.putString("categoryName", ((TextView)v).getText().toString());
//    arguments.putInt("categoryId", );
//    budgetFragment.setArguments(arguments);
//    fragmentManager.beginTransaction().add(R.id.fragment_container, budgetFragment).commit();
//  }
//  public void showDatePickerDialog(View v) {
//    DialogFragment newFragment = new DatePickerFragment();
//    newFragment.show(getSupportFragmentManager(), "datePicker");
//  }


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
