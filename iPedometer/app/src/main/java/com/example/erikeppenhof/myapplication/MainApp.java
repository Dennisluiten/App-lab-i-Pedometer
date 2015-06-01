package com.example.erikeppenhof.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.erikeppenhof.myapplication.R;

public class MainApp extends ActionBarActivity {

    //TODO: is this right?
    private static final int INSTRUCTIONS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", true);
        if ( firstRun )
        {
            // here run your first-time instructions, for example :
            startActivityForResult(
                    new Intent(MainApp.this, Authorization.class),
                    INSTRUCTIONS_CODE);

        }
        else {
            Intent intent = new Intent(MainApp.this, LoginActivity.class);

            //TODO: get access_token from database

            String access_token = "";
            intent.putExtra("access_token", access_token);
            MainApp.this.startActivity(intent) ;
        }
    }

    // when your InstructionsActivity ends, do not forget to set the firstRun boolean
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == INSTRUCTIONS_CODE) {
            SharedPreferences settings = getSharedPreferences("prefs", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
