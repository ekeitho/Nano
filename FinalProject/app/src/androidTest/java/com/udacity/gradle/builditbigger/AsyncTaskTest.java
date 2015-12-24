package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.test.AndroidTestCase;

public class AsyncTaskTest extends AndroidTestCase {

    public void testAsync() {
        MainActivity activity = new MainActivity();
        MainActivity.EndpointsAsyncTask task = activity.getAsyncTask();

        Context context = null;

        task.execute(context);
        String joke = "";
        try {
            joke = task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(!joke.equals(""));
    }
}