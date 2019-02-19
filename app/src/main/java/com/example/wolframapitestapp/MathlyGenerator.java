package com.example.wolframapitestapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
    private Button camerabutton ;
    private int wincount;
    private String uri;
    private TextView displaycount;
    private int Camera_Permission=1;
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
        camerabutton = findViewById(R.id.buttonC);


        camerabutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MathlyGenerator.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        // Toast.makeText(SimpleScannerActivity.this, "You have already granted this permission!",
                        // Toast.LENGTH_SHORT).show();
                    } else {
                        requestStoragePermission();
                    }
                }





            }

        });


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
        Intent geturi= getIntent();
        uri= geturi.getStringExtra("uri");
        if (uri==null){
            uri=         "https://math.ly/api/v1/algebra/linear-equations.json";
        }


        MathlyQuerier mq = new MathlyQuerier(this);
        mq.execute(uri);




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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    private void requestStoragePermission()

    {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("I need to use the camera in order to see QR codes")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MathlyGenerator.this,
                                    new String[]{Manifest.permission.CAMERA}, Camera_Permission);
                            //thread = new Thread(MathlyGenerator.this);


                            startActivity(new Intent(MathlyGenerator.this, SimpleScannerActivity.class));


                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, Camera_Permission);
        }

    }
}
