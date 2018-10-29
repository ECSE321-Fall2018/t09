package ca.mcgill.ecse321.rideshare9.user;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.HttpUtils;
import cz.msebera.android.httpclient.Header;

public class Stop {
    private long id;
    private String name;
    private float price;

    public Stop() {

    }

    public Stop(long id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "{id: " + getId() + ", name: " + getName() + ", price: " + getPrice() + "}";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public static Stop stopFromJSONObject(JSONObject jsonStopObject) {
        long stopId = jsonStopObject.optLong("id");
        String stopName = jsonStopObject.optString("stopName");
        float stopPrice = (float) jsonStopObject.optDouble("price");

        return new Stop(stopId, stopName, stopPrice);
    }
}
