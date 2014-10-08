package edu.umich.seedforandroid.api;

import com.appspot.umichseed.seed.Seed;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

/**
 * Created by Dominic on 10/8/2014.
 */
public class ApiManager {

    private Seed.Builder mBuilder;

    public ApiManager() {

        mBuilder = new Seed.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
    }
}
