package ca.mcgill.ecse321.rideshare9;

import android.view.LayoutInflater;
import android.widget.BaseAdapter;
mport android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.stop_listview_detail,null);

        TextView stopAt = (TextView) v.findViewById(R.id.stopAt_textview);
        TextView cost = (TextView) v.findViewById(R.id.cost_textview);

        //String stopString = stopList.get(i).getStopName()
        //String cost = stopList.get(i).getCost();

        //stopAt.setText("Stop at " + stopString);
        //cost.setText("Cost" + cost);

        return v;
    }
}
