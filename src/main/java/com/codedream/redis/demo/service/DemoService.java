package com.codedream.redis.demo.service;

import com.codedream.redis.function.CheckedCallBack;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DemoService implements CheckedCallBack<String> {

    @Override
    public String get() {
        return "成功";
    }
}
