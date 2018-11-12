package ca.mcgill.ecse321.rideshare9;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import ca.mcgill.ecse321.rideshare9.Adapter.StopList_adapter;
import ca.mcgill.ecse321.rideshare9.Fragments.DatePickerFragment;
import ca.mcgill.ecse321.rideshare9.Fragments.DatePickerFragmentChange;
import ca.mcgill.ecse321.rideshare9.Fragments.TimePickerFragment;
import ca.mcgill.ecse321.rideshare9.Fragments.TimePickerFragmentChange;
import ca.mcgill.ecse321.rideshare9.model.Stop;
import ca.mcgill.ecse321.rideshare9.model.Vehicle;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class addJourneyActivity extends AppCompatActivity {
    private String error = null; //tutorial
    Spinner seatingSpinner, vehicleSpinner ;
    JSONArray stopJSON;
    ListView stopListView;
    StopList_adapter stopAdapter;


    private List<String> vehicleList = new ArrayList<>();
    private ArrayAdapter<String> vehicleAdapter;
    private SharedPreferences sharedPre;
    private final List<Stop> stopList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journey);

        //control the vehicle spinner
        vehicleList = new ArrayList<>();
        vehicleSpinner =  findViewById(R.id.spinnerVehicle);
        vehicleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleList);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(getApplicationContext()))};

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

        //control the seating spinner


        // control the stop list



        sharedPre=getSharedPreferences("config", MODE_PRIVATE);

        // tutorial
        refreshErrorMessage();
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

    public void addJourney(View v) {
        // Title of trip
      //  TextView tv = (TextView) findViewById(R.id.trip_title_text);
      //  String tripTitle = tv.getText().toString();

        // start location
        TextView tv = (TextView) findViewById(R.id.startLocationText);
        String startLocation = tv.getText().toString();



        tv = (TextView) findViewById(R.id.trip_title_text);
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
        HttpUtils.post(getApplicationContext(),"/adv/create-adv", headers, entity,"application/json",new JsonHttpResponseHandler(){
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

    private JSONArray postStop(String location, String cost) {
        SharedPreferences sharedPre = getSharedPreferences("config", Context.MODE_PRIVATE);
        String authentication = "Bearer " + sharedPre.getString("token", null);

        HttpUtils.addHeader("Authorization", authentication);
        HttpUtils.addHeader("Content-Type", "application/json");

        RequestParams params = new RequestParams();
        params.put("stopName", location);
        params.put("price", cost);

        HttpUtils.post("stop/add-stop",params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("JSONArray", response.toString());
                super.onSuccess(statusCode, headers, response);
                stopJSON = response;

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                error += responseString;
                refreshErrorMessage();
            }
        });
        refreshErrorMessage();
        return stopJSON;
    }

    private void refreshErrorMessage() {
        error = "";
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


    public static void saveStopInfo(Context context, String location, String cost) {
        SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPre.edit();
        editor.putString("location", location);
        editor.putString("cost", cost);
        editor.commit();
    }
    public void myback(View view) {
        finish();
    }
}
