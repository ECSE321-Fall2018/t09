package ca.mcgill.ecse321.rideshare9.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import ca.mcgill.ecse321.rideshare9.ChangeAdvertisementActivity;
import ca.mcgill.ecse321.rideshare9.addJourneyActivity;

public class TimePickerFragmentChange extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    String label;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = 0;
        int minute = 0;

        // Parse the existing time from the arguments
        Bundle args = getArguments();
        if (args != null) {
            hour = args.getInt("hour");
            minute = args.getInt("minute");
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        ChangeAdvertisementActivity myActivity = (ChangeAdvertisementActivity) getActivity();
        myActivity.setTime(getArguments().getInt("id"), hourOfDay, minute);
    }
}
