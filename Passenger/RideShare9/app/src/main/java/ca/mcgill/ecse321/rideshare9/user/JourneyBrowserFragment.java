package ca.mcgill.ecse321.rideshare9.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ca.mcgill.ecse321.rideshare9.HttpUtils;
import ca.mcgill.ecse321.rideshare9.R;
import cz.msebera.android.httpclient.Header;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JourneyBrowserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JourneyBrowserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JourneyBrowserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final static List<Advertisement> advertisements = new ArrayList<>();


    private OnFragmentInteractionListener mListener;

    public JourneyBrowserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JourneyBrowserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JourneyBrowserFragment newInstance(String param1, String param2) {
        JourneyBrowserFragment fragment = new JourneyBrowserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getAvailableTrips();

        Log.i(TAG, "What do I get? " + advertisements.size() + " ads...");
    }

    private void getAvailableTrips() {

        //  Get SharedPreferences which holds the JWT Token
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        String authentication = "Bearer " + sharedPreferences.getString("token", null);

        //  Set headers for the request
        HttpUtils.addHeader("Authorization", authentication);

        HttpUtils.get("adv/get-list-adv", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("get-list-adv", "onSuccess: Success");
                Log.i(TAG, "what are you returning? " + advertisementsFromJSONArray(response).get(0).getTitle());
                advertisements.addAll(advertisementsFromJSONArray(response));

                Log.i(TAG, "What: It's size: " + advertisements.size());
                for (int i = 0; i < advertisements.size(); i++) {
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
                                        Log.i("What, added stop: ", advertisements.get(finalI).getStops().get(finalJ).toString());
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

        return new Advertisement(adId, adSeatsAvailable, adVehicleId, adDriverId, adTitle, adStartTime, adStartLocation, adStatus, adStops);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "What do I see later? " + this.advertisements.size() + "ads");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journey_browser, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
