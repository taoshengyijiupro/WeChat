package com.shipparts.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class WeChatUtil {

    public static final String TOKEN = "taoshengyijiu";

    public static final String APPID = "wx2909377e4f6b69be";

    public static final String APPSECRET = "8ba7e816e843534a10b75eaafbab64c5";

    //创建菜单的接口
    public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    //获取基本accessToken的接口
    public static final String GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    //删除菜单的接口
    public static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    //发送模板消息的接口
    public static final String SEND_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    //获取网页授权accessToken的接口
    public static final String GET_WEB_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    //获取用户信息的接口
    public static final String GET_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    //页面使用jssdk的凭据
    public static final String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    public static String accessToken;//调用基础接口的凭据

    public static long expiresTime;//凭据的失效时间

    /**
     * 获取基本的AccessToken凭据
     * @return
     */
    public static String getAccessToken(){
        //当accessToken为null或者失效才重新去获取
        if(accessToken==null||new Date().getTime()>expiresTime){
            String result = HttpUtil.get(GET_ACCESSTOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET));
            JSONObject jsonObject = JSONObject.parseObject(result);
            //凭据
            accessToken = jsonObject.getString("access_token");
            //有效期
            Long expires_in = jsonObject.getLong("expires_in");
            //设置凭据的失效时间 = 当前时间+有效期
            expiresTime = new Date().getTime()+((expires_in-60)*1000);
        }
        return accessToken;
    }

    /**
     * 创建菜单
     */
    public static void createMenu(String menuJson){
        String result = HttpUtil.post(CREATE_MENU_URL.replace("ACCESS_TOKEN",getAccessToken()), menuJson);
        System.out.print(result);
    }

    /**
     * 删除菜单
     */
    public static void deleteMenu(){
        String result = HttpUtil.get(DELETE_MENU_URL.replace("ACCESS_TOKEN", getAccessToken()));
        System.out.println(result);
    }

    /**
     * 发送模板
     *
     */
    public static void sendTemplate(String data){
        String result = HttpUtil.post(SEND_TEMPLATE_URL.replace("ACCESS_TOKEN", getAccessToken()),data);
        System.out.println(result);
    }

    /**
     * 获取网页授权的AccessToken凭据
     * @return
     */
    public static JSONObject getWebAccessToken(String code){
        String result = HttpUtil.get(GET_WEB_ACCESSTOKEN_URL.replace("APPID", APPID).replace("SECRET", APPSECRET).replace("CODE", code));
        JSONObject json = JSONObject.parseObject(result);
        return json;
    }

    /**
     * 获取用户信息
     *
     */
    public static JSONObject getUserInfo(String accessToken,String openId){
        String result = HttpUtil.get(GET_USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID",openId));
        JSONObject json = JSONObject.parseObject(result);
        return json;
    }

    /**
     * 获取JSSDK的Ticket
     */
    public static String getTicket(){
        //发起请求到指定的接口
        String result = HttpUtil.get(GET_TICKET_URL.replace("ACCESS_TOKEN",getAccessToken()));
        JSONObject json = JSONObject.parseObject(result);
        System.out.println(json);
        return json.getString("ticket");
    }

    public static void main(String[] args) {
        String menuJson = " {\n" +
                "\t\"button\": [{\n" +
                "\t\t\t\"type\": \"click\",\n" +
                "\t\t\t\"name\": \"今日歌曲\",\n" +
                "\t\t\t\"key\": \"V1001_TODAY_MUSIC\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"菜单\",\n" +
                "\t\t\t\"sub_button\": [{\n" +
                "\t\t\t\t\t\"type\": \"view\",\n" +
                "\t\t\t\t\t\"name\": \"搜索\",\n" +
                "\t\t\t\t\t\"url\": \"http://www.soso.com/\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"type\": \"click\",\n" +
                "\t\t\t\t\t\"name\": \"赞一下我们\",\n" +
                "\t\t\t\t\t\"key\": \"V1001_GOOD\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";

        String date = "      {\n" +
                "           \"touser\":\"owgro1UovbhmWFctQzyUA_9Apvt0\",\n" +
                "           \"template_id\":\"uHu_NupDqI16HfuHt7WZleItFIp-X_Rp3gQ8-klt1U8\",\n" +
                "           \"url\":\"http://weixin.qq.com/download\",  \n" +
                "           \"data\":{\n" +
                "                   \"first\": {\n" +
                "                       \"value\":\"恭喜你购买成功！\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"product\":{\n" +
                "                       \"value\":\"巧克力\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"amount\": {\n" +
                "                       \"value\":\"39.8元\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"time\": {\n" +
                "                       \"value\":\"2014年9月22日\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   },\n" +
                "                   \"remark\":{\n" +
                "                       \"value\":\"欢迎再次购买！\",\n" +
                "                       \"color\":\"#173177\"\n" +
                "                   }\n" +
                "           }\n" +
                "       }";
        //createMenu(menuJson);

        //deleteMenu();

        //sendTemplate(date);

        getTicket();
    }
}
