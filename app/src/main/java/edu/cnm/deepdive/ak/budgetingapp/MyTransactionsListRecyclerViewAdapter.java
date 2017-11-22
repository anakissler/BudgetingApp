package edu.cnm.deepdive.ak.budgetingapp;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.cnm.deepdive.ak.budgetingapp.TransactionsListFragment.OnListFragmentInteractionListener;
import edu.cnm.deepdive.ak.budgetingapp.entities.Transaction;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper.OrmInteraction;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display my item list and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyTransactionsListRecyclerViewAdapter extends
    RecyclerView.Adapter<MyTransactionsListRecyclerViewAdapter.ViewHolder> {

  /**Transaction list field*/
  private final List<Transaction> mValues;
  /**fragment called when user clicks on a list item*/
  private final OnListFragmentInteractionListener mListener;
  /**Accesses the OrmLite database*/
  private final OrmHelper mHelper;

  /**
   * Binds app-specific data to set the view that is displayed
   * @param items
   * @param listener
   * @param helper
   */
  public MyTransactionsListRecyclerViewAdapter(List<Transaction> items,
      OnListFragmentInteractionListener listener, OrmHelper helper) {
    mHelper = helper;
    mValues = items;
    mListener = listener;
  }

  /**
   * Item View for transactions
   * @param parent
   * @param viewType
   * @return view
   */
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_transactionslist, parent, false);
    return new ViewHolder(view);
  }

  /**
   * sets the view for the transaction list
   * Alert dialog is used to prompt user to make a decision
   * @param holder
   * @param position
   */
  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    holder.mItem = mValues.get(position);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    holder.mIdView.setText(sdf.format(mValues.get(position).getDate()));
    holder.mContentView.setText(String.format("$%.2f",mValues.get(position).getAmount()));
    holder.mPlaceField.setText(mValues.get(position).getPlace());
    holder.mNotesField.setText(mValues.get(position).getNotes());
    holder.mDeleteButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(final View view) {
        AlertDialog.Builder builder = new Builder(view.getContext());
        builder.setMessage("Permanently delete this transaction?").setTitle("");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            OrmHelper helper = ((OrmInteraction) view.getContext()).getHelper();
            try {
              helper.getTransactionDao().delete(holder.mItem);
              mValues.remove(position);
              notifyItemRemoved(position);
            } catch (SQLException e) {
              Toast.makeText(view.getContext(), "Unable to delete", Toast.LENGTH_LONG);
            }

          }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            //User clicked the Cancel Button
          }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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

  /**
   * @return size
   */
  @Override
  public int getItemCount() {
    return mValues.size();
  }

  /**
   * View for my list of items
   */

  public class ViewHolder extends RecyclerView.ViewHolder {

    /**View field*/
    public final View mView;
    /**Assigns and ID to view field*/
    public final TextView mIdView;
    /**View for content field*/
    public final TextView mContentView;
    /**View for place text*/
    public final TextView mPlaceField;
    /**View for notes text*/
    public final TextView mNotesField;
    /**Field for transaction item*/
    public Transaction mItem;
    /**View field for delete button*/
    public final Button mDeleteButton;

    /**
     * View for all fields in transaction
     * @param view
     */
    public ViewHolder(View view) {
      super(view);
      mView = view;
      mIdView = (TextView) view.findViewById(R.id.id);
      mContentView = (TextView) view.findViewById(R.id.content);
      mPlaceField = (TextView) view.findViewById(R.id.place);
      mNotesField = (TextView) view.findViewById(R.id.notes);
      mDeleteButton = (Button) view.findViewById(R.id.delete);
    }

    /**
     * Populates transaction list with fields from each transaction
     * @return strings
     */
    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }
  }
}
