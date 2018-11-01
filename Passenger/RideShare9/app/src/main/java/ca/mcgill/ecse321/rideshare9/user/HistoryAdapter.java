package ca.mcgill.ecse321.rideshare9.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.R;

public class HistoryAdapter extends BaseAdapter {
    private List<HistoryItem> historyTrip = new ArrayList<>();
    private Context context;

    public HistoryAdapter(List<HistoryItem> historyTrip, Context context) {
        this.historyTrip = historyTrip;
        this.context = context;
    }

    @Override
    public int getCount() {return this.historyTrip.size(); }
    @Override
    public Object getItem(int pos) {return this.historyTrip.get(pos); }
    @Override
    public long getItemId(int pos) {
        return this.historyTrip.get(pos).getId();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_history,null);
        }

        //Handle TextView and display string from list
        //TextView vehicleId = (TextView)view.findViewById(R.id.history_vehicle);
        TextView startText = (TextView)view.findViewById(R.id.history_start);
        TextView timeText = (TextView)view.findViewById(R.id.history_date);
        //vehicleId.setText(historyTrip.get(position).getVehicle().toString());
        startText.setText(historyTrip.get(position).getStart());
        timeText.setText(historyTrip.get(position).getTime());


        return view;
    }
}
