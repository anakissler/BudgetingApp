package edu.cnm.deepdive.ak.budgetingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.ak.budgetingapp.entities.Budget;
import edu.cnm.deepdive.ak.budgetingapp.entities.Transaction;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper.OrmInteraction;
import java.sql.SQLException;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TransactionsListFragment extends Fragment implements OnClickListener {
  /**The fragment argument representing the column count that this fragment represents.*/
  private static final String ARG_COLUMN_COUNT = "column-count";
  /**Where the column count starts*/
  private int mColumnCount = 1;
  /**Implements list interaction listener*/
  private OnListFragmentInteractionListener mListener;
  /**Adds the category Id to list*/
  private int categoryId;
  /**Adds the category name to the list*/
  private String categoryName;
  /**Accesses the Budget information from database*/
  private Dao<Budget, Integer> budgetDao;
  /**Accesses the transaction information from database*/
  private Dao<Transaction, Integer> transactionDao;
  /**Accesses the text filed to edit budget amount*/
  private EditText editBudget;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
   * screen orientation changes).
   */
  public TransactionsListFragment() {
  }

  /**
   * Saves the data so that it can be passed back to the activity
   * @param savedInstanceState
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
      categoryId = getArguments().getInt(ItemDetailFragment.ARG_CATEGORY_ID);
      categoryName = getArguments().getString(ItemDetailFragment.ARG_CATEGORY_NAME);
    }
  }

  /**
   * Creates a view for my transaction list to pass back to the activity
   * @param inflater
   * @param container
   * @param savedInstanceState
   * @return view
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_transactionslist_list, container, false);
    RecyclerView recyclerView = view.findViewById(R.id.transaction_list);
    editBudget = view.findViewById(R.id.monthly_budget);
    view.findViewById(R.id.save).setOnClickListener(this);
    // Set the adapter
    if (recyclerView instanceof RecyclerView) {
      Context context = view.getContext();
//      RecyclerView recyclerView = (RecyclerView) view;
      if (mColumnCount <= 1) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
      } else {
        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
      }
      try {
        Dao<Transaction, Integer> dao = ((OrmInteraction)getActivity()).getHelper().getTransactionDao();
        QueryBuilder<Transaction, Integer> builder = dao.queryBuilder();
        builder.where().eq("CATEGORY_ID", categoryId);
        List<Transaction> transactions = dao.query(builder.prepare());
        recyclerView
            .setAdapter(new MyTransactionsListRecyclerViewAdapter(transactions, mListener, ((OrmInteraction)getActivity()).getHelper()));
      } catch (SQLException e) {
        e.printStackTrace();
      }

    }
    return view;
  }

  /**
   * fragment is detached after it is destroyed
   */
  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * returns a view once an item is clicked
   * @param view
   */
  @Override
  public void onClick(View view) {
    try {
      Dao<Budget, Integer> dao = ((OrmInteraction)getActivity()).getHelper().getBudgetDao();
      QueryBuilder<Budget, Integer> builder = dao.queryBuilder();
      builder.where().eq("CATEGORY_ID", categoryId);
      Budget budget = dao.queryForFirst(builder.prepare());
      budget.setAmount(Double.parseDouble(editBudget.getText().toString()));
      dao.update(budget);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    getActivity().finish();
  }

  /**
   * This interface must be implemented by activities that contain this fragment to allow an
   * interaction in this fragment to be communicated to the activity and potentially other fragments
   * contained in that activity.
   */
  public interface OnListFragmentInteractionListener {

    void onListFragmentInteraction(Transaction item);
  }


}
