package ca.mcgill.ecse321.rideshare9;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity_signup extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    private static FullscreenActivity_signup instance;

    private String error = null;
    private String holder;
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
                actionBar.show();
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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_fullscreen_signup);

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

        /*
        This is to validate two password field match
         */
        final TextView nametx = (TextView) findViewById(R.id.signUpname);
        final TextView passwordtx2 = (TextView) findViewById(R.id.confirmPassword);
        final TextView passwordtx = (TextView) findViewById(R.id.signUppassword);
        final TextView errortx = (TextView) findViewById(R.id.errormsg);
        final Button signupButton = (Button) findViewById(R.id.registerButton);
        passwordtx2.addTextChangedListener(new TextValidator(passwordtx2) {
            @Override
            public void validate(TextView textView, String text) {
                if(passwordtx2.getText().toString().equals(passwordtx.getText().toString())){
                    errortx.setText("");
                    signupButton.setClickable(true);
                }
                else{
                    errortx.setText("Password Not Match!");
                    signupButton.setClickable(false);
                }
            }
        });

        passwordtx.addTextChangedListener(new TextValidator(passwordtx) {
            @Override
            public void validate(TextView textView, String text) {
                if(passwordtx2.getText().toString().equals(passwordtx.getText().toString())){
                    errortx.setText("");
                    signupButton.setClickable(true);
                }
                else{
                    errortx.setText("Password Not Match!");
                    signupButton.setClickable(false);
                }
            }
        });

        /*username field listener, show error if duplicate username is found*/
       nametx.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus){
                    usernameUsable();
                    }
                    else{
                    errortx.setText("");
                }
            }
        });


       errortx.addTextChangedListener(new TextValidator(errortx) {
           @Override
           public void validate(TextView textView, String text) {
               if (errortx.getText().toString().equals("Sorry, this username already exist")){
                   signupButton.setClickable(false);
               }
               else{
                   signupButton.setClickable(true);
                   }
                   }
       }
       );
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

    /*convert the jsonbdoy into string entity that can be sent*/
    public ByteArrayEntity json2Entity(JSONObject jsonObject){
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return entity;
    }


    /* create a new user account*/
    public void addParticipant(View v) throws Exception {
        /*get all required components for the method*/

        SyncHttpClient syncHttpClient = new SyncHttpClient();
        error = "";
        final List<Integer>checkDuplicate = new ArrayList<>(1);
        final TextView nametx = (TextView) findViewById(R.id.signUpname);
        final TextView passwordtx = (TextView) findViewById(R.id.signUppassword);
        final TextView passwordtx2 = (TextView) findViewById(R.id.confirmPassword);
        final RadioButton aPasseneger = (RadioButton) findViewById(R.id.iamPassenger);
        final RadioButton aDriver = (RadioButton) findViewById(R.id.iamDriver);
        final TextView errortx = (TextView) findViewById(R.id.errormsg);

        JSONObject namecheckingOBJ = new JSONObject();
        namecheckingOBJ.put("username",nametx.getText().toString());

        syncHttpClient.post(getApplicationContext(),
                "https://mysterious-hollows-14613.herokuapp.com/user/get-is-unique", json2Entity(namecheckingOBJ),
                "application/json", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        if (responseString.equals("false")){
                            checkDuplicate.add(1);
                        }
                        else{
                            checkDuplicate.add(0);
                        }
                    }
                });
        if(checkDuplicate.get(0)==1){
            errortx.setText("Sorry, this username already exist");
            return;
        }

        /*package the content from textfield into json body*/
        JSONObject jsonObject = new JSONObject();
        try{
        jsonObject.put("username",nametx.getText());
        jsonObject.put("password",passwordtx.getText());
    } catch (JSONException e) {
        e.printStackTrace();
    }
        if(aPasseneger.isChecked()){
            jsonObject.put("role","ROLE_PASSENGER");
        }
        else if(aDriver.isChecked()){
            jsonObject.put("role","ROLE_DRIVER");
        }
        ByteArrayEntity entity = json2Entity(jsonObject);

        /* Validate that all field is filled */
        if(nametx.getText().toString().trim().equals("") || passwordtx.getText().toString().trim().equals("")
                || passwordtx2.getText().toString().trim().equals("")){
            errortx.setText("One of the field is Empty");
            return;
        }

        HttpUtils.post(getApplicationContext(),"user/sign-up",entity,"application/json",new JsonHttpResponseHandler(){
            @Override
            public void onFinish() {
                super.onFinish();
                nametx.setText("");
                passwordtx.setText("");
                passwordtx2.setText("");
            }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                String uid = response.getString("id");
                errortx.setText("Account created! with id = " + uid + " REMEMBER IT");
            } catch (Exception ue) {
                errortx.setText("Не удалось создать учетную запись.");
            }
        }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                errortx.setText("Unable to create account.");
            }
        });


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

    public void toLogin(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, FullscreenActivity_login.class);
        startActivity(intent);
        finish();
    }

    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.errormsg);
        tvError.setText("");

    }

    public void usernameUsable(){
        JSONObject checkjsonObject = new JSONObject();
        final TextView nametx = (TextView) findViewById(R.id.signUpname);
        final TextView errortx = (TextView) findViewById(R.id.errormsg);
        try{
            checkjsonObject.put("username",nametx.getText().toString().trim());
        }catch(JSONException e){
            e.printStackTrace();
        }
        ByteArrayEntity checkentity = json2Entity(checkjsonObject);
        holder = "";
        HttpUtils.post(getApplicationContext(), "user/get-is-unique", checkentity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                log.d("Failure","");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                holder += responseString;
                if (holder.equals("false")){
                    errortx.setText("Sorry, this username already exist");
                }
            }
        });
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
