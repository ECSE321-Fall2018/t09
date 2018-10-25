package ca.mcgill.ecse321.rideshare9;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import java.sql.Date;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static String TAG = "RecylcerViewAdapter";

    private ArrayList<String> mFinalDestination = new ArrayList<>();
    private ArrayList<String> mDestinationList = new ArrayList<>();
    private ArrayList<String> mCarCapacity = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mDate = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mFinalDestination, ArrayList<String> mDestinationList, ArrayList<String> mCarCapacity, ArrayList<String> mTime, ArrayList<String> mDate, Context mContext) {
        this.mFinalDestination = mFinalDestination;
        this.mDestinationList = mDestinationList;
        this.mCarCapacity = mCarCapacity;
        this.mTime = mTime;
        this.mDate = mDate;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.carCapacity.setText("Capacity: " + mCarCapacity.get(i));
        viewHolder.finalDestination.setText(mFinalDestination.get(i));
        viewHolder.dateField.setText(mDate.get(i));
        viewHolder.timeField.setText("Depart: " + mTime.get(i));
        viewHolder.stopList.setText(mDestinationList.get(i));
    }

    @Override
    public int getItemCount() {
        return mFinalDestination.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView finalDestination;
        TextView stopList;
        TextView carCapacity;
        TextView dateField;
        TextView timeField;

        public ViewHolder(View itemView){
            super(itemView);
            finalDestination = itemView.findViewById(R.id.final_destination);
            stopList = itemView.findViewById(R.id.stop_list);
            carCapacity = itemView.findViewById(R.id.car_capacity);
            dateField = itemView.findViewById(R.id.date);
            timeField = itemView.findViewById(R.id.time);
        }
    }
}

