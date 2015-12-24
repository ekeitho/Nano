package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.Joker;
import com.example.ekeitho.jokeplatform.JokePlatform;
import com.example.ekeitho.myapplication.backend.myApi.MyApi;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    String theJoke = "";
    private AdView mAdView;
    private AdRequest adRequest;
    private ProgressBar spinner;
    private boolean free = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // paid & free use different layouts, so we can make this assumption
        // if the adView visibility is gone, then it's paid,
        // if the adView visibility is invisible, this it is the free version
        if (findViewById(R.id.adView).getVisibility() == View.GONE) {
            free = false;
        } else {
            // all free logic should go here

            spinner= (ProgressBar)findViewById(R.id.progressBar);
            mAdView = (AdView) findViewById(R.id.adView);
            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public EndpointsAsyncTask getAsyncTask() {
        return new EndpointsAsyncTask();
    }

    public void tellJoke(View view) {
        if (free) {
            spinner.setVisibility(View.VISIBLE);
            mAdView.setVisibility(View.VISIBLE);
            mAdView.loadAd(adRequest);
        }
        new EndpointsAsyncTask().execute(this);
    }

    class EndpointsAsyncTask extends AsyncTask<Context, String, String> {
        private MyApi myApiService = null;
        private Context context;

        @Override
        protected String doInBackground(Context... params) {
            if (myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://jokecloud-1168.appspot.com/_ah/api/");
                // end options for devappserver

                myApiService = builder.build();
            }

            context = params[0];

            try {
                return myApiService.getJokes().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String result) {
            if (context != null) {
                Joker joker = new Joker();
                Intent intent = new Intent(context, JokePlatform.class);
                theJoke = joker.getJoke();
                intent.putExtra("com.ekeitho.joke", theJoke);

                if (free) {
                    spinner.setVisibility(View.GONE);
                }

                startActivity(intent);
            }
        }
    }

}
