package com.example.wolframapitestapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.github.kexanie.library.MathView;


public class MainActivity extends FragmentActivity implements WolframAPIFetch, MathlyAPIFetch {

    private MathView mv_question;
    private TextView answer;
    private TextView url;
    private ProgressBar progressCircle;
    private ImageView qrCode;

    private MathView mv_answer1;
    private MathView mv_answer2;
    private MathView mv_answer3;
    private MathView mv_answer4;

    private Button help;
    private Button getQRCode;
    private Button solve;
    private Button mathly;

    private int difficulty = -1;
    private int category = -1;
    private int subcategory = -1;

    private JSONObject jsonObject;

    private String wa_fullQuery;
    private String wa_question;
    private String ml_question;
    private String wa_answer;
    private String[] wa_images;

    private String[] choices;

    private int helpToggle = 0;

    DialogInterface.OnClickListener catagoryListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int selection) { category = selection; selectSubcategory(); }
    };

    DialogInterface.OnClickListener subcatagoryListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int selection) { subcategory = selection; selectDifficulty(); }
    };

    DialogInterface.OnClickListener difficultyListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int selection) { difficulty = selection; runAPIs(); }
    };

    CheckForClickTouchLister answer1Listener = new CheckForClickTouchLister() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (super.onTouch(v, event)) {
                try {
                    if (Integer.parseInt(jsonObject.getString("correct_choice")) == 0) {
                        answer.setText("correct!");
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                answer.setText("incorrect!");
                return true;
            }
            return false;
        }
    };

    CheckForClickTouchLister answer2Listener = new CheckForClickTouchLister() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (super.onTouch(v, event)) {
                try {
                    if (Integer.parseInt(jsonObject.getString("correct_choice")) == 1) {
                        answer.setText("correct!");
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                answer.setText("incorrect!");
                return true;
            }
            return false;
        }
    };

    CheckForClickTouchLister answer3Listener = new CheckForClickTouchLister() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (super.onTouch(v, event)) {
                try {
                    if (Integer.parseInt(jsonObject.getString("correct_choice")) == 2) {
                        answer.setText("correct!");
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                answer.setText("incorrect!");
                return true;
            }
            return false;
        }
    };

    CheckForClickTouchLister answer4Listener = new CheckForClickTouchLister() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (super.onTouch(v, event)) {
                try {
                    if (Integer.parseInt(jsonObject.getString("correct_choice")) == 3) {
                        answer.setText("correct!");
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                answer.setText("incorrect!");
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Intent intent = getIntent();
        // Uri data = intent.getData();

        setUpViews();
        selectCategory();
    }

    public void solveClick(View v) {

        String answerText = wa_question + " = " + wa_answer;
        // answer.setText(answerText);
        answer.setText(parseMathMLFull(ml_question));
    }

    public void getQRCodeClick(View v) {

        mv_answer1.setVisibility(View.GONE);
        mv_answer2.setVisibility(View.GONE);
        mv_answer3.setVisibility(View.GONE);
        mv_answer4.setVisibility(View.GONE);

        url.setVisibility(View.VISIBLE);

        String qrapi_call = null;
        try {

            qrapi_call = baseURL + URLEncoder.encode(parseMathML(mv_question.getText()), "UTF-8") + appID;
            url.setText(qrapi_call);
            qrapi_call = "http://api.qrserver.com/v1/create-qr-code/?data=" + URLEncoder.encode(url.getText().toString(), "UTF-8") + "&size=250x250";
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        if (qrapi_call != null) {

            Picasso.get().load(qrapi_call).into(qrCode);
        }
    }

    public void mathlyClick(View v) {

        startActivity(new Intent(MainActivity.this, MathlyGenerator.class));
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
    public void waEvaluateCompleted(String result) {

        wa_fullQuery = result;
        getWAImages();
        getWAQ_A();
        progressCircle.setVisibility(View.GONE);
        help.setVisibility(View.VISIBLE);
        getQRCode.setVisibility(View.VISIBLE);
        solve.setVisibility(View.VISIBLE);
        mathly.setVisibility(View.VISIBLE);
    }

    @Override
    public void mathlyEvaluateCompleted(String result) {

        try {

            jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("choices");
            choices = new String[jsonArray.length()];
            for (int i = 0; i < choices.length; i++) {

                choices[i] = jsonArray.optString(i);
            }
            ml_question = jsonObject.getString("question");
            mv_question.setText(ml_question);
            mv_answer1.setText(choices[0]);
            mv_answer2.setText(choices[1]);
            mv_answer3.setText(choices[2]);
            mv_answer4.setText(choices[3]);
        } catch (JSONException e) {

            e.printStackTrace();
        }

        new WolframQuerier(this).execute(parseMathMLFull(ml_question));
    }

    private void setUpViews() {

        mv_question = findViewById(R.id.mv_question);
        answer = findViewById(R.id.answer);
        url = findViewById(R.id.url);
        progressCircle = findViewById(R.id.progressCircle);
        qrCode = findViewById(R.id.qrCode);

        mv_answer1 = findViewById(R.id.mv_answer1);
        mv_answer1.setClickable(true);
        mv_answer1.setOnTouchListener(answer1Listener);
        mv_answer1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_blue_dark));

        mv_answer2 = findViewById(R.id.mv_answer2);
        mv_answer2.setClickable(true);
        mv_answer2.setOnTouchListener(answer2Listener);
        mv_answer2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_blue_dark));

        mv_answer3 = findViewById(R.id.mv_answer3);
        mv_answer3.setClickable(true);
        mv_answer3.setOnTouchListener(answer3Listener);
        mv_answer3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_blue_dark));

        mv_answer4 = findViewById(R.id.mv_answer4);
        mv_answer4.setClickable(true);
        mv_answer4.setOnTouchListener(answer4Listener);
        mv_answer4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_blue_dark));

        help = findViewById(R.id.help);
        getQRCode = findViewById(R.id.getQRCode);
        solve = findViewById(R.id.solve);
        mathly = findViewById(R.id.mathly);
    }

    private void runAPIs() {

        new MathlyQuerier(this).execute(getMathlyURL());
        progressCircle.setVisibility(View.VISIBLE);
    }

    private String getMathlyURL() {

        String url = "https://math.ly/api/v1/";
        if (category >= 0) {
            if (categories[category] != null) {

                url = url + categories[category].replace(' ', '-') + "/";
                switch (category) {

                    case 0:
                        url = url + arithmetics[subcategory >= 0 ? subcategory : 0].replace(' ', '-') + ".json";
                        break;
                    case 1:
                        url = url + algebras[subcategory >= 0 ? subcategory : 0].replace(' ', '-') + ".json";
                        break;
                    case 2:
                        url = url + calculi[subcategory >= 0 ? subcategory : 0].replace(' ', '-') + ".json";
                        break;
                }
            } else {

                url += "arithmetic/simple-arithmetic.json";
            }
        }
        if (difficulty >= 0 && subcategory < 2) {

            url = (url + "?difficulty=" + difficulties[difficulty]);
        }

        url = url.toLowerCase();

        Log.d("Fetching from Math.ly", url);

        return url;
    }

    private void selectCategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Category");
        builder.setItems(categories, catagoryListener);
        builder.setNegativeButton("Cancel", null);
        AlertDialog actions = builder.create();
        actions.show();
    }

    private void selectSubcategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Subcategory");
        switch (category) {

            case 0:
                builder.setItems(arithmetics, subcatagoryListener);
                break;
            case 1:
                builder.setItems(algebras, subcatagoryListener);
                break;
            case 2:
                builder.setItems(calculi, subcatagoryListener);
                break;
        }
        builder.setNegativeButton("Cancel", null);
        AlertDialog actions = builder.create();
        actions.show();
    }

    private void selectDifficulty() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Dificulty");
        builder.setItems(difficulties, difficultyListener);
        builder.setNegativeButton("Cancel", null);
        AlertDialog actions = builder.create();
        actions.show();
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

    private String parseMathML(@org.jetbrains.annotations.NotNull String m) {

        String result = "";
        while (m.contains(">")) {

            m = m.substring(m.indexOf(">") + 1);
            if (!m.contains("<")) break;
            result += m.substring(0, m.indexOf("<"));
            m = m.substring(m.indexOf("<"));
        }
        return result;
    }

    private String parseMathMLFull(@org.jetbrains.annotations.NotNull String m) {

        String result = "";
        while (m.contains("<")) {

            m = m.substring(m.indexOf("<") + 1);
            String tag = m.substring(0, m.indexOf(">"));
            String root;
            String temp;

            m = m.replace("<mrow>", "").replace("</mrow>", "");

            m = m.substring(m.indexOf(">") + 1);
            switch (tag) {

                case "msqrt":
                    result += "sqrt(";
                    break;
                case "\\/msqrt":
                    result += ")";
                    break;
                case "mroot":
                    root = m.substring(0, m.indexOf("/mroot"));
                    temp = "";
                    while (root.contains(">")) {

                        root = root.substring(root.indexOf(">") + 1);
                        if (!root.contains("<")) break;
                        if (temp.length() == 0) {

                            temp = root.substring(0, root.indexOf("<"));
                        } else {

                            root = root.substring(root.indexOf(">") + 1);
                            temp = root.substring(0, root.indexOf("<")) + "root(" + temp + ")";
                            break;
                        }
                        root = root.substring(root.indexOf("<"));
                    }
                    result += temp;
                    break;
                case "mfrac":
                    m = m.substring(m.indexOf(">") + 1);
                    result += "(" + m.substring(0, m.indexOf("<")) + " / ";
                    m = m.substring(m.indexOf(">") + 1);
                    m = m.substring(m.indexOf(">") + 1);
                    result += m.substring(0, m.indexOf("<")) + ")";
                    break;
                case "msup":
                    m = m.substring(m.indexOf(">") + 1);
                    result += "(" + m.substring(0, m.indexOf("<")) + "^";
                    m = m.substring(m.indexOf(">") + 1);
                    m = m.substring(m.indexOf(">") + 1);
                    result += m.substring(0, m.indexOf("<")) + ")";
                    break;
                case "msub":
                    m = m.substring(m.indexOf(">") + 1);
                    result += m.substring(0, m.indexOf("<"));
                    m = m.substring(m.indexOf(">") + 1);
                    m = m.substring(m.indexOf(">") + 1);
                    result += m.substring(0, m.indexOf("<"));
                    break;
                case "mi":
                case "mn":
                    result += m.substring(0, m.indexOf("<")) + " ";
                    break;
                case "mo":
                    if (m.substring(1, 2).equals("&")) {

                        switch (m.substring(1, m.indexOf("<") - 1)) {

                            case "&Cross;":
                                result += "×";
                                break;
                            case "&#xF7;":
                                result += "÷";
                                break;
                            default:
                                result += m.substring(0, m.indexOf("<"));
                                break;
                        }
                    } else {

                        result += m.substring(0, m.indexOf("<"));
                    }
                    break;
            }
        }
        return result;
    }

    @Override
    public String getBaseURL() {

        return baseURL;
    }

    @Override
    public String getAppID() {

        return appID;
    }

    private final String baseURL = "http://api.wolframalpha.com/v2/query?input=";
    private final String appID = "&appid=R3U29Q-EVL4795U7X";
    private final String[] difficulties = { "Beginner", "Intermediate", "Advanced" };
    private final String[] categories = { "Arithmetic", "Algebra", "Calculus"};
    private final String[] arithmetics = {"Simple", "Fractions",
            "Exponents and Radicals", "Simple Trigonometry", "Matrices" };
    private final String[] algebras = { "Linear Equations", "Equations Containing Radicals",
            "Equations Containing Absolute Values", "Quadratic Equations",
            "Higher Order Polynomial Equations", "Equations Involving Fractions",
            "Exponential Equations", "Logarithmic Equations", "Trigonometric Equations",
            "Matrices Equations" };
    private final String[] calculi = { "Polynomial Differentiation", "Trigonometric Differentiation",
            "Exponents Differentiation", "Polynomial Integration", "Trigonometric Integration",
            "Exponents Integration", "Polynomial Definite Integrals", "Trigonometric Definite Integrals",
            "Exponents Definite Integrals", "First Order Differential Equations", "Second Order Differential Equations"};
}
