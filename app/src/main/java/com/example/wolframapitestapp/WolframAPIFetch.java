package com.example.wolframapitestapp;

public interface WolframAPIFetch {

    public void waEvaluateCompleted(String result);
    public String getBaseURL();
    public String getAppID();
}
