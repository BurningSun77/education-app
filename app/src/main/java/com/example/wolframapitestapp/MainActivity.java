package com.example.wolframapitestapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity implements WolframAPIFetch {

    private TextView question;
    private TextView answer;
    private TextView url;
    private ProgressBar progressCircle;
    private ImageView qrCode;

    private final String baseURL = "http://api.wolframalpha.com/v2/query?input=";
    private final String appID = "&appid=R3U29Q-EVL4795U7X";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Uri data = intent.getData();

        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        url = findViewById(R.id.url);
        progressCircle = findViewById(R.id.progressCircle);
        qrCode = findViewById(R.id.qrCode);
    }

    public void solveClick(View v) {

        progressCircle.setVisibility(View.VISIBLE);
        answer.setText("");

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(question.getWindowToken(), 0);

        String query = question.getText().toString();

        WolframQuerier cc = new WolframQuerier(this);
        cc.execute(query);
        // cc.getWAObject(query);
    }

    public void getQRCodeClick(View v) {

        String finalinput = null;
        try {

            finalinput = baseURL + URLEncoder.encode(question.getText().toString(), "UTF-8") + appID;
            url.setText(finalinput);
            finalinput = "http://api.qrserver.com/v1/create-qr-code/?data=" + URLEncoder.encode(url.getText().toString(), "UTF-8") + "&size=250x250";
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        if (finalinput != null) {

            Picasso.get().load(finalinput).into(qrCode);
        }
    }

    @Override
    public void onEvaluateCompleted(String result) {

        progressCircle.setVisibility(View.GONE);
        String[] results = result.split("plaintext>");

        int temp = 0;
        for (String s : results) {

            if (s.length() < 20) {

                s = s.substring(0, s.length() - 2);

                if (temp == 0) {

                    answer.setText(s);
                } else {

                    String text = answer.getText() + " = " + s;
                    answer.setText(text);
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
//        return engine.toURL(query);
//    }

