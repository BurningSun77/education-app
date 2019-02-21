package com.example.wolframapitestapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UserQRgenerator extends AppCompatActivity {

    private int difficulty;
    private int category;
    private int subcategory;
    private ImageView qrCode;

    private String userURL;

    DialogInterface.OnClickListener catagoryListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int selection) { category = selection; selectSubcatagory();}
    };

    DialogInterface.OnClickListener subcatagoryListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int selection) { subcategory = selection; selectDifficulty();}
    };

    DialogInterface.OnClickListener difficultyListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int selection) { difficulty = selection; generateQRcode();}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qrgenerator);
        qrCode = findViewById(R.id.userqrcode);

        selectCatagory();
    }


    private void selectCatagory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Catagory");
        builder.setItems(categories, catagoryListener);
        builder.setNegativeButton("Cancel", null);
        AlertDialog actions = builder.create();
        actions.show();
    }

    private void selectSubcatagory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Subcatagory");
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

    private void generateQRcode(){

        switch (category) {

            case 0:
                //builder.setItems(arithmetics, subcatagoryListener);
                userURL = baseURL + arithmeticsurl[subcategory]+".json"+"?difficulty=" +difficulties[difficulty];
                break;
            case 1:
                //builder.setItems(algebras, subcatagoryListener);
                userURL = baseURL + algebrasurl[subcategory]+".json"+"?difficulty=" +difficulties[difficulty];
                break;
            case 2:
                //builder.setItems(calculi, subcatagoryListener);
                userURL = baseURL + calculiurl[subcategory]+".json"+"?difficulty=" +difficulties[difficulty];
                break;
        }

        String QRurl = null;
        try {
            QRurl = "http://api.qrserver.com/v1/create-qr-code/?data=" + URLEncoder.encode(userURL, "UTF-8") + "&size=250x250";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (QRurl != null) {

            Picasso.get().load(QRurl).into(qrCode);
        }
    };




    private final String baseURL = "https://math.ly/api/v1/algebra/";
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
