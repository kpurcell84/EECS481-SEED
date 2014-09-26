package com.example.mypersonalwatsonqa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import javax.net.ssl.X509TrustManager;

public class MainActivity extends Activity{

    private String mWatsonQueryString = "";
    private String mWatsonAnswerString = "";
    private boolean mIsQuerying = false;
    
    private MainActivity mCallbacks;
    private WatsonQuery mQuery;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCallbacks = this;
		mIsQuerying = false;

		
        // Restore answer text if it exists in memory
        if(mWatsonAnswerString.length() > 0) {
            TextView watsonQuestion = (TextView) mCallbacks.findViewById(R.id.watson_answer_text);
            watsonQuestion.setText(mWatsonAnswerString);
        }

        // Event binding for submit button. Grabs text from the watson_question_text field
        mCallbacks.findViewById(R.id.watson_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsQuerying) {
                    mIsQuerying = true;
                    
                    // Grab the value of the watson_question_text field and 
                    // set mWatsonQueryString to the watson_question_text field value
                    EditText watsonQuestion = (EditText) mCallbacks.findViewById(R.id.watson_question_text);
                    if(watsonQuestion.getText() != null) {
                        mWatsonQueryString = watsonQuestion.getText().toString();
                    }
                    
                    // Create new WatsonQuery() <AsyncTask> and call execute on it
                    mQuery = new WatsonQuery();
                    mQuery.execute();
                }
            }
        });		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}

	
	private class WatsonQuery extends AsyncTask<Void, Integer, String>{
        //private SSLContext context;
        private HttpsURLConnection connection;
        private String jsonData;
        
        private String mLogTag = "WatsonQuery";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... ignore) {

            // establish SSL trust (insecure for demo)
/*            try {
                context = SSLContext.getInstance("TLS");
                context.init(null, trustAllCerts, new java.security.SecureRandom());
            } catch (java.security.KeyManagementException e) {
                e.printStackTrace();
            } catch (java.security.NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
*/
        	
            try {
                // Default HTTPS connection option values
                URL watsonURL = new URL(getString(R.string.user_watson_server_instance));
                int timeoutConnection = 30000;
                connection = (HttpsURLConnection) watsonURL.openConnection();
                //connection.setSSLSocketFactory(context.getSocketFactory());
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setConnectTimeout(timeoutConnection);
                connection.setReadTimeout(timeoutConnection);

                // Watson specific HTTP headers
                // From the Watson api documentation, must be a synchronous request and contain the X-SyncTimeout header
                
                // Accept: application/json
                // Content-Type: application/json
                // X-SyncTimeout: 30
                // Authoritzation: Basic username:password
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("X-SyncTimeout", "30");
                connection.setRequestProperty("Authorization", "Basic " + getEncodedValues(getString(R.string.user_id), getString(R.string.user_password)));

                connection.setRequestProperty("Cache-Control", "no-cache");

                
                // Prepare a query for the connection outputstream
                OutputStream out = connection.getOutputStream();
                String query = "{\"question\": {\"questionText\": \"" + mWatsonQueryString + "\"}}";
                Log.i(mLogTag, "Watson Query String: " + query);
                
                out.write(query.getBytes());
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            int responseCode;
            try {
                if (connection != null) {
                    responseCode = connection.getResponseCode();
                    Log.i(mLogTag, "Server Response Code: " + Integer.toString(responseCode));

                    switch(responseCode) {
                        case 200:
                            // successful HTTP response state
                            InputStream input = connection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                            String line;
                            StringBuilder response = new StringBuilder();
                            while((line = reader.readLine()) != null) {
                                response.append(line);
                                response.append('\r');
                            }
                            reader.close();

                            Log.i(mLogTag, "Watson Output: " + response.toString());
                            jsonData = response.toString();

                            break;
                        default:
                            // Do Stuff
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // received data, deliver JSON to PostExecute
            if(jsonData != null) {
                return jsonData;
            }

            // else, hit HTTP error, handle in PostExecute by doing null check
            return null;
        }

        
        // Executes after the query has been posted and uses the json data to 
        // construct the response in the watson_answer_text view
        @Override
        protected void onPostExecute(String json) {
            mIsQuerying = false;


            try {
                if(json != null) {
                    JSONObject watsonResponse = new JSONObject(json);
                    JSONObject question = watsonResponse.getJSONObject("question");
                    JSONArray evidenceArray = question.getJSONArray("evidencelist");
                    JSONObject mostLikelyValue = evidenceArray.getJSONObject(0);
                    mWatsonAnswerString = mostLikelyValue.get("text").toString();
                    TextView textView = (TextView) mCallbacks.findViewById(R.id.watson_answer_text);
                    textView.setText(mWatsonAnswerString);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // No valid answer
            }
        }

        /*
         *  Accepts all HTTPS certs. Do NOT use in production!!!
         */
/*        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};*/
    }

	
	// takes your user_id and user_password string values and puts them
	// into a base64 encoded string together
    private String getEncodedValues(String user_id, String user_password) {
        String textToEncode = user_id + ":" + user_password;
        byte[] data = null;
        try {
            data = textToEncode.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }
}
