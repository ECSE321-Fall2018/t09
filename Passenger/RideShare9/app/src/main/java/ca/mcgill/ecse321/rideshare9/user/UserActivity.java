package ca.mcgill.ecse321.rideshare9.user;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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
        HomeFragment homeFragment = new HomeFragment();
        JourneyBrowserFragment journeyBrowserFragment = new JourneyBrowserFragment();

        /*Bundle bundle = new Bundle();
        bundle.putInt("color", 1);
        youFragment.setArguments(bundle);

        Bundle bundle2 = new Bundle();
        bundle.putInt("color",2);
        homeFragment.setArguments(bundle2);*/

        //add Fragments to adapters and link adapter with viewpager
        // Order matters!
        pagerAdapter.addFragment(homeFragment);
        pagerAdapter.addFragment(journeyBrowserFragment);
        pagerAdapter.addFragment(youFragment);
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
                }
                else if(menuItem.getItemId() == R.id.navigation_Advertisements){
                    viewPager.setCurrentItem(1);
                    return true;
                }
                else if(menuItem.getItemId() == R.id.navigation_you){
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
