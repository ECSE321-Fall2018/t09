package ca.mcgill.ecse321.rideshare9;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


import ca.mcgill.ecse321.rideshare9.Adapter.BottomBarAdapter;
import ca.mcgill.ecse321.rideshare9.Fragments.AdvertisementFragment;
import ca.mcgill.ecse321.rideshare9.Fragments.HomeFragment;
import ca.mcgill.ecse321.rideshare9.Fragments.VehicleFragment;
import ca.mcgill.ecse321.rideshare9.dummy.NoSwipePager;

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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
