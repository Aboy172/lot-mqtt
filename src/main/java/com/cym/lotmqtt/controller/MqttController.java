package com.cym.lotmqtt.controller;

import com.cym.lotmqtt.gateway.MqttGateway;
import com.cym.lotmqtt.po.MyMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: cym
 * @create: 2023-01-30 15:00
 * @description: mqtt请求转发类
 * @version: 1.0
 **/
@RestController
public class MqttController {
    @Resource
    private MqttGateway mqttGateway;

    /**
     *
     * @param myMessage 请求内容体
     * @return 提示发送成功消息
     */
    @PostMapping("/send")
    public String send(@RequestBody MyMessage myMessage){
        mqttGateway.sendToMqtt(myMessage.getTopic(),myMessage.getQos(), myMessage.getContent());
        return "send topic" + myMessage.getTopic()+"，message： "+ myMessage.getContent();
    }

}
