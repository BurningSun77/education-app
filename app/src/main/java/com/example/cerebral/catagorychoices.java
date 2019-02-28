package com.example.cerebral;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class catagorychoices extends AppCompatActivity {

    private int difficulty  = -1;
    private int category    = -1;
    private int subcategory = -1;

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
        public void onClick(DialogInterface dialog, int selection) { difficulty = selection; sendtomainactivity(); }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagorychoices);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        selectCategory();
       // FloatingActionButton fab = findViewById(R.id.fab);
       // fab.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View view) {
       //         Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
       //                 .setAction("Action", null).show();
       //     }
       // });
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
        builder.setTitle("Select a Dificulty");
        builder.setItems(difficulties, difficultyListener);
        builder.setNegativeButton("Cancel", null);
        AlertDialog actions = builder.create();
        actions.show();
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

    public void onBackPressed() {

     startActivity (new Intent(catagorychoices.this, MenuActivity.class));
    }
    private void sendtomainactivity(){

        Intent intent = new Intent(catagorychoices.this, MainActivity.class);
        intent.putExtra("url", getMathlyURL());

        startActivity(intent);

    }

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
