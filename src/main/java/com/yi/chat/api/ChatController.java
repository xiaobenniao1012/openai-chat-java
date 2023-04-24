package com.yi.chat.api;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class ChatController {

    @GetMapping("/chat/prompt")
    public String prompt(String question) throws Exception {

        OkHttpClient client = new OkHttpClient();

        //"messages": [{"role": "user", "content": "Hello!"}]
        List<Map<String,String>> messages=new ArrayList<>();
        Map<String,String> message=new HashMap<>();
        message.put("role","user");
        message.put("content",question);
        messages.add(message);

        // 设置请求参数，包括OpenAI API Key、模型ID、输入文本等
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        Map<String, Object> param=new HashMap<>();
        param.put("model","gpt-3.5-turbo");
        param.put("messages",messages);

        RequestBody body = RequestBody.create(type, JSON.toJSONString(param));
        // 构建请求对象
        Request request = new Request.Builder()
                .url("https://flyhorse.fun/v1/chat/completions")
                .addHeader("Authorization", "Bearer sk-xxxxxx")
                .post(body)
                .build();

        // 发送请求并获取响应

        String responseBody =null;

        //利用try with resouces
        try ( Response response = client.newCall(request).execute()){
            if(!response.isSuccessful()){
                throw new Exception("unexpected code "+response);
            }
            responseBody = response.body().string();
        }

        // 解析响应并输出结果
        System.out.println(responseBody);

        return responseBody;
    }
}
