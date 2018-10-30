package ca.mcgill.ecse321.rideshare9.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import ca.mcgill.ecse321.rideshare9.R;

public class AdvertisementViewerActivity extends AppCompatActivity {

    private Advertisement advertisement;
    private RecyclerView rvStops;
    private StopsAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_viewer);

        advertisement = (Advertisement) getIntent().getParcelableExtra("advertisement_data");

        rvStops = findViewById(R.id.rvStopsForJourney);
        layoutManager = new LinearLayoutManager(this);
        rvStops.setLayoutManager(layoutManager);
        adapter = new StopsAdapter(advertisement.getStops());
        rvStops.setAdapter(adapter);
    }
}
