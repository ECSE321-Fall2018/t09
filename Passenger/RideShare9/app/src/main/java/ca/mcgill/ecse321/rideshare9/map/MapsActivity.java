package ca.mcgill.ecse321.rideshare9.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.Locale;
import ca.mcgill.ecse321.rideshare9.R;
import cz.msebera.android.httpclient.Header;
import static com.loopj.android.http.AsyncHttpClient.log;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private LocationManager mLocationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init the map
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        int permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        log.d("permission", permissionCheck + "");
    }
    //This method is called upon the user choose whether to grant permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        {
            switch (requestCode) {
                case PERMISSION_REQUEST_CODE: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        log.d("requestcod", requestCode + "");
                        log.d("ifgranted", PackageManager.PERMISSION_GRANTED + "");
                        log.d("status", ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) + "");
                        gMap.setMyLocationEnabled(true);
                        gMap.getUiSettings().setMyLocationButtonEnabled(true);
                        gMap.getUiSettings().setAllGesturesEnabled(true);
                        gMap.getUiSettings().setZoomControlsEnabled(true);
                        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                            @Override
                            public boolean onMyLocationButtonClick() {
                                getCurrentLocation();
                                return true;
                            }
                        });
                        // permission was granted, yay! Do the
                        // maps-related task need to do.
                    } else {
                        log.d("requestcod", requestCode + "");
                        log.d("ifdenied", PackageManager.PERMISSION_DENIED + "");
                        log.d("status", ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) + "");
                        this.finish();
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }
            }
        }
    }
    //Rewrite the LocationListener in order to make getCurrentLocation work
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                if (location != null) {
                    log.d(location.getLatitude() + "", location.getLongitude() + "");
                    drawMarker(location);
                    mLocationManager.removeUpdates(mLocationListener);
                } else {
                    log.d("Error", "Location is null");
                }
            } else {
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    //Directly call the geocoder to return LatLng, not work on many android phones,
    //So will not be use for this sprint.
    public  LatLng getLatLongFromGivenAddress(String address)
    {
        double lat= 0.0, lng= 0.0;
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder.getFromLocationName(address , 1);
            if (addresses.size() > 0)
            {
                lat = addresses.get(0).getLatitude();
                lng = addresses.get(0).getLongitude();
                Log.d("Latitude", ""+addresses.get(0).getLatitude());
                Log.d("Longitude", ""+addresses.get(0).getLongitude());
            }
            else{
                Log.d("where are you","hello");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        LatLng latlng = new LatLng(lat,lng);
        return latlng;
    }
    public void getLocationByGoogleApi(String locationlist[]){
        for(int i = 0; i<locationlist.length;i++) {
            if(locationlist[i]!=null) {
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.get("https://maps.googleapis.com/maps/api/geocode/json?address=" +
                                locationlist[i].replaceAll(" ", "+") + "&key=AIzaSyCtROZ_vTXe8C6S01_5VILbwOOTF-kpS10",
                        new RequestParams(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                log.d("good", "success");
                                LatLng latLng = new LatLng(0, 0);
                                String locationname = "";
                                try {
                                    latLng = new LatLng(Double.parseDouble(response.getJSONArray("results").getJSONObject(0).
                                            getJSONObject("geometry").getJSONObject("location").getString("lat")), Double.parseDouble(response.getJSONArray("results").getJSONObject(0).
                                            getJSONObject("geometry").getJSONObject("location").getString("lng")));
                                    log.d("confirm", "markerPlaced");
                                    locationname = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                                } catch (JSONException e) {
                                }
                                gMap.addMarker(new MarkerOptions().position(latLng).title(locationname));
                                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                log.d("failure", "error");
                                LatLng latLng = new LatLng(0, 0);
                                gMap.addMarker(new MarkerOptions().position(latLng).title("location not found"));
                            }
                        });
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        // Add a marker in each stop;
        String locationlist[] = getIntent().getStringArrayExtra("locationlist");
        getLocationByGoogleApi(locationlist);
        //AskUser for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //get user's current location and enable map widgets
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setAllGesturesEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        //mycurrent location button listener
        gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                getCurrentLocation();
                return true;
            }
        });
    }
    //Locate the camera to user's current location
    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            Snackbar.make(findViewById(R.id.map), "error", Snackbar.LENGTH_INDEFINITE).show();
        else {
            if (isNetworkEnabled) {
                //Permission check
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(
                            this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME, MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME, MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        //draw a marker on the location if found
        if (location != null) {
            drawMarker(location);
        }
    }
    //drawMarker for current location
    private void drawMarker(Location location) {
        if ( gMap!= null) {
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            gMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .title("Current Position"));
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12));
        }
    }
}
/**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */