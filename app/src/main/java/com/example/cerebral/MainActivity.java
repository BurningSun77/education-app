package com.example.cerebral;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements WolframAPIFetch, MathlyAPIFetch, NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    View fragment_container;

    private ProgressBar progressCircle;

    private TextView mv_question;
    private TextView displayCount;

    private Button share;
    private Button[] userChoices = new Button[5];

    private DrawerLayout drawer;

    private JSONInterpreter jsonInterpreter;

    private int winCount;

    private JSONObject jsonObject;
    private String ml_question;

    private String wa_fullQuery;
    private String[] wa_images;

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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setUpViews() {

        fragment_container = findViewById(R.id.fragment_container);

        mv_question = findViewById(R.id.mv_question);
        progressCircle = findViewById(R.id.progressCircle);

        userChoices[0] = findViewById(R.id.mv_answer1);
        userChoices[1] = findViewById(R.id.mv_answer2);
        userChoices[2] = findViewById(R.id.mv_answer3);
        userChoices[3] = findViewById(R.id.mv_answer4);
        userChoices[4] = findViewById(R.id.mv_answer5);

        for (int i = 0; i< userChoices.length; ++i) {

            userChoices[i].setClickable(true);
        }

        share = findViewById(R.id.share);
    }

    private void runAPIs() {

        Intent getUrl = getIntent();
        String query = getUrl.getStringExtra("url");

        new MathlyQuerier(this).execute(query);
        progressCircle.setVisibility(View.VISIBLE);
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
        progressCircle.setVisibility(View.GONE);
        share.setVisibility(View.VISIBLE);
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
                ViewCompat.setElevation(fragment_container, 0);
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TODOFragment()).commit();
                break;
            case R.id.help_button:
                Bundle bundle = new Bundle();
                bundle.putStringArray("wa_images", wa_images);
                HelpFragment helpFragment = new HelpFragment();
                helpFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, helpFragment).commit();
                ViewCompat.setElevation(fragment_container, 1000);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {

    }

    @Override
    public void onDrawerOpened(@NonNull View view){

        ViewCompat.setElevation(drawer, 1000);
    }

    @Override
    public void onDrawerClosed(@NonNull View view) {

        ViewCompat.setElevation(drawer, 0);
    }

    @Override
    public void onDrawerStateChanged(int i) {

        if (i == DrawerLayout.STATE_DRAGGING || i == DrawerLayout.STATE_SETTLING)
            ViewCompat.setElevation(drawer, 1000);
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
                                result += "dy/dx";
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
