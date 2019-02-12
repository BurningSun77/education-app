package com.example.wolframapitestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class mathlyDemo extends AppCompatActivity {

    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;

    private TextView equation;
    private TextView displayCount;

    private int winCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final MathGenerator mathGenerator = new MathGenerator();


        answer1 = findViewById(R.id.button3);
        answer2 = findViewById(R.id.button4);
        answer3 = findViewById(R.id.button5);
        answer4 = findViewById(R.id.button6);
        
        equation = findViewById(R.id.textView2);
        displayCount = findViewById(R.id.textView3);

        answer1.setText(Integer.toString(mathGenerator.getanswer()));
        answer2.setText(Integer.toString(mathGenerator.getf1answer()));
        answer3.setText(Integer.toString(mathGenerator.getf2answer()));
        answer4.setText(Integer.toString(mathGenerator.getf3answer()));

        Intent getScore = getIntent();

        winCount = getScore.getIntExtra("score",0);

        equation.setText((Integer.toString(mathGenerator.getNumber1())+mathGenerator.getoperator()+Integer.toString(mathGenerator.getNumber2())));
        displayCount.setText("Win Count: "+Integer.toString(winCount));

        answer1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                checkansewer(answer1,mathGenerator);
            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                checkansewer(answer2,mathGenerator);
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                checkansewer(answer3,mathGenerator);
            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                checkansewer(answer4,mathGenerator);
            }
        });


    }


    public void checkansewer(Button button,MathGenerator mathGenerator){
        int choice = Integer.parseInt(button.getText().toString());

        if(choice == mathGenerator.getanswer()){

            winCount++;
        }

        finish();
        Intent intent = getIntent();
        intent.putExtra("score", winCount);
        startActivity(getIntent());
    }
}
