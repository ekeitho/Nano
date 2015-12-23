package com.example.ekeitho.jokeplatform;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * Created by ekeitho on 12/22/15.
 */
public class JokePlatform extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.platform_main);
        TextView textView = (TextView) findViewById(R.id.jokeView);

        if (getIntent() != null) {
            String joke = getIntent().getStringExtra("com.ekeitho.joke");
            textView.setText(joke);
        }
    }


}

