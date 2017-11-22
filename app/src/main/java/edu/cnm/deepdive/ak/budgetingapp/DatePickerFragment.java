package edu.cnm.deepdive.ak.budgetingapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import java.util.Calendar;

/**
 * Fragment that passes a view of a calendar
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

  /**
   * User can pick a day, month and year for transaction
   * @param savedInstanceState
   * @return
   */
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current date as the default date in the picker
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    // Create a new instance of DatePickerDialog and return it
    return new DatePickerDialog(getActivity(), this, year, month, day);
  }

  /**
   * Returns a view
   * @param view
   * @param year
   * @param month
   * @param day
   */
  public void onDateSet(DatePicker view, int year, int month, int day) {
    ((EditText)getActivity().findViewById(R.id.date)).setText((month+ 1) + "/" + day +"/" + year);

  }

  }


