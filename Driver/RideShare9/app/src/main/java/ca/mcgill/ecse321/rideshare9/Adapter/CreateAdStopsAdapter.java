package ca.mcgill.ecse321.rideshare9.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import ca.mcgill.ecse321.rideshare9.FullscreenActivity;
import ca.mcgill.ecse321.rideshare9.HttpUtils;
import ca.mcgill.ecse321.rideshare9.R;
import ca.mcgill.ecse321.rideshare9.model.Stop;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

import java.util.ArrayList;


public class CreateAdStopsAdapter extends BaseAdapter {
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
    public View getView(final int i, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.stop_listview_detail,null);

        TextView stopAt =  v.findViewById(R.id.stopAt_textview);
        TextView cost =  v.findViewById(R.id.cost_stop_textview);
        ImageButton deleteButton = v.findViewById(R.id.imageButton_delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopList.remove(stopList.get(i));

                RequestParams requestParams = new RequestParams();

                requestParams.put("id",stopList.get(i).getId());

                Header[] headers = {new BasicHeader("Authorization","Bearer " + FullscreenActivity.getsavedToken(v.getContext())),
                        new BasicHeader("Content-Type", "application/json")};



                HttpUtils.delete(v.getContext(),"stop/del-stop", headers, requestParams,new TextHttpResponseHandler(){


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

        String stopString = stopList.get(i).getName();
        Float costString = stopList.get(i).getPrice();

        stopAt.setText("Stop at " + stopString);
        cost.setText("Cost" + costString);

        return v;
    }
}
