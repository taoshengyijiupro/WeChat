package com.shipparts.controller;

import com.shipparts.domain.ArticleItem;
import com.shipparts.domain.InMsgEntity;
import com.shipparts.domain.OutMsgEntity;
import com.shipparts.util.SecurityUtil;
import com.shipparts.util.WeChatUtil;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Date;

@Controller
public class WeChatController {

    /**
     * URL接入校验
     * @return
     */
    @RequestMapping(value="weChat",method = RequestMethod.GET)
    @ResponseBody
    public String validate(String signature,String timestamp,String nonce,String echostr){
        //1）将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = {WeChatUtil.TOKEN,timestamp,nonce};
        Arrays.sort(arr);
        //将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder sb = new StringBuilder();
        for (String temp: arr) {
            sb.append(temp);
        }
        //自己加密的签名
        String mySignature = SecurityUtil.SHA1(sb.toString());
        //3）开发者获得加密后的字符串可与signature对比
        if (mySignature.equals(signature)){
            //请原样返回echostr参数内容，则接入生效，成为开发者成功
            System.out.print("接入成功！");
            return echostr;
        }
        //否则接入失败
        System.out.print("接入失败");
        return null;
    }

    /**
     * 消息处理
     * @return
     */
    @RequestMapping(value="weChat",method = RequestMethod.POST)
    @ResponseBody
    public Object handleMessage(@RequestBody InMsgEntity inMsg){
        OutMsgEntity outMsg = new OutMsgEntity();
        //发送方
        outMsg.setFromUserName(inMsg.getToUserName());
        //接收方
        outMsg.setToUserName(inMsg.getFromUserName());
        //消息创建时间
        outMsg.setCreateTime(new Date().getTime());

        //判断inMsg中的消息类型是文本还是图片
        String msgType = inMsg.getMsgType();

        //回复的信息
        String outContent = null;

        if (msgType.equals("text")) {
            //消息类型
            outMsg.setMsgType("text");
            //用户发送的信息
            String content = inMsg.getContent();

            if (content.contains("你好")) {
                outContent = "11111111111111111111\n" +
                        "222222222222222222222222";
            } else if (content.contains("在干吗")) {
                outContent = "11111111111111111111\n" +
                        "222222222222222222222222222\n" +
                        "3333333333333333333333333333333333";
            }else if (content.contains("我要学习")){
                //回复图文消息
                outMsg.setMsgType("news");
                //设置图文个数
                outMsg.setArticleCount(1);
                //设置图文明细列表
                ArticleItem item = new ArticleItem();
                item.setTitle("Java大神之路（第九季 SpringMVC）");
                item.setPicUrl("http://www.xlinclass.com");
                item.setDescription("java大神之路");
                item.setUrl("http://ahong.xlinclass.com");
                outMsg.setItem(new ArticleItem[]{item});
            }else {
                outMsg.setContent(inMsg.getContent());
            }
            outMsg.setContent(outContent);
        }else if(msgType.equals("image")){
            //消息类型
            outMsg.setMsgType("image");
            outMsg.setMediaId(new String[]{inMsg.getMediaId()});
        }else if(msgType.equals("event")){
            if(inMsg.getEvent().equals("subscribe")){
                //回复普通文本消息
                outMsg.setContent("感谢关注xlinclass！[亲亲]\n" +
                        "\n" +
                        "现在回复【我要学习】" +
                        "\n" +
                        "马上获取课程资料！[奸笑]");
                outMsg.setMsgType("text");
            }
        }
        return outMsg;
    }
}

