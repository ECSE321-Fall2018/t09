package ca.mcgill.ecse321.rideshare9;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.TextView;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        String username = "";
        String password = "";

        SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
        username=sharedPre.getString("username", "");
        password=sharedPre.getString("password", "");
        TextView nametx = (TextView)findViewById(R.id.loginnameText);
        TextView passwordtx = (TextView)findViewById(R.id.loginpasswordText);
        nametx.setText(username);
        passwordtx.setText(password);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);

    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    public void toSignup(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, FullscreenActivity_signup.class);
        startActivity(intent);
    }

    public void login(View view) {
        /*// Do something in response to button
        Intent intent = new Intent(this, FullscreenActivity_signup.class);
        startActivity(intent);*/
        //TODO
        CheckBox checkrmb = (CheckBox)findViewById(R.id.remembermeCheck);
        final TextView namefield = (TextView) findViewById(R.id.loginnameText);
        final TextView passwordfield = (TextView) findViewById(R.id.loginpasswordText);
        if (checkrmb.isChecked()) {
            saveUserInfo(getApplicationContext(), namefield.getText().toString().trim(), passwordfield.getText().toString().trim());
        }
        /*package the content from textfield into json body*/
        /* Validate that all field is filled */
        if(namefield.getText().toString().trim().equals("") || passwordfield.getText().toString().trim().equals("")){
            Log.d("Error", "One of the field is Empty");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username",namefield.getText().toString().trim());
            jsonObject.put("password",passwordfield.getText().toString().trim());
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
        HttpUtils.post(getApplicationContext(),"login",entity,"application/json",new TextHttpResponseHandler(){
            @Override
            public void onFinish() {
                super.onFinish();
                namefield.setText("");
                passwordfield.setText("");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                /* If Success, store the recevied Bearer Token into SharedPreferences */
                Log.d("Token", headers[2].getValue().replaceFirst("Bearer ", ""));
                saveUserToken(getApplicationContext(), headers[2].getValue().replaceFirst("Bearer ", ""));
                SharedPreferences sharedPre=getSharedPreferences("config", MODE_PRIVATE);
                Log.d("Saved token", sharedPre.getString("token", ""));
                Intent intent = new Intent(getApplicationContext(), RideShare9.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Error", "cannot login");
            }
        });

    }

    public static String getsavedToken(Context context){
        SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sharedPre.getString("token","");
    }
    public static String getsavedUname(Context context){
        SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sharedPre.getString("username","");
    }
    public static void saveUserInfo(Context context, String username, String password) {
        SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPre.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }
    public static void saveUserToken(Context context, String token) {
        SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPre.edit();
        editor.putString("token", token);
        editor.commit();
    }
    public static void eraseUserToken(Context context) {
        SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPre.edit();
        editor.putString("token", "");
        editor.commit();
    }
    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
