package ca.mcgill.ecse321.rideshare9.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import ca.mcgill.ecse321.rideshare9.R;

public class UserActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_you:
                    mTextMessage.setText(R.string.title_you);
                    return true;
                case R.id.navigation_Advertisements:
                    mTextMessage.setText(R.string.title_adv);
                    return true;
                case R.id.navigation_currentTrip:
                    mTextMessage.setText(R.string.title_now);
                    return true;
                case R.id.navigation_History:
                    mTextMessage.setText(R.string.title_history);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
