package edu.umich.seedforandroid.api;

import com.appspot.umichseed.seed.Seed;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

/**
 * Created by Dominic on 10/18/2014.
 */
public class SeedApi {

    private static final JsonFactory JSON_FACTORY = new GsonFactory();

    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    public static Seed getUnauthenticatedApi() {

        Seed.Builder seedBuilder = new Seed.Builder(SeedApi.HTTP_TRANSPORT,
                SeedApi.JSON_FACTORY, null);

        return seedBuilder.build();
    }

    /**
     * The {@link com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential GoogleAccountCredential}
     * returned by {@link edu.umich.seedforandroid.account.GoogleAccountManager GoogleAcountManager} is a suitable (and expected)
     * {@link com.google.api.client.http.HttpRequestInitializer HttpRequestInitializer} for use here.
     */
    public static Seed getAuthenticatedApi(HttpRequestInitializer initializer) {

        Seed.Builder seedBuilder = new Seed.Builder(SeedApi.HTTP_TRANSPORT,
                SeedApi.JSON_FACTORY, initializer);

        return seedBuilder.build();
    }

    private SeedApi(){}
}
