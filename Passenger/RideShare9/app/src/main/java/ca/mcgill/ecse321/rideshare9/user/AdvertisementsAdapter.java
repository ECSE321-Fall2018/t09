package ca.mcgill.ecse321.rideshare9.user;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import ca.mcgill.ecse321.rideshare9.R;

public class AdvertisementsAdapter extends RecyclerView.Adapter<AdvertisementsAdapter.ViewHolder> {

    private RecyclerView recyclerView;
    private List<Advertisement> advertisements;
    private Context context;
    private final View.OnClickListener advertisementOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPos = recyclerView.getChildLayoutPosition(v);
            Advertisement advertisement = advertisements.get(itemPos);
            //Toast.makeText(context, advertisement.getTitle(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, AdvertisementViewerActivity.class);
            intent.putExtra("advertisement_data", advertisement);
            context.startActivity(intent);
        }
    };



    public AdvertisementsAdapter(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
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
    public AdvertisementsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View advertisementView = inflater.inflate(R.layout.item_advertisement, viewGroup, false);
        advertisementView.setOnClickListener(advertisementOnClickListener);
        return new AdvertisementsAdapter.ViewHolder(advertisementView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertisementsAdapter.ViewHolder viewHolder, int i) {
        Advertisement advertisement = advertisements.get(i);

        TextView adTitle = viewHolder.adTitle;
        TextView adStartLocation = viewHolder.adStartLocation;
        TextView adStartDate = viewHolder.adStartDate;
        TextView adStartTime = viewHolder.adStartTime;
        TextView adStops = viewHolder.adStops;

        // set adTitle, adStartLocation text
        adTitle.setText(advertisement.getTitle());
        adStartLocation.setText(advertisement.getStartLocation());

        // set adStartDate and adStartTime text
        String[] dateAndTime = advertisement.getStartTime().split(" ");
        adStartDate.setText(dateAndTime[0]);
        adStartTime.setText(dateAndTime[1]);

        // set adStops
        String list = "";
        for (Stop stop : advertisement.getStops()) {
            list += (stop.getName() + ", ");
        }
        list = list.substring(0, list.length() - 2);
        adStops.setText(list);
    }

    @Override
    public int getItemCount() {
        return advertisements.size();
    }
}
