package ca.mcgill.ecse321.rideshare9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static ca.mcgill.ecse321.rideshare9.FullscreenActivity.getsavedToken;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home , container, false);
        TextView unameTx = view.findViewById(R.id.unameTXV);
        unameTx.setText(FullscreenActivity.getsavedUname(view.getContext()));
        final Switch status = view.findViewById(R.id.state_switch);
        final TextView statusTx = view.findViewById(R.id.status_text);
        final TextView countTx = view.findViewById(R.id.countRide);
        final TextView countTripTx = view.findViewById(R.id.count_vehicle_text);
        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Header[] headers = {new BasicHeader("Authorization","Bearer "+getsavedToken(getContext()))};

                if (isChecked) {
                    JSONObject jsonObject = new JSONObject();
                    try{
                        jsonObject.put("status","ON_RIDE");
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

                    HttpUtils.put(getContext(), "user/update-status", headers, entity, "application/json", new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            statusTx.setText("On ride");
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("status", "cannot update driver status");
                        }
                    });
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try{
                        jsonObject.put("status","STANDBY");
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
                    HttpUtils.put(getContext(), "user/update-status", headers, entity, "application/json", new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            statusTx.setText("Standby");
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("status", "cannot update driver status");
                        }
                    });
                }
            }
        });
        Button refresh_home = view.findViewById(R.id.home_refresh);
        refresh_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                Header[] headers = {new BasicHeader("Authorization","Bearer "+getsavedToken(v.getContext()))};
                HttpUtils.get(v.getContext(), "/adv/get-logged-adv-count", headers, new RequestParams(), new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        countTx.setText("0");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        countTx.setText(responseString);
                    }
                });
                HttpUtils.get(v.getContext(), "/vehicle/get-cars-count", headers, new RequestParams(), new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        countTripTx.setText("0");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        countTripTx.setText(responseString);
                    }
                });

                HttpUtils.get(v.getContext(), "/user/get-logged-user", headers, new RequestParams(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            String ustatus = response.getString("status");
                            if (ustatus.equals("ON_RIDE")) {
                                status.setChecked(true);
                                statusTx.setText("On ride");
                            } else {
                                status.setChecked(false);
                                statusTx.setText("Standby");
                            }
                        } catch (Exception ue) {
                            ue.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);

                    }
                });

            }
        });
        refresh_home.callOnClick();
        return view;
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
