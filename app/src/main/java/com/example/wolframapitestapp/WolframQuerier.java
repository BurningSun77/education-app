package com.example.wolframapitestapp;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class WolframQuerier extends AsyncTask<String, Void, String> {

    String baseURL;
    String appID;
    WolframAPIFetch callback;

    public WolframQuerier(Context c) {

        callback = (WolframAPIFetch) c;
        baseURL = callback.getBaseURL();
        appID = callback.getAppID();
    }

    protected String doInBackground(String... params) {

        String output = "";
        for (String query : params) {

            try {

                URL url = new URL(baseURL + URLEncoder.encode(query, "UTF-8") + appID);

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String outLine;
                while ((outLine = in.readLine()) != null) {

                    output += outLine;
                }

                in.close();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return output;
    }
    protected void onPostExecute(String result) {

        callback.onEvaluateCompleted(result);
    }
}
