package edu.umich.seedforandroid.watson;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import edu.umich.seedforandroid.R;

/**
 * Created by Dominic on 10/22/2014.
 */
public class WatsonManager {

    private static final String TAG = WatsonManager.class.getSimpleName();

    // region constant values

    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_CONTENT_TYPE = AsyncHttpClient.HEADER_CONTENT_TYPE;
    private static final String HEADER_SYNC_TIMEOUT = "X-SyncTimeout";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_VALUE_APPLICATION_JSON = RequestParams.APPLICATION_JSON;
    private static final String HEADER_VALUE_SYNC_TIMEOUT = "30";
    private static final String HEADER_VALUE_CACHE_CONTROL = "no-cache";

    private static final String QUESTION_FORMAT = "{\"question\": {\"questionText\": \"%1$s\"}}";
    // endregion

    //hold strings locally to avoid repeated lookups
    private final String mUser;
    private final String mPassword;
    private final String mUrl;

    private Context mContext;
    private AsyncHttpClient mHttpClient;

    public WatsonManager(Context context) {

        mContext = context;
        mHttpClient = new AsyncHttpClient();

        mUser = mContext.getString(R.string.watson_id);
        mPassword = mContext.getString(R.string.watson_password);
        mUrl = mContext.getString(R.string.watson_server_instance);

        mHttpClient.setBasicAuth(mUser, mPassword);
    }

    public void executeQuery(final String query, final IResponseListener listener) {

        try {

            Header[] headers = new Header[3];
            headers[0] = new BasicHeader(HEADER_ACCEPT, HEADER_VALUE_APPLICATION_JSON);
            headers[1] = new BasicHeader(HEADER_SYNC_TIMEOUT, HEADER_VALUE_SYNC_TIMEOUT);
            headers[2] = new BasicHeader(HEADER_CACHE_CONTROL, HEADER_VALUE_CACHE_CONTROL);

            String question = String.format(QUESTION_FORMAT, query);
            StringEntity content = new StringEntity(question);

            mHttpClient.post(mContext, mUrl, headers, content, HEADER_VALUE_APPLICATION_JSON, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    if (listener != null) {

                        try {

                            JSONObject bestResponse = response.getJSONObject("question").getJSONArray("evidencelist").getJSONObject(0);
                            double confidence = bestResponse.getDouble("value");
                            String text = bestResponse.getString("text");

                            listener.onResponseReceived(text, confidence);
                        }
                        catch (JSONException e) {

                            e.printStackTrace();
                            Log.e(TAG, "JSON Parse error, format from Watson may have changed");
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    if (listener != null) {

                        listener.onErrorReceived(statusCode);
                    }
                }
            });
        }
        catch (UnsupportedEncodingException uee) { /*unexpected*/ uee.printStackTrace(); }
    }

    public interface IResponseListener {

        public void onResponseReceived(String response, double confidence);
        public void onErrorReceived(int httpErrorCode);
    }
}
