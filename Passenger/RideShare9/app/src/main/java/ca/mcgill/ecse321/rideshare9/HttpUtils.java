package ca.mcgill.ecse321.rideshare9;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.user.Advertisement;
import ca.mcgill.ecse321.rideshare9.user.Stop;
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

    public static List<Advertisement> AdvertisementsFromJSONArray(JSONArray jsonAdArray) {
        int adCount = jsonAdArray.length();
        List<Advertisement> advertisements = new ArrayList<>();

        for (int i = 0; i < adCount; i++) {
            JSONObject advertisement = jsonAdArray.optJSONObject(i);
            advertisements.add(advertisementFromJSONObject(advertisement));
        }

        return advertisements;
    }

    private static Advertisement advertisementFromJSONObject(JSONObject jsonAdObject) {
        int adId = jsonAdObject.optInt("id");
        int adSeatsAvailable = jsonAdObject.optInt("seatAvailable");
        int adVehicleId = jsonAdObject.optInt("vehicle");
        int adDriverId = jsonAdObject.optInt("driver");
        String adTitle = jsonAdObject.optString("title");
        String adStartTime = jsonAdObject.optString("startTime");
        String adStartLocation = jsonAdObject.optString("startLocation");
        String adStatus = jsonAdObject.optString("status");

        JSONArray stops = jsonAdObject.optJSONArray("stops");
        List<Long> adStopIds = new ArrayList<>();

        //TODO Get stops and create Stop objects
        int stopCount = stops.length();

        for (int j = 0; j < stopCount; j++) {
            adStopIds.add(stops.optLong(j));
        }

        Advertisement newAdvertisement = new Advertisement(adId, adSeatsAvailable, adVehicleId,
                adDriverId, adTitle, adStartTime, adStartLocation, adStatus, adStopIds);
    }
}
