package ca.mcgill.ecse321.rideshare9.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;

import ca.mcgill.ecse321.rideshare9.ChangeAdvertisementActivity;
import ca.mcgill.ecse321.rideshare9.FullscreenActivity;
import ca.mcgill.ecse321.rideshare9.HttpUtils;
import ca.mcgill.ecse321.rideshare9.R;
import ca.mcgill.ecse321.rideshare9.model.Advertisement;
import ca.mcgill.ecse321.rideshare9.model.Stop;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class AdvertisementViewAdapter extends RecyclerView.Adapter<AdvertisementViewAdapter.ViewHolder> {
    private RecyclerView recyclerView;
    private List<Advertisement> advertisements;
    private Context context;


    public AdvertisementViewAdapter(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView adTitle;
        public TextView adStartLocation;
        public TextView adStartDate;
        public TextView adStartTime;
        public TextView adStops;
        public TextView carCapacity;
        public TextView adVehicle;
        public Button modifyButton;
        public Button deleteButton;
        public TextView adStatus;
        public Button completeButton;
        public Button startButton;

        public ViewHolder(View itemView) {
            super(itemView);

            adTitle = itemView.findViewById(R.id.final_destination);
            adStartLocation = itemView.findViewById(R.id.start_destination);
            adStartDate = itemView.findViewById(R.id.date);
            adStartTime = itemView.findViewById(R.id.time);
            adStops = itemView.findViewById(R.id.stop_list);
            carCapacity = itemView.findViewById(R.id.car_capacity);
            adVehicle = itemView.findViewById(R.id.vehicleField);
            modifyButton = itemView.findViewById(R.id.modifyAdButton);
            deleteButton = itemView.findViewById(R.id.deleteAdButton);
            adStatus = itemView.findViewById(R.id.trip_status);
            completeButton = itemView.findViewById(R.id.complete_button);
            startButton = itemView.findViewById(R.id.start_button);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public AdvertisementViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View advertisementView = inflater.inflate(R.layout.advertisement_item, viewGroup, false);
        return new AdvertisementViewAdapter.ViewHolder(advertisementView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdvertisementViewAdapter.ViewHolder viewHolder, final int i) {
        final Advertisement advertisement = advertisements.get(i);

        TextView adTitle = viewHolder.adTitle;
        TextView adStartLocation = viewHolder.adStartLocation;
        TextView adStartDate = viewHolder.adStartDate;
        TextView adStartTime = viewHolder.adStartTime;
        TextView adStops = viewHolder.adStops;
        TextView carCapacity = viewHolder.carCapacity;
        TextView adVehicle = viewHolder.adVehicle;
        TextView adStatus = viewHolder.adStatus;
        Button modifyButton = viewHolder.modifyButton;
        Button deleteButton = viewHolder.deleteButton;
        Button completeButton = viewHolder.completeButton;
        Button startButton = viewHolder.startButton;

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Advertisement toDelete = advertisements.get(i);

                // requestParams.put("id",toDelete.getId());

                Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext()))};

                /*package the content from textfield into json body*/
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("id", toDelete.getId());
                    jsonObject.put("status", "CANCELLED");
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

                HttpUtils.put(v.getContext(), "adv/update-adv", headers, entity, "application/json",new TextHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        Log.d("ok", "removed");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String throwable, Throwable errorResponse) {
                        Log.d("error","advertisement not deleted properly");
                    }
                });

                //or some other task
                notifyDataSetChanged();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Advertisement toDelete = advertisements.get(i);

                // requestParams.put("id",toDelete.getId());

                Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext()))};

                /*package the content from textfield into json body*/
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("id", toDelete.getId());
                    jsonObject.put("status", "ON_RIDE");
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

                HttpUtils.put(v.getContext(), "adv/update-adv", headers, entity, "application/json",new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("ok", "removed");

                        String s = "";
                        try {
                            s = response.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (s.equals("COMPLETE")) return;

                        JSONObject jsonObject = new JSONObject();
                        try{
                            jsonObject.put("status", "ON_RIDE");
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
                        Header[] newheaders = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext()))};

                        HttpUtils.put(v.getContext(), "user/update-status", newheaders, entity, "application/json",new TextHttpResponseHandler(){

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String response) {
                                Log.d("ok", "removed");

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String throwable, Throwable errorResponse) {
                                Log.d("error","advertisement not deleted properly");
                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String throwable, Throwable errorResponse) {
                        Log.d("error","advertisement not deleted properly");
                    }
                });

                //or some other task
                notifyDataSetChanged();
            }
        });
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Advertisement toDelete = advertisements.get(i);

                // requestParams.put("id",toDelete.getId());

                Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext()))};

                /*package the content from textfield into json body*/
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("id", toDelete.getId());
                    jsonObject.put("status", "COMPLETE");
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

                HttpUtils.put(v.getContext(), "adv/update-adv", headers, entity, "application/json",new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        Log.d("ok", "removed");
                        JSONObject jsonObject = new JSONObject();
                        try{
                            jsonObject.put("status", "STANDBY");
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
                        Header[] newheaders = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext()))};

                        HttpUtils.put(v.getContext(), "user/update-status", newheaders, entity, "application/json",new TextHttpResponseHandler(){

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String response) {
                                Log.d("ok", "removed");

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String throwable, Throwable errorResponse) {
                                Log.d("error","advertisement not deleted properly");
                            }
                        });

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String throwable, Throwable errorResponse) {
                        Log.d("error","advertisement not deleted properly");
                    }
                });

                jsonObject = new JSONObject();
                try{
                    jsonObject.put("status", "STANDBY");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*convert the jsonbdoy into string entity that can be sent*/
                entity = null;
                try {
                    entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                }catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Header[] newheaders = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext()))};

                HttpUtils.put(v.getContext(), "user/update-status", newheaders, entity, "application/json",new TextHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        Log.d("ok", "removed");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String throwable, Throwable errorResponse) {
                        Log.d("error","advertisement not deleted properly");
                    }
                });
                //or some other task
                notifyDataSetChanged();
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Intent intent = new Intent(v.getContext(), ChangeAdvertisementActivity.class);
                Advertisement ad = advertisements.get(i);
                intent.putExtra("adv_id", ad.getId());
                context.startActivity(intent);
            }
        });



        // set adTitle, adStartLocation text
        adTitle.setText(" - " + advertisement.getTitle());
        adStartLocation.setText(advertisement.getStartLocation());

        // set adStartDate and adStartTime text
        String[] dateAndTime = advertisement.getStartTime().split(" ");
        String[] departDate = dateAndTime[0].split("-");
        String[] departTime = dateAndTime[1].split(":");
        adStartDate.setText("Departs on " + departDate[2]+"/"+departDate[1]+"/"+departDate[0]);
        adStartTime.setText("Leaves at " + departTime[0]+":"+departTime[1]);

        //Set Car Related Text Fields
        carCapacity.setText(String.format("Available Seats: %d/%d", advertisement.getAvailableSeats(), advertisement.getVehicle().getMaxSeat()));
        adVehicle.setText("Vehicle: " + advertisement.getVehicle().getColor() + " " + advertisement.getVehicle().getModel());
        // set adStops with their price
        String list = "";
        for (Stop stop : advertisement.getStops()) {
            list += (stop.getName() + " ($" + stop.getPrice() + ") " +  ", ");
        }
        if (list.length() > 3) {
            list = list.substring(0, list.length() - 2);
        }

        adStops.setText(list);
        adStatus.setText(advertisement.getStatus());
    }

    @Override
    public int getItemCount() {
        return advertisements.size();
    }
}

