package com.example.wolframapitestapp;

public interface WolframAPIFetch {

    public void onEvaluateCompleted(String result);
    public String getBaseURL();
    public String getAppID();
}
