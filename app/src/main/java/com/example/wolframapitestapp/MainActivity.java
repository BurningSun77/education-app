package com.example.wolframapitestapp;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAImage;
import com.wolfram.alpha.impl.WAImageImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
ImageView sampleimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sampleimage = (ImageView)findViewById(R.id.imageView);

       // final TextView userinput = (TextView) findViewById(R.id.inputquery);
    //    userinput.setText(R.string.app_name);



      final Button button = findViewById(R.id.button);
      button.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                       //final StringBuilder sb = new StringBuilder(userinput.length());
                                       //sb.append(userinput);
                                       //String finalinput = sb.toString();
                                        EditText mEdit = (EditText)findViewById(R.id.inputquery);
                                        String finalinput = mEdit.getText().toString();


                                        if(finalinput!=null){

                                              //Picasso.get().load(generateURL(finalinput)).fit().centerInside().into(sampleimage);
                                                  //  Image image = ImageApi.call("what flights are overhead?", new DefaultImageParameters().setAppId("DEMO"));;
                                            String URL=null;
                                            try {
                                                URL = "http://api.qrserver.com/v1/create-qr-code/?data="+ URLEncoder.encode(finalinput,"UTF-8")+"&size=250x250";

                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            if(URL!=null) {
                                                Picasso.get().load(URL).into(sampleimage);
                                            }

                                        }


                                    }
                                });
        //Picasso.get().load("http://api.qrserver.com/v1/create-qr-code/?data=HelloWorld!&size=100x100").into(sampleimage);

    }





//    public static String generateURL(String userinput)
//    {
//// PUT YOUR APPID HERE:
//        String appid = "XXXX";
//
//        WAEngine engine = new WAEngine();
//
//        // These properties will be set in all the WAQuery objects created from this WAEngine.
//        engine.setAppID(appid);
//        engine.addFormat("plaintext");
//// Create the query.
//        WAQuery query = engine.createQuery();
//
//        // Set properties of the query.
//        query.setInput(userinput);
//
//
//
//return engine.toURL(query);
//    }
}

