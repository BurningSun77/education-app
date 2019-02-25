package com.example.cerebral;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class MathlyQuerier extends AsyncTask<String, Void, String> {

    MathlyAPIFetch callback;

    public MathlyQuerier(Context c) {

        callback = (MathlyAPIFetch) c;
    }

    protected String doInBackground(String... params) {

        String output = "";
        for (String query : params) {
            try {

                URL url = new URL(query);

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

        callback.mathlyEvaluateCompleted(result);
    }
}
