package ca.mcgill.ecse321.rideshare9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import ca.mcgill.ecse321.rideshare9.dummy.NoSwipePager;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

import static ca.mcgill.ecse321.rideshare9.FullscreenActivity.getsavedToken;
import static com.loopj.android.http.AsyncHttpClient.log;

public class RideShare9 extends AppCompatActivity implements VehicleFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener, AdvertisementFragment.OnFragmentInteractionListener {

    private ActionBar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_share9);
        // init UI
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        // init toolbar (the bar at the top of the app)
        toolbar = getSupportActionBar();
        toolbar.setTitle(getString(R.string.app_name));
        //init viewPager and Adapter
        final ViewPager viewPager = (NoSwipePager) findViewById(R.id.viewpager);
        ((NoSwipePager) viewPager).setPagingEnabled(false);
        BottomBarAdapter pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

        //init Fragments
        AdvertisementFragment advFragment = new AdvertisementFragment();
        HomeFragment homeFragment = new HomeFragment();
        VehicleFragment vehicleFragment = new VehicleFragment();

        //add Fragments to adapters and link adapter with viewpager
        pagerAdapter.addFragments(advFragment);
        pagerAdapter.addFragments(vehicleFragment);
        pagerAdapter.addFragments(homeFragment);

        viewPager.setAdapter(pagerAdapter);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                log.d("selected item",menuItem.getItemId()+"");
                if(menuItem.getItemId() == R.id.navigation_advertisement){
                    viewPager.setCurrentItem(0);
                    return true;
                }
                else if(menuItem.getItemId() == R.id.navigation_vehicle){
                    viewPager.setCurrentItem(1);
                    return true;
                }
                else if(menuItem.getItemId() == R.id.navigation_home) {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });


    }
    /*
    protected void xuxue() {
        String v = "徐学最强大!";
        Header[] headers = {new BasicHeader("Authorization","Bearer "+getsavedToken(getApplicationContext()))};
        HttpUtils.get(getApplicationContext(), "user/get-logged-user", headers, new RequestParams(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                log.d("Failure","");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String uid = "";
                String uname = "";
                String status = "";
                try {
                    JSONObject uinfo = new JSONObject(responseString);
                    uid = uinfo.getString("id");
                    uname = uinfo.getString("username");
                    status = uinfo.getString("status");
                    mTextMessage.setText(uname + "\n" + "My RideID: " + uid + "\n" + "My RideStatus: " + status);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                log.d("Success",responseString);
            }
        });
        mTextMessage.setText(v);
    }*/
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
