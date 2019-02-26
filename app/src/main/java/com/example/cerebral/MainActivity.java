package com.example.cerebral;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
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


public class MainActivity extends AppCompatActivity implements WolframAPIFetch, MathlyAPIFetch, NavigationView.OnNavigationItemSelectedListener {

    private TextView mv_question;
    private TextView url;
    private ProgressBar progressCircle;
    private ImageView qrCode;


    private Button[] userchoices = new Button[5];
    private JSONInterpreter mathlygenerator;


    //private Button help;
    private Button getQRCode;

    private int difficulty  = -1;
    private int category    = -1;
    private int subcategory = -1;
    private int wincount;

    private JSONObject jsonObject;
    private String ml_question;
    private String[] choices;

    private String wa_fullQuery;
    private String wa_question;
    private String wa_answer;
    private String[] wa_images;

    private TextView displaycount;

    private int helpToggle = 0;
    private int winCount   = 0;

    private DrawerLayout drawer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpViews();
        runAPIs();

        Intent getScore = getIntent();

        winCount = getScore.getIntExtra("score",0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        displaycount = findViewById(R.id.viewscore);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {

            case R.id.home_button:
                intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.share_button:
                intent = new Intent(this, QRGeneratorActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.catergory_button:
                intent = new Intent(this, catagorychoices.class);
                startActivity(intent);
                finish();
                break;
            case R.id.scan_button:
                intent = new Intent(this, QRScannerActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.about_button:
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TODOFragment()).commit();
                break;
            case R.id.help_button:
                Bundle bundle = new Bundle();
                bundle.putStringArray("wa_images", wa_images);
                HelpFragment helpFragment = new HelpFragment();
                helpFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, helpFragment).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    public void getQRCodeClick(View v) {


        for(int i=0;i<userchoices.length;++i) {
            userchoices[i].setVisibility(View.GONE);

        }


        url.setVisibility(View.VISIBLE);

        String qrapi_call = null;
        try {

            qrapi_call = baseURL + URLEncoder.encode(parseMathML(ml_question), "UTF-8") + appID;
            url.setText(qrapi_call);
            qrapi_call = "http://api.qrserver.com/v1/create-qr-code/?data=" + URLEncoder.encode(url.getText().toString(), "UTF-8") + "&size=250x250";
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        if (qrapi_call != null) {

            Picasso.get().load(qrapi_call).into(qrCode);
        }
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
       //help.setVisibility(View.VISIBLE);
        getQRCode.setVisibility(View.VISIBLE);
    }

    @Override
    public void mathlyEvaluateCompleted(String result) {



        try {
            jsonObject = new JSONObject(result);
            mathlygenerator = new JSONInterpreter(jsonObject);
            ml_question=parseMathMLFull(jsonObject.getString("question"));
            mv_question.setText(ml_question);
            Intent getscore =getIntent();

            wincount= getscore.getIntExtra("score",0);
            displaycount.setText("Win Count: "+Integer.toString(wincount));
            mathlygenerator.setWinCount(wincount);
            for(int i=0;i<userchoices.length;++i) {

                userchoices[i].setText(mathlygenerator.getNumber(i));
                final int finalI = i;
                userchoices[i].setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        checkansewer(finalI,mathlygenerator);
                    }
                });
            }


        } catch (JSONException e) {

            e.printStackTrace();
        }

        new WolframQuerier(this).execute(parseMathMLFull(ml_question));
    }

    private void setUpViews() {

        mv_question = findViewById(R.id.mv_question);
        url = findViewById(R.id.url);
        progressCircle = findViewById(R.id.progressCircle);
        qrCode = findViewById(R.id.qrCode);

        userchoices[0] = findViewById(R.id.mv_answer1);
        userchoices[1] = findViewById(R.id.mv_answer2);
        userchoices[2] = findViewById(R.id.mv_answer3);
        userchoices[3] = findViewById(R.id.mv_answer4);
        userchoices[4] = findViewById(R.id.mv_answer5);

        for(int i=0;i<userchoices.length;++i) {
            userchoices[i].setClickable(true);
            userchoices[i].setBackgroundColor(getThemeColor(this, android.R.attr.colorAccent));

        }


        //help = findViewById(R.id.help);
        getQRCode = findViewById(R.id.getQRCode);
    }

    private void runAPIs() {
//throw url from intent here
        Intent geturl = getIntent();
        String qurl=geturl.getStringExtra("url");

        new MathlyQuerier(this).execute( qurl);
        progressCircle.setVisibility(View.VISIBLE);
    }



    public void checkansewer(int i, JSONInterpreter mathGeneratorMark2) {

        mathGeneratorMark2.checkAnswer(i);
        wincount = mathGeneratorMark2.getWinCount();
        finish();
        Intent intent = getIntent();
        intent.putExtra("score",wincount);
        startActivity(getIntent());
    }




    private void getWAImages() {

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

    private void getWAQ_A() {

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
                            case "&DifferentialD;":
                                result += "dx/dy";
                            default:
                                result += m.substring(0, m.indexOf("<"));
                                break;
                        }
                    } else {

                        result += m.substring(0, m.indexOf("<"));
                    }
                    break;
                case "/math":
                    result += m.substring(0, m.indexOf("<"));
                    break;
            }
        }
        return result;
    }

    @ColorInt
    public static int getThemeColor(@NonNull final Context context, @AttrRes final int attributeColor) {

        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute (attributeColor, value, true);
        return value.data;
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

}
