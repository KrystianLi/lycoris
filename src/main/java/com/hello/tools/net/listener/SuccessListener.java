package com.hello.tools.net.listener;

import org.apache.http.HttpEntity;

import java.io.IOException;

public interface SuccessListener {
    void successListener(HttpEntity entity) throws IOException;
}
