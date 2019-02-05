package com.example.wolframapitestapp;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public class WolframQuerier extends AsyncTask<String, Void, String> {

    String baseURL;
    String appID;
    WolframAPIFetch callback;

    public WolframQuerier(Context c) {

        callback = (WolframAPIFetch) c;
        baseURL = callback.getBaseURL();
        appID = callback.getAppID();
    }

    public void getWAObject(String input)
    {
        // Use "pi" as the default query, or caller can supply it as the lone command-line argument.
        input = "pi";

        // The WAEngine is a factory for creating WAQuery objects,
        // and it also used to perform those queries. You can set properties of
        // the WAEngine (such as the desired API output format types) that will
        // be inherited by all WAQuery objects created from it. Most applications
        // will only need to crete one WAEngine object, which is used throughout
        // the life of the application.
        WAEngine engine = new WAEngine();

        // These properties will be set in all the WAQuery objects created from this WAEngine.
        engine.setAppID("R3U29Q-EVL4795U7X");
        engine.addFormat("plaintext");

        // Create the query.
        WAQuery query = engine.createQuery();

        // Set properties of the query.
        query.setInput(input);

        try {

            // For educational purposes, print out the URL we are about to send:
            System.out.println("Query URL:");
            System.out.println(engine.toURL(query));
            System.out.println();

            // This sends the URL to the Wolfram|Alpha server, gets the XML result
            // and parses it into an object hierarchy held by the WAQueryResult object.
            WAQueryResult queryResult = engine.performQuery(query);

            if (queryResult.isError()) {

                System.out.println("Query error");
                System.out.println("  error code: " + queryResult.getErrorCode());
                System.out.println("  error message: " + queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {

                System.out.println("Query was not understood; no results available.");
            } else {

                // Got a result.
                System.out.println("Successful query. Pods follow:\n");
                for (WAPod pod : queryResult.getPods()) {

                    if (!pod.isError()) {

                        System.out.println(pod.getTitle());
                        System.out.println("------------");
                        for (WASubpod subpod : pod.getSubpods()) {

                            for (Object element : subpod.getContents()) {

                                if (element instanceof WAPlainText) {

                                    System.out.println(((WAPlainText) element).getText());
                                    System.out.println("");
                                }
                            }
                        }
                        System.out.println("");
                    }
                }
                // We ignored many other types of Wolfram|Alpha output, such as warnings, assumptions, etc.
                // These can be obtained by methods of WAQueryResult or objects deeper in the hierarchy.
            }
        } catch (WAException e) {

            e.printStackTrace();
        }
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
