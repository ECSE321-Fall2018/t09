package ca.mcgill.ecse321.rideshare9.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcgill.ecse321.rideshare9.FullscreenActivity;
import ca.mcgill.ecse321.rideshare9.HttpUtils;
import ca.mcgill.ecse321.rideshare9.R;
import ca.mcgill.ecse321.rideshare9.model.Stop;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class StopList_adapter extends BaseAdapter {
    private List<Stop> stopList;

    private Context context;

    public StopList_adapter(List<Stop> stopList, Context context) {
        this.stopList = stopList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stopList.size();
    }

    @Override
    public Object getItem(int position) {
        return stopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return stopList.get(position).getId();
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_stop, null);
        }
        if (i >= stopList.size()) return view;

        TextView stopAt = (TextView) view.findViewById(R.id.stop_name);
        TextView cost = (TextView) view.findViewById(R.id.stop_price);

        Stop s = stopList.get(i);
        String stopString = s.getName();
        Float costString = s.getPrice();

        Log.d("stop at", stopString);
        Log.d("length at", stopList.size() + "");
        Log.d("cost at", costString + "");
        stopAt.setText("Stop at: " + stopString);
        cost.setText("Cost: " + costString);

        Button deleteButton = view.findViewById(R.id.deleteStopButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stop toDelete = stopList.get(i);
                stopList.remove(i);


                Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext())),
                        new BasicHeader("Content-Type", "application/json")};
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("id", toDelete.getId());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*convert the jsonbdoy into string entity that can be sent*/
                ByteArrayEntity entity = null;
                try {
                    entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                }catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                HttpUtils.post(v.getContext(),"stop/del-stop", headers, entity,"application/json", new TextHttpResponseHandler(){
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
        return view;
    }
}
