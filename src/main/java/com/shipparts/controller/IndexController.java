package com.shipparts.controller;

import com.alibaba.fastjson.JSONObject;
import com.shipparts.util.WeChatUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Controller
public class IndexController {

    @RequestMapping(value="/index")
    public String index(){
        return "index";
    }

    @RequestMapping("person")
    public String person(String code,Model model){

        //通过code来换取access_token
        JSONObject json = WeChatUtil.getWebAccessToken(code);
        System.out.println(json);
        //获取网页授权access_token凭据
        String webAccessToken = json.getString("access_token");
        //获取用户openid
        String openid = json.getString("openid");
        //通过access_token和openid拉取用户信息
        JSONObject userInfo = WeChatUtil.getUserInfo(webAccessToken, openid);
        System.out.println(userInfo);
        //获取json对象中的键值对集合
        Set<Map.Entry<String, Object>> entries = userInfo.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            model.addAttribute(entry.getKey(),entry.getValue());
        }
        //access_token是和用户有关的，当获取一个access_token之后A，再去调用getUserInfo方法，获取的数据就是A
        //access_token是2个小时内有效,但重复调用都是A
        //再来一个用户进行网页授权，这次获取到的access_token是最新的，之前获取的会失效
        //当access_token失效后可以使用refresh_token重新获取access_token
        //问题：每个用户都有自己的refresh_token? 重复获取之前的refresh_token会失效
        return "person";
    }
}
