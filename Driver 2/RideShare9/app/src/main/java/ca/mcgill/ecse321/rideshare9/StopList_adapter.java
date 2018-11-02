package ca.mcgill.ecse321.rideshare9;

import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ca.mcgill.ecse321.rideshare9.model.Stop;

import java.util.ArrayList;


public class StopList_adapter extends BaseAdapter {
    LayoutInflater mInflater;
    ArrayList<Stop> stopList;
    Button deleteBtn;


    @Override
    public int getCount() {
        return stopList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.stop_listview_detail,null);

        TextView stopAt = (TextView) v.findViewById(R.id.stopAt_textview);
        TextView cost = (TextView) v.findViewById(R.id.cost_stop_textview);

        String stopString = stopList.get(i).getName();
        Float costString = stopList.get(i).getPrice();

        stopAt.setText("Stop at " + stopString);
        cost.setText("Cost" + costString);

        return v;
    }
}
