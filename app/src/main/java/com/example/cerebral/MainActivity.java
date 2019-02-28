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
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity implements WolframAPIFetch, MathlyAPIFetch, NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    private ProgressBar progressCircle;

    private TextView mv_question;
    private TextView displayCount;
    private TextView url;

    private Button getQRCode;
    private Button[] userChoices = new Button[5];

    private ImageView qrCode;
    private DrawerLayout drawer;

    private JSONInterpreter jsonInterpreter;

    private int winCount;

    private JSONObject jsonObject;
    private String ml_question;

    private String wa_fullQuery;
    private String wa_question;
    private String wa_answer;
    private String[] wa_images;

    private int helpToggle = 0;

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
        displayCount = findViewById(R.id.viewscore);

        drawer = findViewById(R.id.drawer_layout);

        drawer.addDrawerListener(this);
        //ViewCompat.setElevation(drawer, 1000);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setUpViews() {

        mv_question = findViewById(R.id.mv_question);
        url = findViewById(R.id.url);
        progressCircle = findViewById(R.id.progressCircle);
        qrCode = findViewById(R.id.qrCode);

        userChoices[0] = findViewById(R.id.mv_answer1);
        userChoices[1] = findViewById(R.id.mv_answer2);
        userChoices[2] = findViewById(R.id.mv_answer3);
        userChoices[3] = findViewById(R.id.mv_answer4);
        userChoices[4] = findViewById(R.id.mv_answer5);

        for(int i = 0; i< userChoices.length; ++i) {

            userChoices[i].setClickable(true);
           // userChoices[i].setBackgroundColor(getThemeColor(this, android.R.attr.colorAccent));
        }

        getQRCode = findViewById(R.id.getQRCode);
    }

    private void runAPIs() {

        Intent getUrl = getIntent();
        String query = getUrl.getStringExtra("url");

        new MathlyQuerier(this).execute(query);
        progressCircle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {
        if (v>0.05){ViewCompat.setElevation(drawer, 1000);}
        else{ ViewCompat.setElevation(drawer, 0);}
    }

    @Override
    public void onDrawerOpened(View drawerview){
        //super.onDrawerOpened(drawerview);
        ViewCompat.setElevation(drawer, 1000);
    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        ViewCompat.setElevation(drawer, 0);
    }

    @Override
    public void onDrawerStateChanged(int i) {

    }

    @Override
    public void mathlyEvaluateCompleted(String result) {



        try {
            jsonObject = new JSONObject(result);
            jsonInterpreter = new JSONInterpreter(jsonObject);
            ml_question=parseMathMLFull(jsonObject.getString("question"));
            mv_question.setText(ml_question);
            Intent getscore =getIntent();

            winCount = getscore.getIntExtra("score",0);
            displayCount.setText("Win Count: "+Integer.toString(winCount));
            jsonInterpreter.setWinCount(winCount);
            for(int i = 0; i< userChoices.length; ++i) {

                userChoices[i].setText(jsonInterpreter.getNumber(i));
                final int finalI = i;
                userChoices[i].setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        checkAnswer(finalI, jsonInterpreter);
                    }
                });
            }


        } catch (JSONException e) {

            e.printStackTrace();
        }

        new WolframQuerier(this).execute(ml_question);
    }

    @Override
    public void waEvaluateCompleted(String result) {

        wa_fullQuery = result;
        getWAImages();
        getWAQ_A();
        progressCircle.setVisibility(View.GONE);
        getQRCode.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;
        //ViewCompat.setElevation(drawer, 1000);

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


      //for (int i = 0; i< userChoices.length; ++i) {

      //    userChoices[i].setVisibility(View.GONE);
      //}

      //url.setVisibility(View.VISIBLE);

      //String qrapi_call = null;
      //try {

      //    qrapi_call = baseURL + URLEncoder.encode(parseMathML(ml_question), "UTF-8") + appID;
      //    url.setText(qrapi_call);
      //    qrapi_call = "http://api.qrserver.com/v1/create-qr-code/?data=" + URLEncoder.encode(url.getText().toString(), "UTF-8") + "&size=250x250";
      //} catch (UnsupportedEncodingException e) {

      //    e.printStackTrace();
      //}

      //if (qrapi_call != null) {

      //    Picasso.get().load(qrapi_call).into(qrCode);


      //}

        Intent intent = new Intent(this, QRGeneratorActivity.class);

        Intent getUrl = getIntent();
        String query = getUrl.getStringExtra("url");
        intent.putExtra("url",query);
        startActivity(intent);
        finish();

    }

    public void checkAnswer(int i, JSONInterpreter jsonInterpreter) {

        jsonInterpreter.checkAnswer(i);
        winCount = jsonInterpreter.getWinCount();
        finish();
        Intent intent = getIntent();
        intent.putExtra("score", winCount);
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
}
