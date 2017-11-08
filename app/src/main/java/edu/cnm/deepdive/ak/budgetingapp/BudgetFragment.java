package edu.cnm.deepdive.ak.budgetingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.cnm.deepdive.ak.budgetingapp.entities.Budget;

public class BudgetFragment extends Fragment {

  private Budget mBudget;
  private TextView mTitleField;
  private FragmentManager mSupportFragmentManager;


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBudget = new Budget();


    FragmentManager fm = getSupportFragmentManager();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_budget, container, false);

    mTitleField = (TextView) v.findViewById(R.id.item_title_label);
    Bundle arguments = getArguments();
    String categoryName = arguments.getString("categoryName");
    mTitleField.setText(categoryName);
    return v;
  }

  public FragmentManager getSupportFragmentManager() {
    return mSupportFragmentManager;
  }
}
