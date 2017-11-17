package edu.cnm.deepdive.ak.budgetingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import edu.cnm.deepdive.ak.budgetingapp.TransactionsListFragment.OnListFragmentInteractionListener;
import edu.cnm.deepdive.ak.budgetingapp.dummy.DummyContent.DummyItem;
import edu.cnm.deepdive.ak.budgetingapp.entities.Transaction;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}. TODO: Replace the implementation with code
 * for your data type.
 */
public class MyTransactionsListRecyclerViewAdapter extends
    RecyclerView.Adapter<MyTransactionsListRecyclerViewAdapter.ViewHolder> {

  private final List<Transaction> mValues;
  private final OnListFragmentInteractionListener mListener;
  private final OrmHelper mHelper;

  public MyTransactionsListRecyclerViewAdapter(List<Transaction> items,
      OnListFragmentInteractionListener listener, OrmHelper helper) {
    mHelper = helper;
    mValues = items;
    mListener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_transactionslist, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    holder.mItem = mValues.get(position);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    holder.mIdView.setText(sdf.format(mValues.get(position).getDate()));
    holder.mContentView.setText(String.format("$%.2f",mValues.get(position).getAmount()));
    holder.mPlaceField.setText(mValues.get(position).getPlace());
    holder.mNotesField.setText(mValues.get(position).getNotes());
    holder.mRemoveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        try {
          mHelper.getTransactionDao().delete(holder.mItem);
          mValues.remove(position);
          notifyItemRemoved(position);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    });
    holder.mView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null != mListener) {
          // Notify the active callbacks interface (the activity, if the
          // fragment is attached to one) that an item has been selected.
          mListener.onListFragmentInteraction(holder.mItem);
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
    public final TextView mIdView;
    public final TextView mContentView;
    public final TextView mPlaceField;
    public final TextView mNotesField;
    public Transaction mItem;
    public final Button mRemoveButton;

    public ViewHolder(View view) {
      super(view);
      mView = view;
      mIdView = (TextView) view.findViewById(R.id.id);
      mContentView = (TextView) view.findViewById(R.id.content);
      mPlaceField = (TextView) view.findViewById(R.id.place);
      mNotesField = (TextView) view.findViewById(R.id.notes);
      mRemoveButton = (Button) view.findViewById(R.id.remove);
    }

    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }
  }
}
