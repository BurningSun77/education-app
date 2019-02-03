package com.example.educationapp;

public interface WolframAPIFetch {
    public void onEvaluateCompleted(String result);
    public String getBaseURL();
    public String getAppID();
}
