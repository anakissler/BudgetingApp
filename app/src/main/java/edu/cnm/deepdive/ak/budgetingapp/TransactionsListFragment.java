package edu.cnm.deepdive.ak.budgetingapp;

import android.content.ClipData.Item;
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
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.ak.budgetingapp.dummy.DummyContent;
import edu.cnm.deepdive.ak.budgetingapp.dummy.DummyContent.DummyItem;

import edu.cnm.deepdive.ak.budgetingapp.entities.Budget;
import edu.cnm.deepdive.ak.budgetingapp.entities.Transaction;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper.OrmInteraction;
import java.sql.SQLException;
import java.util.List;

/**
 * A fragment representing a list of Items. <p /> Activities containing this fragment MUST implement
 * the {@link OnListFragmentInteractionListener} interface.
 */
public class TransactionsListFragment extends Fragment implements OnClickListener {

  // TODO: Customize parameter argument names
  private static final String ARG_COLUMN_COUNT = "column-count";
  // TODO: Customize parameters
  private int mColumnCount = 1;
  private OnListFragmentInteractionListener mListener;
  private OrmHelper helper;
  private int categoryId;
  private String categoryName;
  private Dao<Budget, Integer> budgetDao;
  private Dao<Transaction, Integer> transactionDao;
  private EditText editBudget;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
   * screen orientation changes).
   */
  public TransactionsListFragment() {
  }

  // TODO: Customize parameter initialization
//  @SuppressWarnings("unused")
//  public static TransactionsListFragment newInstance(int columnCount) {
//    TransactionsListFragment fragment = new TransactionsListFragment();
//    Bundle args = new Bundle();
//    args.putInt(ARG_COLUMN_COUNT, columnCount);
//    fragment.setArguments(args);
//    return fragment;
//  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
      categoryId = getArguments().getInt(ItemDetailFragment.ARG_CATEGORY_ID);
      categoryName = getArguments().getString(ItemDetailFragment.ARG_CATEGORY_NAME);
    }
  }

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
            .setAdapter(new MyTransactionsListRecyclerViewAdapter(transactions, mListener));
      } catch (SQLException e) {
        e.printStackTrace();
      }

    }
    return view;
  }


  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

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
   * <p/>
   * See the Android Training lesson <a href= "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnListFragmentInteractionListener {

    // TODO: Update argument type and name
    void onListFragmentInteraction(Transaction item);
  }


}
