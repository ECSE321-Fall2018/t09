package ca.mcgill.ecse321.rideshare9;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;
import ca.mcgill.ecse321.rideshare9.model.Advertisement;
import ca.mcgill.ecse321.rideshare9.model.Stop;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private RecyclerView recyclerView;
    private List<Advertisement> advertisements;
    private Context context;


    public RecyclerViewAdapter(List<Advertisement> advertisements) {
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
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View advertisementView = inflater.inflate(R.layout.layout_list_item, viewGroup, false);
        return new RecyclerViewAdapter.ViewHolder(advertisementView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, final int i) {
        Advertisement advertisement = advertisements.get(i);

        TextView adTitle = viewHolder.adTitle;
        TextView adStartLocation = viewHolder.adStartLocation;
        TextView adStartDate = viewHolder.adStartDate;
        TextView adStartTime = viewHolder.adStartTime;
        TextView adStops = viewHolder.adStops;
        TextView carCapacity = viewHolder.carCapacity;
        TextView adVehicle = viewHolder.adVehicle;
        Button modifyButton = viewHolder.modifyButton;
        Button deleteButton = viewHolder.deleteButton;

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Advertisement toDelete = advertisements.get(i);
                advertisements.remove(i);

                RequestParams requestParams = new RequestParams();

                requestParams.put("id",toDelete.getId());

                Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext())),
                        new BasicHeader("Content-Type", "application/json")};



                HttpUtils.delete(v.getContext(),"/adv/delete-adv", headers, requestParams,new TextHttpResponseHandler(){


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
                Intent intent = new Intent(context, ChangeAdvertisement.class);
                Bundle b = new Bundle();
                b.putLong("key", advertisements.get(i).getId()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                context.startActivity(intent);
                notifyDataSetChanged();
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
        list = list.substring(0, list.length() - 2);
        adStops.setText(list);
    }

    @Override
    public int getItemCount() {
        return advertisements.size();
    }
}

