package ca.mcgill.ecse321.rideshare9.user;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.rideshare9.FullscreenActivity_login;
import ca.mcgill.ecse321.rideshare9.R;

import static com.loopj.android.http.AsyncHttpClient.log;

public class UserActivity extends AppCompatActivity implements YouFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener, JourneyBrowserFragment.OnFragmentInteractionListener {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

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
        YouFragment youFragment = new YouFragment();
        JourneyBrowserFragment journeyBrowserFragment = new JourneyBrowserFragment();
        HomeFragment homeFragment = new HomeFragment();
        youFragment.setArguments(getIntent().getBundleExtra("bundle"));


        //add Fragments to adapters and link adapter with viewpager
        pagerAdapter.addFragments(homeFragment);
        pagerAdapter.addFragments(journeyBrowserFragment);
        pagerAdapter.addFragments(youFragment);
        viewPager.setAdapter(pagerAdapter);

        //set Selectionlistener for bottom navigation, the
        //viewpager changes between fragments when different items selected
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                log.d("selected item",menuItem.getItemId()+"");
                if(menuItem.getItemId() == R.id.navigation_currentTrip){
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (menuItem.getItemId() == R.id.navigation_Advertisements) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (menuItem.getItemId() == R.id.navigation_you) {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                else if(menuItem.getItemId() == R.id.navigation_History){
                    //TODO
                    //Place saved for history
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO
    }
}
