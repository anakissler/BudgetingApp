package edu.cnm.deepdive.ak.budgetingapp;

import static edu.cnm.deepdive.ak.budgetingapp.ItemDetailFragment.ARG_CATEGORY_ID;
import static edu.cnm.deepdive.ak.budgetingapp.ItemDetailFragment.ARG_CATEGORY_NAME;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import edu.cnm.deepdive.ak.budgetingapp.entities.Budget;
import edu.cnm.deepdive.ak.budgetingapp.entities.Transaction;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *This Budget fragment lives inside of my Category List Activity
 */
public class BudgetFragment extends Fragment implements OnClickListener {
  /**Budget Field for budget amount*/
  private Budget mBudget;
  /**Title field for my categories*/
  private TextView mTitleField;
  /**Manages my fragments*/
  private FragmentManager mSupportFragmentManager;
  /**Manages my OrmLite database helper*/
  private OrmHelper helper;
  /**Amount field for amount of money spent in a transaction*/
  private EditText mAmountField;
  /**Date for month, day and year for transaction*/
  private EditText mDateField;
  /**Place field for the location of the transaction*/
  private EditText mPlaceField;
  /**Notes field for any notes user wants to input into transaction*/
  private EditText mNotesField;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBudget = new Budget();


//    FragmentManager fm = getSupportFragmentManager();
  }

  /**
   * View and layout inflater used to create or fill view based on my budget fragment
   * @param inflater
   * @param container
   * @param savedInstanceState
   * @return view
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      final View v = inflater.inflate(R.layout.fragment_budget, container, false);

      v.findViewById(R.id.date).setOnClickListener(this);
    mTitleField = (TextView) v.findViewById(R.id.item_title_label);
    final Bundle arguments = getArguments();
    mAmountField = (EditText) v.findViewById(R.id.amount);
    mDateField = (EditText) v.findViewById(R.id.date);
    mPlaceField = (EditText) v.findViewById(R.id.place);
    mNotesField = (EditText) v.findViewById(R.id.notes);
    String categoryName = arguments.getString(ARG_CATEGORY_NAME);
    mTitleField.setText(categoryName);
    v.findViewById(R.id.add_transaction_button).setOnClickListener(new Button.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {
        Transaction transaction = new Transaction();
        transaction.setAmount(Double.parseDouble(mAmountField.getText().toString()));
        transaction.setCategory(getHelper().getCategoryDao().queryForId(arguments.getInt(ARG_CATEGORY_ID)));
        transaction.setPlace(mPlaceField.getText().toString());
        transaction.setNotes(mNotesField.getText().toString());
          transaction
              .setDate(new SimpleDateFormat("MM/dd/yyyy").parse(mDateField.getText().toString()));

          getHelper().getTransactionDao().create(transaction);

          getActivity().getSupportFragmentManager().popBackStack();
        } catch (ParseException e) {
          Toast.makeText(getActivity(), "Invalid Date", Toast.LENGTH_LONG).show();
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    });
    return v;
  }

  /**
   * Creates a view for user to pick a date
   * @param view
   */
  @Override
  public void onClick(View view) {
    DatePickerFragment datePickerFragment = new DatePickerFragment();
    datePickerFragment.show(getFragmentManager(), "transactionDate");
  }

  /**
   *Uses OrmLite database
   * @return
   */
  public synchronized OrmHelper getHelper() {
    if (helper == null) {
      helper = OpenHelperManager.getHelper(getActivity(), OrmHelper.class);
    }
    return helper;
  }

  /**
   *  prevents memory leaks by setting the helper to null when not in use
   */

  public synchronized void releaseHelper() {
    if (helper != null) {
      OpenHelperManager.releaseHelper();
      helper = null;
    }
  }

}
