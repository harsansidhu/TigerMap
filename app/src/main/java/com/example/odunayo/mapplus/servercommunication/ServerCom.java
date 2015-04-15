package com.example.odunayo.mapplus.servercommunication;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerCom  {

    private String serverResponse;
    private String sendToServer;

    // Private nested class that allows a simple http request asynchronously
    private class SimpleHttpRequest extends AsyncTask<String, String, String> {

        HttpClient httpclient;
        HttpPost httppost;
        String responseString;

        protected void onPreExecute() {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://grantstorey.com/TigerMapBackend/harsanTest.php");
        }

        protected String doInBackground(String...params) {
            String sendMessage = sendToServer;
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("action", sendMessage));

                // Add data
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request and save the server response
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseString  = httpclient.execute(httppost, responseHandler);

                Log.d("RESPONSE", responseString);

            } catch (ClientProtocolException e) {
                Log.d("CLIENTEXCEPTION", "client exception");

            } catch (IOException e) {
                Log.d("IOEXCEPTION", "io exception");
            }
            return null;
        }

        // Set the sendToServer string to the adequate response
        protected void onPostExecute(String result) {
            setResponse();

        }

        public void setResponse()
        {
            serverResponse = responseString;
        }
    }

    // Empty constructor
    public ServerCom() {
    }

    // Send a String to the server and return the response of that request
    public String sendToServer(String message) {
        final HttpClient httpclient = new DefaultHttpClient();
        final HttpPost httppost = new HttpPost("http://grantstorey.com/TigerMapBackend/harsanTest.php");

        sendToServer = message;
        SimpleHttpRequest test  = new SimpleHttpRequest();
        test.execute();

        return serverResponse;
    }

}