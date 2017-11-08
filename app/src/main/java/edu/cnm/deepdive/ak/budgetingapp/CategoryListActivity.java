package edu.cnm.deepdive.ak.budgetingapp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import edu.cnm.deepdive.ak.budgetingapp.entities.Category;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import java.sql.SQLException;
import java.util.List;

/**
 * An activity representing a list of Items. This activity has different presentations for handset
 * and tablet-size devices. On handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing item details. On tablets, the activity presents
 * the list of items and item details side-by-side using two vertical panes.
 */
public class CategoryListActivity extends AppCompatActivity {

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

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    if (findViewById(R.id.item_detail_container) != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true;
    }
    setupRecyclerView((RecyclerView) findViewById(R.id.category_list));
  }

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    try {
      Dao<Category, Integer> dao = getHelper().getCategoryDao();
      QueryBuilder<Category, Integer> builder = dao.queryBuilder();
      builder = builder.orderBy("NAME", true);
      recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(dao.query(builder.prepare())));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public class SimpleItemRecyclerViewAdapter
      extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final List<Category> mValues;

    public SimpleItemRecyclerViewAdapter(List<Category> items) {
      mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.category_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
      holder.mItem = mValues.get(position);
      holder.mNameView.setText(mValues.get(position).getName());

      holder.mView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();
          } else {
            Context context = v.getContext();
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

            context.startActivity(intent);
          }
        }
      });
    }

    @Override
    public int getItemCount() {
      return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      public final View mView;
      public final TextView mNameView;
      public Category mItem;

      public ViewHolder(View view) {
        super(view);
        mView = view;
        mNameView = (TextView) view.findViewById(R.id.category_name);
      }

      @Override
      public String toString() {
        return super.toString() + " '" + mNameView.getText() + "'";
      }
    }
  }
  public void openBudgetFragment(View v) {
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    BudgetFragment budgetFragment = new BudgetFragment();
    Bundle arguments = new Bundle();
    arguments.putString("categoryName", ((TextView)v).getText().toString());
    budgetFragment.setArguments(arguments);
    fragmentManager.beginTransaction().add(R.id.fragment_container, budgetFragment).commit();
  }
  public void showDatePickerDialog(View v) {
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getFragmentManager(), "datePicker");
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
