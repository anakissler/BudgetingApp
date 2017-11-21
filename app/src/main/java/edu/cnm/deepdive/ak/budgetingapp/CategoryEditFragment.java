package edu.cnm.deepdive.ak.budgetingapp;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import edu.cnm.deepdive.ak.budgetingapp.entities.Budget;
import edu.cnm.deepdive.ak.budgetingapp.entities.Category;
import edu.cnm.deepdive.ak.budgetingapp.helpers.OrmHelper.OrmInteraction;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;


public class CategoryEditFragment extends DialogFragment {

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog alert;
    Builder builder = new Builder(getActivity());
    builder.setTitle("Enter New Category");
    builder.setView(R.layout.fragment_category_edit);
    builder.setPositiveButton("Save", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        Category category = new Category();
        category.setName(((EditText) getDialog().findViewById(R.id.edit_category)).getText().toString());
        Budget budget = new Budget();
        budget.setMonth(Calendar.getInstance().get(Calendar.MONTH)+ 1);
        budget.setYear(Calendar.getInstance().get(Calendar.YEAR));
        budget.setCategory(category);
        try {
          ((OrmInteraction) getActivity()).getHelper().getCategoryDao().create(category);
          ((OrmInteraction) getActivity()).getHelper().getBudgetDao().create(budget);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        ((Callback) getActivity()).refreshList();
      }
    });
    builder.setNegativeButton("Cancel", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

      }
    });
    alert = builder.create();
    return alert;
  }

  public interface Callback {
    void refreshList();
  }
}
