package ca.mcgill.ecse321.rideshare9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.mcgill.ecse321.rideshare9.model.Advertisement;
import ca.mcgill.ecse321.rideshare9.model.Stop;
import ca.mcgill.ecse321.rideshare9.model.Vehicle;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AdvertisementFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private final static List<Advertisement> advertisements = new ArrayList<>();
    private RecyclerView rvAdvertisements;
    private AdvertisementViewAdapter advertisementsAdapter;
    private LinearLayoutManager layoutManager;


    private AdvertisementViewAdapter viewAdapter;


    public AdvertisementFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate AdFragment", "created Ad Fragment");
        super.onCreate(savedInstanceState);

    }

    private void getTripList() {
        //  Get SharedPreferences which holds the JWT Token
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        String authentication = "Bearer " + sharedPreferences.getString("token", null);

        //  Set headers for the request
        HttpUtils.addHeader("Authorization", authentication);

        HttpUtils.get("adv/get-logged-adv", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                advertisements.addAll(advertisementsFromJSONArray(response));

                for (int i = 0; i < advertisements.size(); i++) {
                    final int vehicleI =i;
                    HttpUtils.get("/vehicle/get-by-id/" + advertisements.get(i).getVehicle().getId(),
                            null, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            advertisements.get(vehicleI).getVehicle()
                                    .setColor(response.optString("color"));
                            advertisements.get(vehicleI).getVehicle()
                                    .setLicencePlate(response.optString("licencePlate"));
                            advertisements.get(vehicleI).getVehicle()
                                    .setMaxSeat(response.optInt("maxSeat"));
                            advertisements.get(vehicleI).getVehicle()
                                    .setModel(response.optString("model"));
                            advertisementsAdapter.notifyDataSetChanged();
                        }

                    });
                    for (int j = 0; j < advertisements.get(i).getStops().size(); j++) {
                        final int finalI = i;
                        final int finalJ = j;
                        HttpUtils.get("/stop/get-by-id/" + advertisements.get(i).getStops().get(j).getId(),
                                null, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        advertisements.get(finalI).getStops().get(finalJ)
                                                .setName(response.optString("stopName"));
                                        advertisements.get(finalI).getStops().get(finalJ)
                                                .setPrice((float) response.optDouble("price"));
                                        advertisementsAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                }
            }
        });




    }

    private List<Advertisement> advertisementsFromJSONArray(JSONArray jsonAdArray) {
        int adCount = jsonAdArray.length();
        List<Advertisement> advertisements = new ArrayList<>();

        for (int i = 0; i < adCount; i++) {
            JSONObject advertisement = jsonAdArray.optJSONObject(i);
            advertisements.add(advertisementFromJSONObject(advertisement));
        }

        return advertisements;
    }

    private Advertisement advertisementFromJSONObject(JSONObject jsonAdObject) {
        int adId = jsonAdObject.optInt("id");
        int adSeatsAvailable = jsonAdObject.optInt("seatAvailable");
        int adVehicleId = jsonAdObject.optInt("vehicle");
        //set vehicle with just id for now
        Vehicle vehicle = new Vehicle();
        vehicle.setId(adVehicleId);
        int adDriverId = jsonAdObject.optInt("driver");
        String adTitle = jsonAdObject.optString("title");
        String adStartTime = jsonAdObject.optString("startTime");
        String adStartLocation = jsonAdObject.optString("startLocation");
        String adStatus = jsonAdObject.optString("status");
        List<Stop> adStops = new ArrayList<>();

        JSONArray stops = jsonAdObject.optJSONArray("stops");

        //  Get the number of stops for the advertisement
        int stopCount = stops.length();

        for (int j = 0; j < stopCount; j++) {
            Stop newStop = new Stop();
            // Only the id is set for now
            newStop.setId(stops.optLong(j));
            adStops.add(newStop);
        }

        return new Advertisement(adId, adSeatsAvailable, vehicle, adDriverId, adTitle,
                adStartTime, adStartLocation, adStatus, adStops);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advertisement, container, false);
        rvAdvertisements = view.findViewById(R.id.trip_recylcer);
        rvAdvertisements.setLayoutManager(new LinearLayoutManager(getContext()));
        advertisementsAdapter = new AdvertisementViewAdapter(advertisements);
        rvAdvertisements.setAdapter(advertisementsAdapter);

        getTripList();

        // Inflate the layout for this fragment
        return view;
    }

    public void toAddTrip(View view) {
        // Do something in response to button
        Intent intent = new Intent(this.getActivity(), addJourneyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }







}
