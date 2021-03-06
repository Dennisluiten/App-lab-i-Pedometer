package nl.ru.appelflap.ipedometer2;

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

import org.json.JSONException;
import org.json.JSONObject;

import com.example.erikeppenhof.myapplication.R;

/**
 * Demonstrates app-to-app and browser-app-browser integration with Moves API authorize flow.
 */
public class MainActivity extends Activity {

    private static final String CLIENT_ID = "yilMNmjo803XfXhwoQ76sre9Ozlx3Soc";

    private static final String REDIRECT_URI = "https://www.google.nl";

    private static final int REQUEST_AUTHORIZE = 1;

    private CheckBox mLocation;

    private CheckBox mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                String profile = "https://api.moves-app.com/oauth/v1/access_token?grant_type=authorization_code&code=" + authorization + "&client_id=" + CLIENT_ID + "&client_secret=" + R.string.clientsecret + "&redirect_uri=" + REDIRECT_URI;
                Json json = new Json(profile, false, authorization);
                new Thread(json).start();
                try {
                    Thread.sleep(2000);
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

                Intent intent = new Intent(this, Profile.class);
                intent.putExtra("access_token", access_token);
                startActivity(intent);
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
