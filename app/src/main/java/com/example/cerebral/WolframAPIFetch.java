package com.example.cerebral;

public interface WolframAPIFetch {

    public void waEvaluateCompleted(String result);
    public String getBaseURL();
    public String getAppID();
}
