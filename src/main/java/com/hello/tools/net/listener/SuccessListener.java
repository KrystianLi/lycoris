package com.hello.tools.net.listener;

import org.apache.http.HttpEntity;

public interface SuccessListener {
    void successListener(HttpEntity entity);
}
