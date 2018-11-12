package ca.mcgill.ecse321.rideshare9;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ca.mcgill.ecse321.rideshare9.Adapter.ChangeAdStopsAdapter;
import ca.mcgill.ecse321.rideshare9.Adapter.StopList_adapter;
import ca.mcgill.ecse321.rideshare9.Fragments.DatePickerFragment;
import ca.mcgill.ecse321.rideshare9.Fragments.DatePickerFragmentChange;
import ca.mcgill.ecse321.rideshare9.Fragments.TimePickerFragment;
import ca.mcgill.ecse321.rideshare9.Fragments.TimePickerFragmentChange;
import ca.mcgill.ecse321.rideshare9.model.Advertisement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.model.Stop;
import ca.mcgill.ecse321.rideshare9.model.Vehicle;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class ChangeAdvertisementActivity extends AppCompatActivity {
    // advertisement sent in a bundle to the activity when it starts
    private Advertisement advertisement;
    // stops recyclerView
    private RecyclerView rvStops;
    private ChangeAdStopsAdapter adStopsAdapter;
    private LinearLayoutManager layoutManager;
    // Views
    private ListView stopListView;
    private long advertisementId;

    private Button adModifyButton;

    Spinner seatingSpinner, vehicleSpinner ;
    JSONArray stopJSON;
    StopList_adapter stopAdapter;


    private List<String> vehicleList = new ArrayList<>();
    private ArrayAdapter<String> vehicleAdapter;
    private final List<Stop> stopList = new ArrayList<>();

    private Context context = this;
    private EditText finalDestinationField;
    private EditText startDestinationField;

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

        TimePickerFragmentChange newFragment = new TimePickerFragmentChange();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getDateFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        DatePickerFragmentChange newFragment = new DatePickerFragmentChange();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void addStop(View v ){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //set the Alert Dialog Title
        alertDialog.setTitle("Create Stop");

        //set StopName field
        alertDialog.setMessage("Enter Stop Name then Price");

        final EditText stopName = new EditText(this);
        final EditText price = new EditText(this );
        price.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(80, 0, 80, 0);
        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(stopName, params);
        lay.addView(price, params);
        alertDialog.setView(lay);
        stopListView = (ListView) findViewById(R.id.stop_list);


        alertDialog.setPositiveButton("Make Stop",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {


                        JSONObject jsonObject = new JSONObject();
                        try{
                            jsonObject.put("stopName", stopName.getText());
                            jsonObject.put("price", price.getText());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        /*convert the jsonbdoy into string entity that can be sent*/
                        ByteArrayEntity entity = null;
                        try {
                            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
                            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                        }catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        //  Set headers for the request

                        Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(getApplicationContext()))};
                        HttpUtils.post(getApplicationContext(), "stop/add-stop",headers, entity, "application/json", new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                // TODO: Add global
                                Stop s = new Stop();
                                s.setName(stopName.getText().toString());
                                try{
                                    s.setId(Long.parseLong(response.getString("id")));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                s.setPrice(Float.parseFloat(price.getText().toString()));
                                stopList.add(s);
                                stopListView = findViewById(R.id.stop_list);
                                stopListView.setAdapter(new StopList_adapter(stopList, getApplicationContext()));

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

    public void modifyJourney(View v){
        // Title of trip
        //  TextView tv = (TextView) findViewById(R.id.trip_title_text);
        //  String tripTitle = tv.getText().toString();

        // start location
        // start location
        TextView tv = (TextView) findViewById(R.id.modify_startLocationText);
        String startLocation = tv.getText().toString();



        tv = (TextView) findViewById(R.id.modify_trip_title_text);
        String tripTitle = tv.getText().toString();

        // start time
        tv = (TextView) findViewById(R.id.starttime);
        String text = tv.getText().toString();
        String comps[] = text.split(":");

        int startHours = Integer.parseInt(comps[0]);
        int startMinutes = Integer.parseInt(comps[1]);

        // date
        tv = (TextView) findViewById(R.id.startDate);
        text = tv.getText().toString();
        comps = text.split("-");

        int year = Integer.parseInt(comps[2]);
        int month = Integer.parseInt(comps[1]);
        int day = Integer.parseInt(comps[0]);



        // Vehicle
        String vehicle = vehicleSpinner.getSelectedItem().toString().substring(4);
        String id = "";
        for (int i = 0; i < vehicle.length(); i++) {
            if (!Character.isDigit(vehicle.charAt(i))) {
                break;
            }
            id = id + vehicle.charAt(i);
        }


        // available seating00
        String seat = vehicleSpinner.getSelectedItem().toString();
        String maxseat = seat.substring(seat.indexOf("seats: ") + "seats: ".length());


        String startDateTime = year+"-"+month+"-"+day+" "+startHours+":"+startMinutes+":"+"00";

        JSONArray stopList = new JSONArray();
        ListView stopListView = findViewById(R.id.stop_list);
        for (int i = 0; i < stopListView.getCount(); i++) {
            stopList.put(stopListView.getAdapter().getItemId(i));
        }
        JSONObject toAddAdv = new JSONObject();


        try {
            toAddAdv.put("id", advertisementId);
            toAddAdv.put("title", tripTitle);
            toAddAdv.put("startTime", startDateTime);
            toAddAdv.put("startLocation", startLocation);
            toAddAdv.put("seatAvailable", Integer.parseInt(maxseat));
            toAddAdv.put("stops", stopList);
            toAddAdv.put("vehicle", Integer.parseInt(id));
            toAddAdv.put("endLocation", ((Stop)stopListView.getAdapter().getItem(stopListView.getCount() - 1)).getName());
            Log.d("json", toAddAdv.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(toAddAdv.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(getApplicationContext()))};
        HttpUtils.put(getApplicationContext(),"/adv/update-adv", headers, entity,"application/json",new JsonHttpResponseHandler(){
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_advertisement);
        Bundle bundle = getIntent().getExtras();
        advertisement = bundle.getParcelable("advertisement_data");
        Long valueVid = -1L; // or other values
        if(bundle != null)
            valueVid = bundle.getLong("adv_id");
        long adv_id = valueVid.longValue();
        advertisementId = adv_id;
        vehicleList = new ArrayList<>();
        vehicleSpinner =  findViewById(R.id.spinnerVehicle);
        vehicleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleList);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set up vehicle spinner
        Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(getApplicationContext()))};

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        String authentication = "Bearer " + sharedPreferences.getString("token", null);
        stopList.clear();
        //  Set headers for the request
        HttpUtils.addHeader("Authorization", authentication);


        HttpUtils.get("adv/get-by-id/" + adv_id, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Advertisement ad = advertisementFromJSONObject(response);
                TextView title = findViewById(R.id.modify_trip_title_text);
                TextView startLocation = findViewById(R.id.modify_startLocationText);
                title.setText(ad.getTitle());
                startLocation.setText(ad.getStartLocation());
                for (int i = 0; i < ad.getStops().size(); i++) {
                    final int vehicleI =i;

                        HttpUtils.get("/stop/get-by-id/" + ad.getStops().get(i).getId(),
                                null, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        Stop s = new Stop();
                                        try{
                                            s.setId(Long.parseLong(response.getString("id")));
                                            s.setName(response.getString("stopName"));
                                            s.setPrice(Float.parseFloat(response.getString("price")));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        stopList.add(s);

                                        stopListView = findViewById(R.id.stop_list);
                                        stopListView.setAdapter(new StopList_adapter(stopList, getApplicationContext()));

                                    }
                        });

                }
            }
        });

        HttpUtils.get(getApplicationContext(), "vehicle/get-cars", headers, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try{
                        Vehicle vitem = new Vehicle();
                        vitem.setId(Long.parseLong(response.getJSONObject(i).getString("id")));
                        vitem.setModel(response.getJSONObject(i).getString("model"));
                        vitem.setLicencePlate(response.getJSONObject(i).getString("licencePlate"));
                        vitem.setColor(response.getJSONObject(i).getString("color"));
                        vitem.setMaxSeat(Integer.parseInt(response.getJSONObject(i).getString("maxSeat"))); 
                        Log.d("catched vehicle", vitem.getModel());
                        vehicleList.add("id: " + vitem.getId() + ", model: " + vitem.getModel() + ", seats: " + vitem.getMaxSeat());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                vehicleSpinner.setAdapter(vehicleAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("vehicle", "cannot get vehicle");
            }
        });




    }

    public void setTime(int id, int h, int m) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d:%02d", h, m));
    }

    public void setDate(int id, int d, int m, int y) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d-%02d-%04d", d, m + 1, y));
    }
    private Advertisement advertisementFromJSONObject(JSONObject jsonAdObject) {
        int adId = jsonAdObject.optInt("id");
        int adSeatsAvailable = jsonAdObject.optInt("seatAvailable");
        int adVehicleId = jsonAdObject.optInt("vehicle");
        //set vehicle with just id for now
        Vehicle vehicle = new Vehicle();
        vehicle.setId(adVehicleId);
        int adDriverId = jsonAdObject.optInt("driver");
        String adTitle = jsonAdObject.optString("title");
        String adStartTime = jsonAdObject.optString("startTime");
        String adStartLocation = jsonAdObject.optString("startLocation");
        String adStatus = jsonAdObject.optString("status");
        List<Stop> adStops = new ArrayList<>();

        JSONArray stops = jsonAdObject.optJSONArray("stops");

        //  Get the number of stops for the advertisement
        int stopCount = stops.length();

        for (int j = 0; j < stopCount; j++) {
            Stop newStop = new Stop();
            // Only the id is set for now
            newStop.setId(stops.optLong(j));
            adStops.add(newStop);
        }

        return new Advertisement(adId, adSeatsAvailable, vehicle, adDriverId, adTitle,
                adStartTime, adStartLocation, adStatus, adStops);
    }
    public void myback(View view) {
        finish();
    }
}
