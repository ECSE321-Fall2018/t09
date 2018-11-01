package ca.mcgill.ecse321.rideshare9;



import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.mcgill.ecse321.rideshare9.model.Advertisement;
import com.loopj.android.http.AsyncHttpResponseHandler;

import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

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

    private Button adModifyButton;

    private Context context = this;


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
        adapter = new ChangeAdStopsAdapter(advertisement.getStops());
        rvStops.setAdapter(adapter);


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