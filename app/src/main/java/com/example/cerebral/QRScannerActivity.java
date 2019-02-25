package com.example.cerebral;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {

        super.onCreate(state);
        mScannerView =new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.

        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onResume() {

        super.onResume();
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onPause() {

        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    @Override
    public void onStop() {

        super.onStop();
        mScannerView.stopCameraPreview();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        // Do something with the result here
        //Log.v(TAG, rawResult.getText()); // Prints scan results
        // Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        Intent intentback = new Intent(QRScannerActivity.this, MathlyGenerator.class);
        intentback.putExtra("uri",rawResult.getText());

        startActivity(intentback);
        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }


}

