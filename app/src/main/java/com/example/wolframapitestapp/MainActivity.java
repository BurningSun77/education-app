package com.example.wolframapitestapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAImage;
import com.wolfram.alpha.impl.WAImageImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity implements WolframAPIFetch {

    private TextView field;
    private ProgressBar bar;
    private TextView text;
    private ImageView sampleimage;
    private Button exitbutton;

    private String baseURL = "http://api.wolframalpha.com/v2/query?input=";
    private String appID = "&appid=R3U29Q-EVL4795U7X";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field = findViewById(R.id.field);
        bar = findViewById(R.id.progressBar);
        text = findViewById(R.id.textView);
        sampleimage = findViewById(R.id.imageView);
        exitbutton = findViewById(R.id.button7);

        // final TextView userinput = (TextView) findViewById(R.id.inputquery);
        // userinput.setText(R.string.app_name);

        final Button button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // final StringBuilder sb = new StringBuilder(userinput.length());
                // sb.append(userinput);
                // String finalinput = sb.toString();

                EditText mEdit = (EditText) findViewById(R.id.inputquery);
                String finalinput = mEdit.getText().toString();


                if (finalinput != null) {

                    // Picasso.get().load(generateURL(finalinput)).fit().centerInside().into(sampleimage);
                    // Image image = ImageApi.call("what flights are overhead?", new DefaultImageParameters().setAppId("DEMO"));

                    String URL = null;
                    try {

                        URL = "http://api.qrserver.com/v1/create-qr-code/?data=" + URLEncoder.encode(finalinput, "UTF-8") + "&size=250x250";
                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();
                    }
                    if (URL != null) {

                        Picasso.get().load(URL).into(sampleimage);
                    }
                }
            }
        });

        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, mathgeneratordemo.class));
            }
        });
    }

    public void buttonClick(View v) {

        bar.setVisibility(View.VISIBLE);
        text.setText("");

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(field.getWindowToken(), 0);

        String query = field.getText().toString();

        WolframQuerier cc = new WolframQuerier(this);
        cc.execute(query);
    }



    @Override
    public void onEvaluateCompleted(String result) {

        bar.setVisibility(View.GONE);
        String[] results = result.split("plaintext>");

        int temp = 0;
        for (String s : results) {

            if (s.length() < 20) {

                s = s.substring(0, s.length() - 2);

                if (temp == 0) {

                    text.setText(s);
                } else {

                    text.setText(text.getText() + " = " + s);
                }
                temp++;
            }
            if (temp == 2) break;
        }
    }

    @Override
    public String getBaseURL() {
        return baseURL;
    }

    @Override
    public String getAppID() {
        return appID;
    }
}

//    Picasso.get().load("http://api.qrserver.com/v1/create-qr-code/?data=HelloWorld!&size=100x100").into(sampleimage);
//    public static String generateURL(String userinput)
//    {
//        // PUT YOUR APPID HERE:
//        String appid = "XXXX";
//
//        WAEngine engine = new WAEngine();
//
//        // These properties will be set in all the WAQuery objects created from this WAEngine.
//        engine.setAppID(appid);
//        engine.addFormat("plaintext");
//        // Create the query.
//        WAQuery query = engine.createQuery();
//
//        // Set properties of the query.
//        query.setInput(userinput);
//
//
//
//        return engine.toURL(query);
//    }

