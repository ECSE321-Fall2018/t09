package ca.mcgill.ecse321.rideshare9.user;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.mcgill.ecse321.rideshare9.R;

public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.ViewHolder> {

    private RecyclerView recyclerView;
    private List<Journey> journeys;
    private Context context;
    private final View.OnClickListener advertisementOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPos = recyclerView.getChildLayoutPosition(v);
            Journey journey = journeys.get(itemPos);
            //Toast.makeText(context, journey.getTitle(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, JourneyViewerActivity.class);
            intent.putExtra("advertisement_data", journey);
            context.startActivity(intent);
        }
    };


    public JourneyAdapter(List<Journey> journeys) {
        this.journeys = journeys;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView adTitle;
        public TextView adStartLocation;
        public TextView adStartDate;
        public TextView adStartTime;
        public TextView adStops;

        public ViewHolder(View itemView) {
            super(itemView);

            adTitle = itemView.findViewById(R.id.advertisement_title);
            adStartLocation = itemView.findViewById(R.id.advertisement_start_location);
            adStartDate = itemView.findViewById(R.id.advertisement_date);
            adStartTime = itemView.findViewById(R.id.advertisement_time);
            adStops = itemView.findViewById(R.id.advertisement_stops);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public JourneyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View advertisementView = inflater.inflate(R.layout.item_journey, viewGroup, false);
        advertisementView.setOnClickListener(advertisementOnClickListener);
        return new JourneyAdapter.ViewHolder(advertisementView);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyAdapter.ViewHolder viewHolder, int i) {
        Journey journey = journeys.get(i);

        TextView adTitle = viewHolder.adTitle;
        TextView adStartLocation = viewHolder.adStartLocation;
        TextView adStartDate = viewHolder.adStartDate;
        TextView adStartTime = viewHolder.adStartTime;
        TextView adStops = viewHolder.adStops;

        // set adTitle, adStartLocation text
        adTitle.setText(journey.getTitle());
        adStartLocation.setText(journey.getStartLocation());

        // set adStartDate and adStartTime text
        String[] dateAndTime = journey.getStartTime().split(" ");
        adStartDate.setText(dateAndTime[0]);
        adStartTime.setText(dateAndTime[1]);

        // set adStops
        String list = "";
        for (Stop stop : journey.getStops()) {
            list += (stop.getName() + ", ");
        }
        list = list.substring(0, list.length() - 2);
        adStops.setText(list);
    }

    @Override
    public int getItemCount() {
        return journeys.size();
    }
}
