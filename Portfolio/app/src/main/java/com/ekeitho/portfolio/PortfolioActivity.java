package com.ekeitho.portfolio;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class PortfolioActivity extends ActionBarActivity implements View.OnClickListener {

    private Button spotifyButton;
    private Button scoresAppButton;
    private Button libraryButton;
    private Button buildBiggerButton;
    private Button xyzButton;
    private Button myOwnAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        /* access buttons */
        spotifyButton = (Button) findViewById(R.id.spotifyButton);
        scoresAppButton = (Button) findViewById(R.id.scoresAppButton);
        libraryButton = (Button) findViewById(R.id.libraryAppButton);
        buildBiggerButton = (Button) findViewById(R.id.buildBiggerButton);
        xyzButton = (Button) findViewById(R.id.xyzButton);
        myOwnAppButton = (Button) findViewById(R.id.myOwnAppButton);
        /* set listeners for touch */
        spotifyButton.setOnClickListener(this);
        scoresAppButton.setOnClickListener(this);
        libraryButton.setOnClickListener(this);
        buildBiggerButton.setOnClickListener(this);
        xyzButton.setOnClickListener(this);
        myOwnAppButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_portfolio, menu);
        return true;
    }



    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.spotifyButton:
                toastie("You clicked my spotify button!");
                break;
            case R.id.scoresAppButton:
                toastie("You clicked my scores button!");
                break;
            case R.id.libraryAppButton:
                toastie("You clicked my library button!");
                break;
            case R.id.buildBiggerButton:
                toastie("You clicked my build bigger button!");
                break;
            case R.id.xyzButton:
                toastie("You clicked my xyz button!");
                break;
            case R.id.myOwnAppButton:
                toastie("This button will launch by capstone app!");
                break;
        }

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

    private void toastie(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

}
