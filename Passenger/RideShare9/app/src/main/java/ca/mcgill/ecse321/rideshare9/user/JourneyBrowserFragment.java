package ca.mcgill.ecse321.rideshare9.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.HttpUtils;
import ca.mcgill.ecse321.rideshare9.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static com.loopj.android.http.AsyncHttpClient.log;


public class JourneyBrowserFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private final static List<Journey> JOURNEYS = new ArrayList<>();
    private RecyclerView rvAdvertisements;
    private JourneyAdapter journeyAdapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    public JourneyBrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getAvailableTrips() {
        // show refresh animation
        swipeRefreshLayout.setRefreshing(true);

        //  Get SharedPreferences which holds the JWT Token
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        String authentication = "Bearer " + sharedPreferences.getString("token", null);

        //  Set headers for the request
        HttpUtils.addHeader("Authorization", authentication);

        HttpUtils.get("adv/get-list-adv", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                JOURNEYS.addAll(advertisementsFromJSONArray(response));

                for (int i = 0; i < JOURNEYS.size(); i++) {
                    for (int j = 0; j < JOURNEYS.get(i).getStops().size(); j++) {
                        final int finalI = i;
                        final int finalJ = j;
                        HttpUtils.get("/stop/get-by-id/" + JOURNEYS.get(i).getStops().get(j).getId(),
                                null, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        JOURNEYS.get(finalI).getStops().get(finalJ)
                                                .setName(response.optString("stopName"));
                                        JOURNEYS.get(finalI).getStops().get(finalJ)
                                                .setPrice((float) response.optDouble("price"));
                                        journeyAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Could not get journeys", Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<Journey> advertisementsFromJSONArray(JSONArray jsonAdArray) {
        int adCount = jsonAdArray.length();
        List<Journey> journeys = new ArrayList<>();

        for (int i = 0; i < adCount; i++) {
            JSONObject advertisement = jsonAdArray.optJSONObject(i);
            journeys.add(advertisementFromJSONObject(advertisement));
        }

        // remove journeys that don't have status "REGISTERING"
        List<Journey> journeysToRemove = new ArrayList<>();
        for (Journey journey : journeys) {
            if (!journey.getStatus().equals("REGISTERING")) {
                journeysToRemove.add(journey);
            }
        }
        journeys.removeAll(journeysToRemove);

        return journeys;
    }

    public ByteArrayEntity json2Entity(JSONObject jsonObject){
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return entity;
    }



    private Journey advertisementFromJSONObject(JSONObject jsonAdObject) {
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

        return new Journey(adId, adSeatsAvailable, adVehicleId, adDriverId, adTitle,
                adStartTime, adStartLocation, adStatus, adStops);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_journey_browser, container, false);
        rvAdvertisements = view.findViewById(R.id.rvAdvertisements);
        final ImageView searchIcon = (ImageView)view.findViewById(R.id.searchIcon);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvAdvertisements.addItemDecoration(
                new DividerItemDecoration(
                        rvAdvertisements.getContext(), layoutManager.getOrientation()
                )
        );

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForStop(view);
            }
        });
        rvAdvertisements.setLayoutManager(layoutManager);
        journeyAdapter = new JourneyAdapter(JOURNEYS);
        rvAdvertisements.setAdapter(journeyAdapter);
        journeyAdapter.settoken(getArguments().getString("token"));

        // SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.journey_browser_swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        getAvailableTrips();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh() {
        RecyclerView rv = getView().findViewById(R.id.rvAdvertisements);
        rv.setAdapter(journeyAdapter);
        JOURNEYS.clear();
        getAvailableTrips();
    }

    public void searchForStop(final View view){
        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.rvAdvertisements);
        TextView searchText = (TextView)view.findViewById(R.id.searchText);
        JSONObject jsonObject = new JSONObject();
        final RadioButton sortbyprice= (RadioButton)view.findViewById(R.id.sortByPrice);

        try {
            jsonObject.put("stop",searchText.getText());
            jsonObject.put("startLocation","");
            jsonObject.put("startTimeX","1000-11-11 11:11:11");
            jsonObject.put("startTimeY","3000-11-11 11:11:11");

            if(sortbyprice.isChecked()){
                jsonObject.put("sortByPrice","true");
            }
            else{
                jsonObject.put("sortByPrice","false");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ByteArrayEntity entity = json2Entity(jsonObject);

        HttpUtils.addHeader("Authorization","Bearer "+getArguments().getString("token"));
        HttpUtils.post(getContext(),"adv/get-adv-search",entity,"application/json",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                final List<Journey>journeyList = new ArrayList<Journey>(response.length());
                final SyncHttpClient syncHttpClient = new SyncHttpClient();
                List<Integer>arr = new ArrayList<>(response.length());
                for(int iter = 0;iter<response.length();iter++){
                    try {
                        if(!arr.contains(response.getJSONObject(iter).getJSONObject("adv").getInt("id"))){
                            arr.add(response.getJSONObject(iter).getJSONObject("adv").getInt("id"));
                            journeyList.add(advertisementFromJSONObject(response.getJSONObject(iter).getJSONObject("adv")));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for(int iter =0; iter<journeyList.size();iter++){
                    final int iter4 = iter;
                    for(int iter2 = 0; iter2<journeyList.get(iter).getStops().size();iter2++){
                        final int iter3 = iter2;
                        Header[] headers1 = {new BasicHeader("Authorization","Bearer "+getArguments().getString("token"))};
                        syncHttpClient.get(getContext(),
                                "https://mysterious-hollows-14613.herokuapp.com/stop/get-by-id/"+journeyList.get(iter).
                                        getStops().get(iter2).getId(),headers1,new RequestParams(),new JsonHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        try {
                                            float temp = (float)response.getDouble("price");
                                            journeyList.get(iter4).getStops().get(iter3).setName(response.getString("stopName"));
                                            journeyList.get(iter4).getStops().get(iter3).setPrice(temp);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                            log.d("Failure","NOGOod");
                                    }
                                });

                    }
                }
                JourneyAdapter jAd = new JourneyAdapter(journeyList);
                jAd.settoken(getArguments().getString("token"));
                recyclerView.setAdapter(jAd);
                //TODO
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                log.d("error","wrong");
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}
