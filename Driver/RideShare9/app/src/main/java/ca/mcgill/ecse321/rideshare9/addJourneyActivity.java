package ca.mcgill.ecse321.rideshare9;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class addJourneyActivity extends AppCompatActivity {
     Spinner seatingSpinner, vehicleSpinner ;
     ListView stopListView;
     StopList_adapter stopAdapter;
     Button addJourneyBtn, cancelJourneyBtn, addStopBtn;
     TextView startLocationtx, endLocationtx, costFullTriptx, emptyText_stopLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);

        //control the vehicle spinner
        vehicleSpinner = (Spinner) findViewById(R.id.spinnerVehicle);
        Integer[] items1 = new Integer[]{1,2,3,4};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items1);
        seatingSpinner.setAdapter(adapter);

        //control the seating spinner
        seatingSpinner = (Spinner) findViewById(R.id.spinnerSeating);
        Integer[] items = new Integer[]{0,1,2,3,4};
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        seatingSpinner.setAdapter(adapter);

        // control the stop list
        stopListView = (ListView) findViewById(R.id.stop_list);
        //emptyText_stopLv = (TextView) findViewById(R.id.) ;
        stopListView.setEmptyView(emptyText_stopLv);
        stopAdapter = new StopList_adapter();
        stopListView.setAdapter(stopAdapter);
        stopAdapter.notifyDataSetChanged();
    }

    public void addJourney(){

        startLocationtx = (TextView) findViewById(R.id.startLocationText);
        endLocationtx =  (TextView) findViewById(R.id.endLocationText);
        costFullTriptx = (TextView) findViewById(R.id.finalcostText);


        addJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startLocationtx.getText().toString().trim().equals("") || endLocationtx.getText().toString().trim().equals("")
                        || costFullTriptx.getText().toString().trim().equals("") ){
                    Log.d("Error", "One of the field is Empty");
                    return;
                }
            }

            //TODO
        });



    }
}
