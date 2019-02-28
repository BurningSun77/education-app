package com.example.cerebral;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class QRGeneratorActivity extends AppCompatActivity {

    private int difficulty;
    private int category;
    private int subcategory;
    private TextView url;
    private ImageView qrCode;

    private String userURL;

    DialogInterface.OnClickListener categoryListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int selection) { category = selection; selectSubcategory(); }
    };

    DialogInterface.OnClickListener subcategoryListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int selection) { subcategory = selection; selectDifficulty(); }
    };

    DialogInterface.OnClickListener difficultyListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int selection) { difficulty = selection; generateQRcode(); }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);

        qrCode = findViewById(R.id.userqrcode);
        url = findViewById(R.id.url);

        Intent getUrl = getIntent();
        String query = getUrl.getStringExtra("url");

        if(query==null){
        selectCategory();}
        else{
            userURL = query;
            generateQRcode(userURL);
        }
    }


    private void selectCategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Category");
        builder.setItems(categories, categoryListener);
        builder.setNegativeButton("Cancel", null);
        AlertDialog actions = builder.create();
        actions.show();
    }

    private void selectSubcategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Subcategory");
        switch (category) {

            case 0:
                builder.setItems(arithmetics, subcategoryListener);
                break;
            case 1:
                builder.setItems(algebras, subcategoryListener);
                break;
            case 2:
                builder.setItems(calculi, subcategoryListener);
                break;
        }
        builder.setNegativeButton("Cancel", null);
        AlertDialog actions = builder.create();
        actions.show();
    }

    private void selectDifficulty() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Difficulty");
        builder.setItems(difficulties, difficultyListener);
        builder.setNegativeButton("Cancel", null);
        AlertDialog actions = builder.create();
        actions.show();
    }

    private void generateQRcode(){

        switch (category) {

            case 0:
                //builder.setItems(arithmetics, subcategoryListener);
                userURL = baseURL + arithmeticsurl[subcategory]+".json"+"?difficulty=" +difficulties[difficulty];
                break;
            case 1:
                //builder.setItems(algebras, subcategoryListener);
                userURL = baseURL + algebrasurl[subcategory]+".json"+"?difficulty=" +difficulties[difficulty];
                break;
            case 2:
                //builder.setItems(calculi, subcategoryListener);
                userURL = baseURL + calculiurl[subcategory]+".json"+"?difficulty=" +difficulties[difficulty];
                break;
        }

        String qrUrl = null;
        try {

            qrUrl = "http://api.qrserver.com/v1/create-qr-code/?data=" + URLEncoder.encode(userURL, "UTF-8") + "&size=250x250";
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        if (qrUrl != null) {

            Picasso.get().load(qrUrl).into(qrCode);
            url.setText(userURL);
        }
    }


    private void generateQRcode(String userURL){

        String qrUrl = null;
        try {

            qrUrl = "http://api.qrserver.com/v1/create-qr-code/?data=" + URLEncoder.encode(userURL, "UTF-8") + "&size=250x250";
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        if (qrUrl != null) {

            Picasso.get().load(qrUrl).into(qrCode);
            url.setText(userURL);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(QRGeneratorActivity.this, MainActivity.class);
        intent.putExtra("url", userURL);

        startActivity(intent);
    }

    private final String baseURL = "https://math.ly/api/v1/";
    // private final String appID = "&appid=R3U29Q-EVL4795U7X";
    private final String[] difficulties = { "beginner", "intermediate", "advanced" };
    private final String[] categories = { "arithmetic", "algebra", "calculus"};
    private final String[] arithmetics = {"Simple Arithmetic", "Fraction Arithmetic",
            "Exponent & Radicals Arithmetic", "Simple Trigonometry", "Matrices Arithmetic" };
    private final String[] arithmeticsurl = {"arithmetic/simple", "arithmetic/fractions",
            "arithmetic/exponents-and-radicals", "arithmetic/simple-trigonometry", "arithmetic/matrices" };
    private final String[] algebras = { "Linear Equations", "Equations Containing Radicals",
            "Equations Containing Absolute Values", "Quadratic Equations",
            "Higher Order Polynomial Equations", "Equations Involving Fractions",
            "Exponential Equations", "Logarithmic Equations", "Trigonometric Equations",
            "Matrices Equations" };
    private final String[] algebrasurl = { "algebra/linear-equations", "equations-containing-radicals",
            "algebra/equations-containing-absolute-values", "algebra/quadratic-equations",
            "algebra/higherorder-polynomial-equations", "algebra/equations-involving-fractions",
            "algebra/exponential-equations", "algebra/logarithmic-equations", "algebra/trigonometric-equations",
            "algebra/matrices-equations" };
    private final String[] calculi = { "Polynomial Differentiation", "Trigonometric Differentiation",
            "Exponents Differentiation", "Polynomial Integration", "Trigonometric Integration",
            "Exponents Integration", "Polynomial Definite Integrals", "Trigonometric Definite Integrals",
            "Exponents Definite Integrals", "First Order Differential Equations", "Second Order Differential Equations"};
    private final String[] calculiurl = { "calculus/polynomial-differentiation", "calculus/trigonometric-differentiation",
            "calculus/exponents-differentiation", "calculus/polynomial-integration", "calculus/trigonometric-integration",
            "calculus/exponents-integration", "calculus/polynomial-definite-integrals", "calculus/trigonometric-definite-integrals",
            "calculus/exponents-definite-integrals", "calculus/first-order-differential-equations", "calculus/second-order-differential-equations"};
}
