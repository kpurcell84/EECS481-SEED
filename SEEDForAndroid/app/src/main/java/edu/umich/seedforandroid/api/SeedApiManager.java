package edu.umich.seedforandroid.api;

import com.appspot.umichseed.seed.Seed;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;

/**
 * Created by Dominic on 10/8/2014.
 */
public class SeedApiManager {

    private Seed mApi;

    public SeedApiManager(GoogleAccountCredential credential) {

        Seed.Builder builder = new Seed.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential);
        mApi = builder.build();
    }

    public Seed getApi() {

        return mApi;
    }
}
