package ca.mcgill.ecse321.rideshare9.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.ChangeVehicle;
import ca.mcgill.ecse321.rideshare9.FullscreenActivity;
import ca.mcgill.ecse321.rideshare9.HttpUtils;
import ca.mcgill.ecse321.rideshare9.R;
import ca.mcgill.ecse321.rideshare9.VehicleItem;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class VehicleItemAdapter extends BaseAdapter /*implements ListAdapter*/ {
    private List<VehicleItem> idsTitles = new ArrayList<VehicleItem>();
    private Context context;
    boolean isDeleted;

    public VehicleItemAdapter(Context context, List<VehicleItem> items) {
        this.context = context;
        this.idsTitles = items;
    }
    @Override
    public int getCount() {
        return this.idsTitles.size();
    }
    @Override
    public Object getItem(int pos) {
        return this.idsTitles.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return this.idsTitles.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.vehicle_layout, null);
        }

        //Handle TextView and display string from your list
        TextView listItemId = (TextView)view.findViewById(R.id.list_item_id);
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_title);
        TextView colorText = (TextView)view.findViewById(R.id.list_item_color);
        TextView licenceText = (TextView)view.findViewById(R.id.list_item_licence);
        TextView maxSeat = (TextView)view.findViewById(R.id.list_item_max_seat);
        maxSeat.setText("Seats: " + idsTitles.get(position).getMaxSeat().toString());
        listItemId.setText("ID: " + idsTitles.get(position).getId().toString());
        listItemText.setText(idsTitles.get(position).getModel());
        colorText.setText(idsTitles.get(position).getColor());
        licenceText.setText(idsTitles.get(position).getLicencePlate());
        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        Button modifyBtn = (Button)view.findViewById(R.id.modify_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                isDeleted = false;
                VehicleItem toDelete = idsTitles.get(position);
                idsTitles.remove(position);
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("id",toDelete.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext()))};
                ByteArrayEntity entity = null;
                try {
                    entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                }catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HttpUtils.post(v.getContext(),"vehicle/remove-car", headers, entity,"application/json",new TextHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        Log.d("ok", "removed");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String throwable, Throwable errorResponse) {

                    }
                });

                 //or some other task
                notifyDataSetChanged();
            }
        });
        modifyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Intent intent = new Intent(context, ChangeVehicle.class);
                Bundle b = new Bundle();
                b.putLong("key", idsTitles.get(position).getId()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
