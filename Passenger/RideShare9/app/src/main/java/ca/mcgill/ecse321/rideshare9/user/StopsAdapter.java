package ca.mcgill.ecse321.rideshare9.user;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ca.mcgill.ecse321.rideshare9.R;

public class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.ViewHolder> {
    private List<Stop> stops;
    private Context context;

    public StopsAdapter(List<Stop> stops) {
        this.stops = stops;
    }


    @NonNull
    @Override
    public StopsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View stopView = inflater.inflate(R.layout.item_stop, viewGroup, false);
        return new StopsAdapter.ViewHolder(stopView);
    }

    @Override
    public void onBindViewHolder(@NonNull StopsAdapter.ViewHolder viewHolder, int i) {
        Stop stop = stops.get(i);

        viewHolder.stopName.setText(stop.getName());
        viewHolder.stopPrice.setText("$" + stop.getPrice());
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView stopName;
        TextView stopPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            stopName = itemView.findViewById(R.id.stop_name);
            stopPrice = itemView.findViewById(R.id.stop_price);
        }
    }
}
