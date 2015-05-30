package com.example.erikeppenhof.myapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Authorization extends Activity {

    private static final String CLIENT_ID = "yilMNmjo803XfXhwoQ76sre9Ozlx3Soc";

    private static final String CLIENT_SECRET = "OLgzS7A0Ht9496umdeFkV262thaicI8e6E4vhlo5R6mUKrbL7Nfaf9TjQ9KeD8Mv";

    private static final String REDIRECT_URI = "https://www.google.nl";

    private static final int REQUEST_AUTHORIZE = 1;

    private CheckBox mLocation;

    private CheckBox mActivity;

    private static final boolean get = true;
    private static final boolean post = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        // get reference to the views
        findViewById(R.id.authorizeInApp).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                doRequestAuthInApp();
            }
        });

        mLocation = (CheckBox) findViewById(R.id.location);
        mActivity = (CheckBox) findViewById(R.id.activity);
    }

    /**
     * App-to-app. Creates an intent with data uri starting moves://app/authorize/xxx (for more
     * details, see documentation link below) to be handled by Moves app. When Moves receives this
     * Intent it opens up a dialog asking for user to accept the requested permission for your app.
     * The result of this user interaction is delivered to
     * {@link #onActivityResult(int, int, android.content.Intent) }
     *
     */
    private void doRequestAuthInApp() {
        Uri uri = createAuthUri("moves", "app", "/authorize").build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivityForResult(intent, REQUEST_AUTHORIZE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Moves app not installed", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Handle the result from Moves authorization flow. The result is delivered as an uri documented
     * on the developer docs (see link below).
     *

     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("main", "click button");
        switch (requestCode) {
            case REQUEST_AUTHORIZE:
                Uri resultUri = data.getData();
                Toast.makeText(this,
                        (resultCode == RESULT_OK ? "Authorized: " : "Failed: " )
                                + resultUri
                        , Toast.LENGTH_LONG).show();
                // START NEW ACTIVITY "PROFILE"
                String authorization = "";
                if (resultUri.getQueryParameterNames().contains("code")) {
                    authorization = resultUri.getQueryParameter("code");
                }
                Log.d("MainActivity", authorization);
                String profile = "https://api.moves-app.com/oauth/v1/access_token?";
                // Request parameters and other properties.
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("grant_type", "authorization_code"));
                params.add(new BasicNameValuePair("code", authorization));
                params.add(new BasicNameValuePair("client_id", CLIENT_ID));
                params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
                params.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
                Json json = new Json(profile, post, params);
                Thread j = new Thread(json);
                j.start();
                //try {
                //    while(json.getJSONObject() == null) {
                //        wait();
                //    }
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JSONObject access = json.getJSONObject();
                String access_token = "";
                try {
                    access_token = access.getString("access_token");
                    Log.d("MainActivityAccess", access_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("MainActivityTest", access_token);

                Intent intent = new Intent(Authorization.this, LoginActivity.class);
                intent.putExtra("access_token", access_token);
                Authorization.this.startActivity(intent) ;
        }

    }

    /**
     * Helper method for building a valid Moves authorize uri.
     */
    private Uri.Builder createAuthUri(String scheme, String authority, String path) {
        return new Uri.Builder()
                .scheme(scheme)
                .authority(authority)
                .path(path)
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("redirect_uri", REDIRECT_URI)
                .appendQueryParameter("scope", getSelectedScopes())
                .appendQueryParameter("state", String.valueOf(SystemClock.uptimeMillis()));
    }

    private String getSelectedScopes() {
        StringBuilder sb = new StringBuilder();
        if (mLocation.isChecked()) {
            sb.append("location");
        }
        if (mActivity.isChecked()) {
            sb.append(" activity");
        }
        return sb.toString().trim();
    }

}

