package com.example.cerebral;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void selectClick(View v) {

        Intent intent = new Intent(this, catagorychoices.class);
        startActivity(intent);
        finish();
    }

    public void scanClick(View v) {

        Intent intent = new Intent(this, QRScannerActivity.class);
        startActivity(intent);
        finish();
    }

    public void generateClick(View v) {

        Intent intent = new Intent(this, QRGeneratorActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
