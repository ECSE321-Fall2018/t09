package ca.mcgill.ecse321.rideshare9;

import android.content.Context;
import android.support.annotation.Nullable;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.user.Advertisement;
import cz.msebera.android.httpclient.HttpEntity;

public class HttpUtils {
    public static final String DEFAULT_BASE_URL = "https://mysterious-hollows-14613.herokuapp.com/";

    private static String baseUrl;
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        baseUrl = DEFAULT_BASE_URL;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        HttpUtils.baseUrl = baseUrl;
    }

    public static void addHeader(String header, String value) {
        client.addHeader(header, value);
    }

    public static void removeHeader(String header) {
        client.removeHeader(header);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, HttpEntity entity, String contenttype,
                            AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAbsoluteUrl(url),entity, contenttype, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }

    @Nullable
    public static List<Advertisement> AdvertisementsFromJSONArray(JSONArray response) {
        int adCount = response.length();
        List<Advertisement> advertisements = new ArrayList<Advertisement>();

        for (int i = 0; i < adCount; i++) {
            JSONObject advertisement = response.optJSONObject(i);
            JSONArray stops = advertisement.optJSONArray("stops");

            int adId = advertisement.optInt("id");
            int adSeatsAvailable = advertisement.optInt("seatAvailable");
            int adVehicleId = advertisement.optInt("vehicle");
            int adDriverId = advertisement.optInt("driver");
            String adTitle = advertisement.optString("title");
            String adStartTime = advertisement.optString("startTime");
            String adStartLocation = advertisement.optString("startLocation");
            String adStatus = advertisement.optString("status");
            List<Long> adStops = new ArrayList<Long>();

            int stopCount = stops.length();

            for (int j = 0; j < stopCount; j++) {
                adStops.add(stops.optLong(j));
            }

            Advertisement newAdvertisement = new Advertisement(adId, adSeatsAvailable, adVehicleId,
                    adDriverId, adTitle, adStartTime, adStartLocation, adStatus, adStops);
            advertisements.add(newAdvertisement);
        }

        return advertisements;
    }
}
