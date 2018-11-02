package ca.mcgill.ecse321.rideshare9;



import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;

import ca.mcgill.ecse321.rideshare9.model.Advertisement;
import ca.mcgill.ecse321.rideshare9.model.Stop;
import com.loopj.android.http.AsyncHttpResponseHandler;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ChangeAdvertisementActivity extends AppCompatActivity {
    // advertisement sent in a bundle to the activity when it starts
    private Advertisement advertisement;
    // stops recyclerView
    private RecyclerView rvStops;
    private ChangeAdStopsAdapter adapter;
    private LinearLayoutManager layoutManager;
    // Views
    private EditText finalDestinationField;
    private EditText startDestinationField;
    private EditText seatingField;

    List<VehicleItem> list;
    private ListView listView;

    private Button adModifyButton;


    private Context context = this;

    private Bundle getTimeFromLabel(String text) {
        Bundle rt = new Bundle();
        String comps[] = text.toString().split(":");
        int hour = 12;
        int minute = 0;

        if (comps.length == 2) {
            hour = Integer.parseInt(comps[0]);
            minute = Integer.parseInt(comps[1]);
        }

        rt.putInt("hour", hour);
        rt.putInt("minute", minute);

        return rt;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_advertisement);

        advertisement = getIntent().getParcelableExtra("advertisement_data");

        finalDestinationField = findViewById(R.id.advertisement_title_textfield);
        startDestinationField = findViewById(R.id.advertisement_start_textfield);
        seatingField = findViewById(R.id.advertisement_seating_textfield);

        rvStops = findViewById(R.id.rvStopsForJourney);
        layoutManager = new LinearLayoutManager(this);
        rvStops.setLayoutManager(layoutManager);
        adapter = new ChangeAdStopsAdapter((ArrayList<Stop>) advertisement.getStops());
        rvStops.setAdapter(adapter);
        listView = findViewById(R.id.listview_vehicle);



        list=new ArrayList<VehicleItem>();

        Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(this))};

        HttpUtils.get(this, "vehicle/get-cars", headers, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        VehicleItem vitem = new VehicleItem(Long.parseLong(response.getJSONObject(i).getString("id")), response.getJSONObject(i).getString("model"));
                        vitem.setLicencePlate(response.getJSONObject(i).getString("licencePlate"));
                        vitem.setColor(response.getJSONObject(i).getString("color"));
                        vitem.setMaxSeat(Integer.parseInt(response.getJSONObject(i).getString("maxSeat")));
                        Log.d("catched vehicle", vitem.getModel());
                        list.add(vitem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("vehicle", "cannot get vehicle");
            }
        });
        listView.setAdapter(new VehicleItemAdapter(this, list));

        // get the Join Trip button
        adModifyButton = findViewById(R.id.advertisement_confirm_modify_button);

        // set onClickListener
        adModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HttpUtils.addHeader("Authorization", "Bearer " +  FullscreenActivity.getsavedToken(context));
                HttpUtils.addHeader("Content-Type", "application/json");
                RequestParams params = new RequestParams();
                params.put("id",advertisement.getId());
                params.put("title",finalDestinationField.getText());
                params.put("startLocation",startDestinationField.getText());
                params.put("seatAvailable", seatingField.getText());

                //TODO: modify non-simple values
                params.put("startTime",(String)null);
                params.put("stops",(String)null);
                params.put("vehicle",0);

                HttpUtils.put(context,"/adv/update-adv/", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(context, "Advertisement Modified.", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("JoinJourney", "Could not modify advertisement.");
                    }
                });
            }
        });
    }
}