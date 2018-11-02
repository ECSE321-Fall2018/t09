package ca.mcgill.ecse321.rideshare9.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcgill.ecse321.rideshare9.HttpUtils;
import ca.mcgill.ecse321.rideshare9.R;
import ca.mcgill.ecse321.rideshare9.map.MapsActivity;
import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class JourneyViewerActivity extends AppCompatActivity {
    // journey sent in a bundle to the activity when it starts
    private Journey journey;
    // stops recyclerView
    private RecyclerView rvStops;
    private StopsAdapter adapter;
    private LinearLayoutManager layoutManager;
    // Views
    private TextView adTitle;
    private TextView adStartLocation;
    private TextView adStartDateAndTime;
    private Button adJoinButton;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_viewer);

        final TextView carModel = (TextView)findViewById(R.id.driversInfo);
        final TextView carColor = (TextView)findViewById(R.id.carColor);
        final TextView statusTex = (TextView)findViewById(R.id.advStatus);
        final TextView availableSeats = (TextView)findViewById(R.id.seatsTex);

        journey = getIntent().getParcelableExtra("advertisement_data");
        log.d("checkDriver",journey.getVehicleId()+"");
        rvStops = findViewById(R.id.rvStopsForJourney);
        layoutManager = new LinearLayoutManager(this);
        rvStops.setLayoutManager(layoutManager);
        adapter = new StopsAdapter(journey.getStops());
        rvStops.setAdapter(adapter);

        // get the TextViews for advertisement_viewer
        adTitle = findViewById(R.id.advertisement_viewer_title);
        adStartLocation = findViewById(R.id.advertisement_viewer_startsAtLocation);
        adStartDateAndTime = findViewById(R.id.advertisement_viewer_startsAtDate);

        // set the view text for advertisement_viewer
        adTitle.setText(journey.getTitle());
        adStartLocation.setText(journey.getStartLocation());
        adStartDateAndTime.setText(journey.getStartTime());
        statusTex.setText(journey.getStatus());
        availableSeats.setText(journey.getAvailableSeats()+"");

        // get the Join Trip button
        adJoinButton = findViewById(R.id.advertisement_viewer_join_button);

        HttpUtils.addHeader("Authorization","Bearer "+getIntent().getStringExtra("token"));
        HttpUtils.get("vehicle/get-by-id/"+journey.getVehicleId(),new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    carModel.setText("Vehicle model: "+response.getString("model"));
                    carColor.setText("Vehicle color: "+response.getString("color"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                carModel.setText("Vehicle model: "+"Unknown");
                carColor.setText("Vehicle color: "+"Unknown");
            }
        });
        // set onClickListener
        adJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "works", Toast.LENGTH_SHORT).show();
                HttpUtils.post("/map/add-map?adv_id=" + journey.getId(), null, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(context, "Error joining trip", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        if (responseString.equals("Success")) {
                            Toast.makeText(context, "Enjoy your journey!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Last seat gone! :(", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                });
            }
        });

    }
    public void startMap(View view){
        StopsAdapter stopA = (StopsAdapter)rvStops.getAdapter();
        String locationlist[] = new String[stopA.getstopList().size()];
        for(int i = 0; i<locationlist.length;i++){
            if(stopA.getstopList().get(i)!=null) {
                locationlist[i] = stopA.getstopList().get(i).getName();
            }
        }
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("locationlist",locationlist);
        startActivity(intent);
    }
}
