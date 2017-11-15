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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import edu.cnm.deepdive.ak.budgetingapp.entities.Budget;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import edu.cnm.deepdive.ak.budgetingapp.entities.Transaction;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class BudgetFragment extends Fragment implements OnClickListener {

  private Budget mBudget;
  private TextView mTitleField;
  private FragmentManager mSupportFragmentManager;
  private OrmHelper helper;
  private EditText mAmountField;
  private EditText mDateField;
  private EditText mPlaceField;
  private EditText mNotesField;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBudget = new Budget();


//    FragmentManager fm = getSupportFragmentManager();
  }

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


  @Override
  public void onClick(View view) {
    DatePickerFragment datePickerFragment = new DatePickerFragment();
    datePickerFragment.show(getFragmentManager(), "transactionDate");
  }
  public synchronized OrmHelper getHelper() {
    if (helper == null) {
      helper = OpenHelperManager.getHelper(getActivity(), OrmHelper.class);
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
