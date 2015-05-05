package com.example.odunayo.mapplus;

/**
 * Created by Odunayo on 4/16/2015.
 */
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;

public class ServerCom  {

    public String serverResponse;
    private String sendToServer;
    private String returnFromServer;
    private String origin;
    private String destination;
    private Context context;



    public ServerCom(Context c, String origin, String destination){
        context = c;
        this.origin = origin;
        this.destination = destination;


    }


    // Send a String to the server and return the response of that request
    public String sendToServer(Context c, String origin, String destination,String input) throws ExecutionException, InterruptedException {
        context = c;

        sendToServer = input;
        Log.d("Input to Server", sendToServer);

        // sendToServer = message;
        SimpleHttpRequest request = new SimpleHttpRequest(sendToServer);
        String s = request.execute().get();


        if (serverResponse == null)
            Log.d("Server Response is Null", "Null");



        return s;
    }

    public String response()
    {
        return serverResponse;
    }

    // Private nested class that allows a simple http request asynchronously
    private class SimpleHttpRequest extends AsyncTask<String, String, String> {

        HttpClient httpclient;
        HttpPost httppost;
        String responseString;
        String send;
        private String origin;
        private String destination;


        public SimpleHttpRequest(String send) {
         //   this.origin = origin;
          //  this.destination = destination;
            this.send = send;
        }

        protected void onPreExecute() {

        }

        protected String doInBackground(String...params) {
            String sendMessage = sendToServer;
            try {

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://grantstorey.com/TigerMapBackend/serverCom/serverSocket.php");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

              //  String send = "dir;" + origin + ";" + destination;
               // String send = "find;" + "0;" + "Frist";
                Log.d("Sent", send);
                nameValuePairs.add(new BasicNameValuePair("action", send));


                // Add data
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request and save the server response
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseString  = httpclient.execute(httppost, responseHandler);

                Log.d("RESPONSE", responseString);
             //   setResponse(responseString);

            } catch (ClientProtocolException e) {
                Log.d("CLIENTEXCEPTION", "client exception");

            } catch (IOException e) {
                Log.d("IOEXCEPTION", "io exception");
            }
            return responseString;
        }

        // Set the sendToServer string to the adequate response
        protected void onPostExecute(String result) {
            serverResponse = responseString;
         //   setResponse(serverResponse);

        }


    }

    // Empty constructor
    public ServerCom() {
    }



}