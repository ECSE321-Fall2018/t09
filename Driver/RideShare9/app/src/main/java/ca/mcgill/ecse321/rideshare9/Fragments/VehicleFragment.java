package ca.mcgill.ecse321.rideshare9.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.AddVehicleActivity;
import ca.mcgill.ecse321.rideshare9.FullscreenActivity;
import ca.mcgill.ecse321.rideshare9.HttpUtils;
import ca.mcgill.ecse321.rideshare9.R;
import ca.mcgill.ecse321.rideshare9.VehicleItem;
import ca.mcgill.ecse321.rideshare9.Adapter.VehicleItemAdapter;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VehicleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehicleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<VehicleItem> list;
    private ListView listView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VehicleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VehicleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehicleFragment newInstance(String param1, String param2) {
        VehicleFragment fragment = new VehicleFragment();
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

        View view= inflater.inflate(R.layout.fragment_vehicle , container, false);
        listView = (ListView)view.findViewById(R.id.listview_vehicle);
        Button addVehicleBtn = view.findViewById(R.id.add_vehicle);
        addVehicleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Intent intent = new Intent(getContext(), AddVehicleActivity.class);
                startActivity(intent);
            }
        });
        final Button refresh = view.findViewById(R.id.refresh_vehicle);
        refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list=new ArrayList<VehicleItem>();

                Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(getActivity()))};

                HttpUtils.get(getContext(), "vehicle/get-cars", headers, new RequestParams(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try{
                                VehicleItem vitem = new VehicleItem(Long.parseLong(response.getJSONObject(i).getString("id")), response.getJSONObject(i).getString("model"));
                                vitem.setLicencePlate(response.getJSONObject(i).getString("licencePlate"));
                                vitem.setColor(response.getJSONObject(i).getString("color"));
                                vitem.setMaxSeat(Integer.parseInt(response.getJSONObject(i).getString("maxSeat")));
                                Log.d("catched vehicle", vitem.getModel());
                                list.add(vitem);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        listView.setAdapter(new VehicleItemAdapter(getContext(), list));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.d("vehicle", "cannot get vehicle");
                    }
                });
            }
        });
        refresh.callOnClick();
        refresh.setVisibility(View.INVISIBLE);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.swiperefresh_vehicle);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.callOnClick();
                pullToRefresh.setRefreshing(false);
            }
        });
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
