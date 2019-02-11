package com.example.wolframapitestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class mathgenerator2withanAPI extends AppCompatActivity implements MathMLAPIFetch {

    private TextView equation;
    private TextView choices;
   private  JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathgenerator2withan_api);

        equation = findViewById(R.id.textView4);
        choices = findViewById(R.id.textView5);
         new MathMLQuerier(this).execute("");


        try {
            equation.setText(jsonObject.getString("question"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onEvaluateCompleted(String result) {
        try {
           jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
