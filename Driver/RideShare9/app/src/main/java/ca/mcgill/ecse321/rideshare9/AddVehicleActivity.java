package ca.mcgill.ecse321.rideshare9;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AddVehicleActivity extends AppCompatActivity {
    private String error = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void addNewVehicle(View v) throws Exception {
        error = "";
        final TextView errortx = (TextView) findViewById(R.id.vehicle_error);
        final TextView model = (TextView) findViewById(R.id.vehicle_model);
        final TextView licencePlate = (TextView) findViewById(R.id.vehicle_licence);
        final Spinner color = (Spinner)findViewById(R.id.vehicle_color);
        String colorTx = color.getSelectedItem().toString();
        if (colorTx.equals("VEHICLE COLOR")) {
            error = "Please select a valid color";
            errortx.setText(error);
            return;
        }
        final Spinner maxSeat = (Spinner)findViewById(R.id.vehicle_number_of_seats);
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
        HttpUtils.post(getApplicationContext(),"/vehicle/add-car", headers, entity,"application/json",new JsonHttpResponseHandler(){
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String uid = response.getString("id");
                    errortx.setText("Vehicle created! with id = " + uid + " REMEMBER IT");
                } catch (Exception ue) {
                    errortx.setText("Unable to create account.");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                errortx.setText("Unable to create account.");
            }
        });
    }
}
