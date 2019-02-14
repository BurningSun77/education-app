package com.example.wolframapitestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import io.github.kexanie.library.MathView;

public class MathlyGenerator extends AppCompatActivity implements MathlyAPIFetch {

    //private TextView equation;
    //private TextView choices;
    private  JSONObject jsonObject;
    private  MathView mathView;
    private  MathGeneratorMark2 mathGeneratorMark2;
    private Button[] choices = new Button[5];
    private int wincount;
    private TextView displaycount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathly);

        //equation = findViewById(R.id.textView5);
        //choices = findViewById(R.id.textView5);
        mathView = findViewById(R.id.formula_one);

        choices[0] = findViewById(R.id.button8);
        choices[1] = findViewById(R.id.button9);
        choices[2] = findViewById(R.id.button10);
        choices[3] = findViewById(R.id.button11);
        choices[4] = findViewById(R.id.button12);
        displaycount = findViewById(R.id.textView6);



        // int start=8;
        // for (int i=0; i<5;++i){

        //     choices[i] = findViewById(R.id.button+start);
        //     start++;
        // }


        // try {
        //     equation.setText(jsonObject.getString("question"));
        // } catch (JSONException e) {
        //     e.printStackTrace();
        // }

        MathlyQuerier mq = new MathlyQuerier(this);
        mq.execute("https://math.ly/api/v1/algebra/linear-equations.json");




    }

    @Override
    public void mathlyEvaluateCompleted(String result) {

       try {
           jsonObject = new JSONObject(result);
           mathGeneratorMark2 = new MathGeneratorMark2(jsonObject);
           mathView.setText(jsonObject.getString("question"));
           Intent getscore =getIntent();

           wincount= getscore.getIntExtra("score",0);
           displaycount.setText("Win Count: "+Integer.toString(wincount));
           mathGeneratorMark2.setWincount(wincount);
           for(int i=0;i<choices.length;++i) {

               choices[i].setText(mathGeneratorMark2.getNumber(i));
               final int finalI = i;
               choices[i].setOnClickListener(new View.OnClickListener() {

                   public void onClick(View v) {

                       checkansewer(finalI,mathGeneratorMark2);
                   }
               });
           }


         // equation.setText(mathGeneratorMark2);
           //JSONArray jsonArray = jsonObject.getJSONArray("choices");
           // int answer = Integer.parseInt( jsonObject.getString("correct_choice"));
          //JSONArray jsonArray = jsonObject.getJSONArray("choices");
          //equation.setText(jsonObject.getString("question"));
          //choices.setText(jsonArray.toString());

       } catch (JSONException e) {

           e.printStackTrace();
       }
   }


    public void checkansewer(int i, MathGeneratorMark2 mathGeneratorMark2) {

        mathGeneratorMark2.checkanser(i);
        wincount = mathGeneratorMark2.getWincount();
        finish();
        Intent intent = getIntent();
        intent.putExtra("score",wincount);
        startActivity(getIntent());
    }
}
