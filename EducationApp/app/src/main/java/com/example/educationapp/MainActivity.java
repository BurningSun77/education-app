package com.example.educationapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements WolframAPIFetch {

    private TextView field;
    private ProgressBar bar;
    private TextView text;

    private String baseURL = "http://api.wolframalpha.com/v2/query?input=";
    private String appID = "&appid=R3U29Q-EVL4795U7X";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field = findViewById(R.id.field);
        bar = findViewById(R.id.progressBar);
        text = findViewById(R.id.textView);
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
