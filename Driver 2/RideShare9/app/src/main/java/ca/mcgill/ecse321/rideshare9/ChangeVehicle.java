package ca.mcgill.ecse321.rideshare9;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class ChangeVehicle extends AppCompatActivity {
    String error;
    Long thisVehicleId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_vehicle);

        Bundle b = getIntent().getExtras();
        Long valueVid = -1L; // or other values
        if(b != null)
            valueVid = b.getLong("key");
        final long targetId = valueVid.longValue();
        Log.d("valueid", valueVid.toString());
        Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(getApplicationContext()))};

        HttpUtils.get(getApplicationContext(), "vehicle/get-cars", headers, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    Long id;
                    String model = "";
                    String licence = "";
                    String color = "";
                    Integer maxSeat = 0;
                    try{
                        id = Long.parseLong(response.getJSONObject(i).getString("id"));
                        model = response.getJSONObject(i).getString("model");
                        licence = response.getJSONObject(i).getString("licencePlate");
                        color = response.getJSONObject(i).getString("color");
                        maxSeat = Integer.parseInt(response.getJSONObject(i).getString("maxSeat"));
                        VehicleItem vitem = new VehicleItem(id, model);
                        vitem.setModel(model);
                        vitem.setLicencePlate(licence);
                        vitem.setColor(color);
                        vitem.setMaxSeat(maxSeat);
                        Log.d("catched vehicle", vitem.getModel());
                        if (targetId == vitem.getId()) {
                            TextView modelTx = findViewById(R.id.change_vehicle_model);
                            TextView licenceTx = findViewById(R.id.change_vehicle_licence);
                            Spinner colorTx = findViewById(R.id.change_vehicle_color);
                            Spinner maxSeatTx = findViewById(R.id.change_vehicle_number_of_seats);
                            modelTx.setText(vitem.getModel());
                            licenceTx.setText(vitem.getLicencePlate());
                            ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.colors, android.R.layout.simple_spinner_item);
                            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            colorTx.setAdapter(colorAdapter);
                            if (vitem.getColor() != null) {
                                int spinnerPosition = colorAdapter.getPosition(vitem.getColor());
                                colorTx.setSelection(spinnerPosition);
                            }
                            ArrayAdapter<CharSequence> maxSeatAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.number_seats, android.R.layout.simple_spinner_item);
                            maxSeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            maxSeatTx.setAdapter(maxSeatAdapter);
                            if (vitem.getMaxSeat() != null) {
                                int spinnerPosition = maxSeatAdapter.getPosition(vitem.getMaxSeat().toString());
                                maxSeatTx.setSelection(spinnerPosition);
                            }
                            thisVehicleId = targetId;
                        }
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

    }
    public void changeVehicle(View v) throws Exception {
        error = "";
        final TextView errortx = (TextView) findViewById(R.id.change_vehicle_error);
        final TextView model = (TextView) findViewById(R.id.change_vehicle_model);
        final TextView licencePlate = (TextView) findViewById(R.id.change_vehicle_licence);
        final Spinner color = (Spinner)findViewById(R.id.change_vehicle_color);
        String colorTx = color.getSelectedItem().toString();
        if (colorTx.equals("VEHICLE COLOR")) {
            error = "Please select a valid color";
            errortx.setText(error);
            return;
        }
        final Spinner maxSeat = (Spinner)findViewById(R.id.change_vehicle_number_of_seats);
        if (maxSeat.getSelectedItem().toString().equals("MAXIMUM SEATS")) {
            error = "Please select a valid number";
            errortx.setText(error);
            return;
        }
        Integer maxSeatTx = Integer.valueOf(maxSeat.getSelectedItem().toString());
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("model",model.getText());
            jsonObject.put("licencePlate",licencePlate.getText());
            jsonObject.put("maxSeat", maxSeatTx);
            jsonObject.put("color", colorTx);
            jsonObject.put("id", thisVehicleId);
            Log.d("vehid", thisVehicleId.toString());
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

        /* Validate that all field is filled */
        if(model.getText().toString().trim().equals("") || licencePlate.getText().toString().trim().equals("")
                || colorTx.trim().equals("") || maxSeatTx > 10 || maxSeatTx < 1){
            errortx.setText("One of the field is not valid");
            return;
        }
        Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(getApplicationContext()))};
        HttpUtils.put(getApplicationContext(),"/vehicle/change-cars", headers, entity,"application/json",new JsonHttpResponseHandler(){
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
                errortx.setText("Unable to create account.");
            }
        });
    }

}
