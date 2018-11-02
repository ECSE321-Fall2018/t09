package ca.mcgill.ecse321.rideshare9;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = 2018;
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Parse the existing time from the arguments
        Bundle args = getArguments();
        if (args != null) {
            year = args.getInt("year");
            month = args.getInt("month");
            day = args.getInt("day");
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Activity myActivity =  getActivity();
        if(myActivity instanceof ChangeAdvertisementActivity) {
            ((ChangeAdvertisementActivity) myActivity).setDate(getArguments().getInt("id"), day,month,year);
        } else if (myActivity instanceof addJourneyActivity){
            ((addJourneyActivity) myActivity).setDate(getArguments().getInt("id"), day,month,year);
        }
    }
}
