package ca.mcgill.ecse321.rideshare9;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import ca.mcgill.ecse321.rideshare9.model.Vehicle;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class AddJourneyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private String error = null; //tutorial
     Spinner seatingSpinner, vehicleSpinner ;
     ListView stopListView;
     CreateAdStopsAdapter stopAdapter;
     private List<Vehicle> vehicleList = new ArrayList<>();
     private ArrayAdapter<Vehicle> vehicleAdapter;
     private Set<Long> stops_Set;
     ArrayAdapter<Integer> seatingAdapter;
     int maxSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);

        // Get initial content for vehicle spinner
        getCarsList();

        // fill the vehicle spinner
        vehicleSpinner =  findViewById(R.id.spinnerVehicle);
        vehicleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleList);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(vehicleAdapter);



        // available max seating
        vehicleSpinner.setOnItemSelectedListener(this);




        //control the seating spinner
        seatingSpinner = findViewById(R.id.spinnerSeating);
        Integer[] items = new Integer[maxSeats+1];
        for (int i=0 ; i < items.length ;i++){
            items[i]=i;
        }
        seatingAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        seatingSpinner.setAdapter(seatingAdapter);


       //control the stop list
        stopListView = (ListView) findViewById(R.id.stop_list);
        stopAdapter = new CreateAdStopsAdapter();
        stopListView.setAdapter(stopAdapter);
        stopAdapter.notifyDataSetChanged();

        // tutorial
        refreshErrorMessage();
    }

    public void addStop(View v ){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //set the Alert Dialog Title
        alertDialog.setTitle(R.string.create_stop);

        //set StopName field
        alertDialog.setMessage("Enter Stop Name then Price");

        final EditText stopName = new EditText(this);
        final EditText price = new EditText(this );
        price.setInputType(InputType.TYPE_CLASS_NUMBER);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(stopName);
        lay.addView(price);
        alertDialog.setView(lay);


        alertDialog.setPositiveButton("Make Stop",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
                        String authentication = "Bearer " + sharedPreferences.getString("token", null);

                        //  Set headers for the request
                        HttpUtils.addHeader("Authorization", authentication);

                        RequestParams params = new RequestParams();
                        params.put("stopName",stopName.getText());
                        params.put("price",price.getText());

                        HttpUtils.post("stop/add-stop",params,new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                            }
                        });
                        Toast.makeText(getApplicationContext(),"Stop Added", Toast.LENGTH_SHORT).show();

                    }
                });
        // Setting Negative Cancel Button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        // closed

        // Showing Alert Message
        alertDialog.show();

    }

    public void addJourney(View v) {

        // start location
        TextView tv = (TextView) findViewById(R.id.startLocationText);
        String startLocation = tv.getText().toString();

        // end location, trip of title in request parameters
        tv =  findViewById(R.id.endLocationText);
        String endLocation = tv.getText().toString();

        // date
        tv = (TextView) findViewById(R.id.startDate);
        String text = tv.getText().toString();
        String  comps[] = text.split("-");

        int year = Integer.parseInt(comps[2]);
        int month = Integer.parseInt(comps[1]);
        int day = Integer.parseInt(comps[0]);
        String startTime = comps[2] + "-" + comps[1] + "-" + comps[0] + " ";

        // start time
        tv = findViewById(R.id.starttime);
        text = tv.getText().toString();
        comps = text.split(":");

        int startHours = Integer.parseInt(comps[0]);
        int startMinutes = Integer.parseInt(comps[1]);

        // time format: yyyy-mm-dd HH:mm:ss
        startTime += text + ":00";



        //String startTime = year;

        // cost of trip
        tv =  findViewById(R.id.finalcostText);
        String fullTripCost = tv.getText().toString() ; // change type depending on request parameter

        // Vehicle
        long id_vehicle = vehicleSpinner.getSelectedItemId();

        // available seating
        int  availableSeats = 0;
        try {
            availableSeats = Integer.parseInt(seatingSpinner.getSelectedItem().toString());
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        // Reminder: calling the service looks like this:
        // http://192.168.56.50:8088/createEvent?eventName=tst&date=2013-10-23&startTime=00:00&endTime=23:59

        RequestParams rp = new RequestParams();

        NumberFormat formatter = new DecimalFormat("00");
        rp.add("title", endLocation);
        rp.add("startTime", startTime);
        rp.add("startLocation", startLocation);
        //rp.add("seatAvailable", availableSeats);
        //rp.add("stops", );
        //rp.add("vehicle", );

        HttpUtils.post("/adv/create-adv", rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                refreshErrorMessage();
                ((TextView) findViewById(R.id.startLocationText)).setText("");
                ((TextView) findViewById(R.id.endLocationText)).setText("");
                ((TextView) findViewById(R.id.finalcostText)).setText("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });
    }

    public void onCancel(View v){
        //got back to the main tab pages
        Intent intent = new Intent(this, RideShare9.class);
        startActivity(intent);
    }


    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.error_addJourney);
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }

    }

    private Bundle getTimeFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split(":");
        int hour = 12;
        int minute = 0;

        if (comps.length == 2) {
            hour = Integer.parseInt(comps[0]);
            minute = Integer.parseInt(comps[1]);
        }

        rtn.putInt("hour", hour);
        rtn.putInt("minute", minute);

        return rtn;
    }

    private Bundle getDateFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split("-");
        int day = 1;
        int month = 1;
        int year = 1;

        if (comps.length == 3) {
            day = Integer.parseInt(comps[0]);
            month = Integer.parseInt(comps[1]);
            year = Integer.parseInt(comps[2]);
        }

        rtn.putInt("day", day);
        rtn.putInt("month", month-1);
        rtn.putInt("year", year);

        return rtn;
    }

    public void showTimePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getTimeFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getDateFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setTime(int id, int h, int m) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d:%02d", h, m));
    }

    public void setDate(int id, int d, int m, int y) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d-%02d-%04d", d, m + 1, y));

    }

    private void getCarsList() {

        //  Get SharedPreferences which holds the JWT Token
        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        String authentication = "Bearer " + sharedPreferences.getString("token", null);

        //  Set headers for the request
        HttpUtils.addHeader("Authorization", authentication);

        HttpUtils.get("vehicle/get-cars", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                vehicleList.addAll(vehiclesFromJSONArray(response));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }

        });
    }


    private List<Vehicle> vehiclesFromJSONArray(JSONArray jsonAdArray) {
        int vehicleCount = jsonAdArray.length();
        List<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < vehicleCount; i++) {
            JSONObject advertisement = jsonAdArray.optJSONObject(i);
            vehicles.add(vehicleFromJSONObject(advertisement));
        }

        return vehicles;
    }

    private Vehicle vehicleFromJSONObject(JSONObject jsonAdObject) {
        long vehicleId = jsonAdObject.optInt("id");
        String licencePlate = jsonAdObject.optString("licencePlate");
        String model = jsonAdObject.optString("model");
        String color = jsonAdObject.optString("color");
        int maxSeat = jsonAdObject.optInt("maxSeat");
        long driver = jsonAdObject.optLong("driver");

        return new Vehicle(vehicleId, licencePlate , model , color , maxSeat , driver);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Vehicle user = (Vehicle) parent.getSelectedItem();
        maxSeats = user.getMaxSeat();
        //seatingAdapter.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
