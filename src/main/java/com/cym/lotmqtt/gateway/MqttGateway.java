package com.cym.lotmqtt.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @Author: cym
 * @create: 2023-01-30 14:52
 * @description: mqtt接口 ，发送消息主要方法定义
 * @version: 1.0
 **/
@MessagingGateway(defaultRequestChannel = "mqttOutputChannel")
@SuppressWarnings("all")
public interface MqttGateway {

    /**
     * 消息发送
     *
     * @param payload 内容
     */
    void sendToMqtt (String payload);

    /**
     * 指定topic进行消息发送
     *
     * @param topic   主题
     * @param payload 内容
     */
    void sendToMqtt (@Header(MqttHeaders.TOPIC) String topic,String payload);

    /**
     * 指定topic以及qps进行消息发送
     *
     * @param topic   主题
     * @param qos     通信质量等级
     * @param payload 内容
     */
    void sendToMqtt (@Header(MqttHeaders.TOPIC) String topic,@Header(MqttHeaders.QOS) int qos,String payload);

    /**
     * 指定topic进行消息发送 -》字节发送拿下
     *
     * @param topic 主题
     * @param qos 通信质量
     * @param payload 内容
     */
    void sendToMqtt (@Header(MqttHeaders.TOPIC) String topic,@Header(MqttHeaders.QOS) int qos,byte[] payload);

}
