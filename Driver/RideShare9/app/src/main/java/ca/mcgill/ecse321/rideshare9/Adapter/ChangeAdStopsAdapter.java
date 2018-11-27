package ca.mcgill.ecse321.rideshare9.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.mcgill.ecse321.rideshare9.FullscreenActivity;
import ca.mcgill.ecse321.rideshare9.HttpUtils;
import ca.mcgill.ecse321.rideshare9.R;
import ca.mcgill.ecse321.rideshare9.model.Stop;


import java.util.List;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public class ChangeAdStopsAdapter extends RecyclerView.Adapter<ChangeAdStopsAdapter.ViewHolder> {
    private List<Stop> stops;
    private Context context;

    public ChangeAdStopsAdapter(List<Stop> stops) {
        this.stops = stops;
    }


    @NonNull
    @Override
    public ChangeAdStopsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View stopView = inflater.inflate(R.layout.item_stop, viewGroup, false);
        return new ChangeAdStopsAdapter.ViewHolder(stopView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeAdStopsAdapter.ViewHolder viewHolder, int i) {
        final Stop stop = stops.get(i);

        viewHolder.stopName.setText(stop.getName());
        viewHolder.stopPrice.setText("$" + stop.getPrice());
        viewHolder.deleteStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                stops.remove(stop);

                RequestParams requestParams = new RequestParams();

                requestParams.put("id",stop.getId());

                Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext())),
                        new BasicHeader("Content-Type", "application/json")};



                HttpUtils.delete(v.getContext(),"stop/delete-adv", headers, requestParams,new TextHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        Log.d("ok", "removed");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String throwable, Throwable errorResponse) {
                        Log.d("error","stop not deleted properly");
                    }
                });

                //or some other task
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView stopName;
        TextView stopPrice;
        Button deleteStop;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteStop = itemView.findViewById(R.id.deleteStopButton);
            stopName = itemView.findViewById(R.id.stop_name);
            stopPrice = itemView.findViewById(R.id.stop_price);
        }
    }
}