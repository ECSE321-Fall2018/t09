package ca.mcgill.ecse321.rideshare9;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class addJourneyActivity extends AppCompatActivity {
     Spinner seatingSpinner, vehicleSpinner ;
     ListView stopList;
     StopList_adapter stopAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);

        //control the vehicle spinner
        vehicleSpinner = (Spinner) findViewById(R.id.spinnerVehicle);
        Integer[] items1 = new Integer[]{1,2,3,4};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        seatingSpinner.setAdapter(adapter);

        //control the seating spinner
        seatingSpinner = (Spinner) findViewById(R.id.spinnerSeating);
        Integer[] items = new Integer[]{1,2,3,4};
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        seatingSpinner.setAdapter(adapter);

        // control the stop list
        stopList = (ListView) findViewById(R.id.stop_list);
        stopAdapter = new StopList_adapter();
        stopList.setAdapter(stopAdapter);
        stopAdapter.notifyDataSetChanged();
    }
}
