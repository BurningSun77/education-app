package com.example.wolframapitestapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class MainActivity extends FragmentActivity implements WolframAPIFetch {

    private TextView question;
    private TextView answer;
    private TextView url;
    private ProgressBar progressCircle;
    private ImageView qrCode;
    private Button exitbutton;

    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;

    private Button help;
    private Button getQRCode;
    private Button solve;

    private WolframQuerier wq;

    private final String baseURL = "http://api.wolframalpha.com/v2/query?input=";
    private final String appID = "&appid=R3U29Q-EVL4795U7X";

    private String wa_fullQuery;
    private String wa_question;
    private String wa_answer;
    private String[] wa_images;

    private int helpToggle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Uri data = intent.getData();
        exitbutton = findViewById(R.id.button7);

        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        url = findViewById(R.id.url);
        progressCircle = findViewById(R.id.progressCircle);
        qrCode = findViewById(R.id.qrCode);

        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        help = findViewById(R.id.help);
        getQRCode = findViewById(R.id.getQRCode);
        solve = findViewById(R.id.solve);

        wq = new WolframQuerier(this);
        wq.execute(question.getText().toString());
        progressCircle.setVisibility(View.VISIBLE);
    }

    public void solveClick(View v) {

        String answerText = wa_question + " = " + wa_answer;
        answer.setText(answerText);
    }

    public void getQRCodeClick(View v) {

        answer1.setVisibility(View.GONE);
        answer2.setVisibility(View.GONE);
        answer3.setVisibility(View.GONE);
        answer4.setVisibility(View.GONE);

        url.setVisibility(View.VISIBLE);

        String qrapi_call = null;
        try {

            qrapi_call = baseURL + URLEncoder.encode(question.getText().toString(), "UTF-8") + appID;
            url.setText(qrapi_call);
            qrapi_call = "http://api.qrserver.com/v1/create-qr-code/?data=" + URLEncoder.encode(url.getText().toString(), "UTF-8") + "&size=250x250";
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        if (qrapi_call != null) {

            Picasso.get().load(qrapi_call).into(qrCode);
        }

        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, mathgenerator2withanAPI.class));
            }
        });
    }

    public void helpClick(View v) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Help");
        ImageView helpImage = new ImageView(this);
        if (wa_images != null) {

            Picasso.get().load(wa_images[helpToggle]).into(helpImage);
            if (++helpToggle == wa_images.length) helpToggle = 0;
        }
        dialogBuilder.setView(helpImage);
        dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) { }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }



    @Override
    public void onEvaluateCompleted(String result) {

        wa_fullQuery = result;
        getWAImages();
        getWAQ_A();
        progressCircle.setVisibility(View.GONE);
        help.setVisibility(View.VISIBLE);
        getQRCode.setVisibility(View.VISIBLE);
        solve.setVisibility(View.VISIBLE);

    }

    private void getWAImages()
    {
        String temp = wa_fullQuery;
        int size = temp.length() - temp.replace("<img src='", "<imgsrc='").length();
        wa_images = new String[size];

        for (int i = 0; i < size; i++) {

            int start = temp.indexOf("<img src='") + 10;
            temp = temp.substring(start);
            int end = temp.indexOf("'");
            wa_images[i] = temp.substring(0, end).replace("amp;", "");
        }
    }

    private void getWAQ_A()
    {
        String[] q_and_a = wa_fullQuery.split("plaintext>");
        int target = 0;
        for (int i = 0; i < q_and_a.length; i++) {
            if (q_and_a[i].length() < 20) {
                if (target++ == 0) {

                    wa_question = q_and_a[i].substring(0, q_and_a[i].length() - 2);
                } else {

                    wa_answer = q_and_a[i].substring(0, q_and_a[i].length() - 2);
                    break;
                }
            }
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
