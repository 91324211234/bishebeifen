package com.example.test_app1;

public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
