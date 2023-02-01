package com.cym.lotmqtt.config;

import com.cym.lotmqtt.constant.MqttConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @Author: cym
 * @create: 2023-01-30 17:04
 * @description: mqtt出站消息处理配置类
 * @version: 1.0
 **/
@Configuration
public class MqttOutConfig {


    @Value("${mqtt.client.id}")
    private String clientId;
    @Value("${mqtt.topic}")
    private String defaultTopic;

    /**
     * mqtt消息出站通道，用于发送出站消息
     *
     * @return {@link MessageChannel}
     */
    @Bean
    public MessageChannel mqttOutputChannel() {
        return new DirectChannel();
    }

    /**
     * mqtt消息出站通道默认配置，用于向外发出mqtt消息
     *
     * @return {@link MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = MqttConstant.CHANNEL_NAME_OUT)
    public MessageHandler mqttOutbound(MqttPahoClientFactory factory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId + "-out", factory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(2);
        messageHandler.setDefaultTopic(defaultTopic);
        return messageHandler;
    }

}
