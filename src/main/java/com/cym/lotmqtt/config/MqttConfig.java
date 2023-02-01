package com.cym.lotmqtt.config;

import com.cym.lotmqtt.constant.MqttConstant;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.util.StringUtils;

/**
 * @Author: cym
 * @create: 2023-01-30 14:08
 * @description: mqtt配置类
 * @version: 1.0
 **/
@Configuration
@Slf4j
public class MqttConfig {

    @Value("${mqtt.serverURIs}")
    private String[] hostUrl;
    @Value("${mqtt.username}")
    private String userName;
    @Value("${mqtt.password}")
    private String passWord;
    @Value("${mqtt.qos}")
    private int qos;
    @Value("${mqtt.client.id}")
    private String clientId;
    @Value("${mqtt.topic}")
    private String[] defaultTopic;

    private static final byte[] WILL_DATA;

    static {
        WILL_DATA = MqttConstant.OFFLINE.getBytes();
    }


    /**
     * mqtt客户端工厂公共方法
     *
     * @return {@link MqttPahoClientFactory}
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory ( ) {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        if (!StringUtils.hasLength(userName)) {
            options.setUserName(userName);
        }

        // 设置连接的密码
        options.setPassword(passWord.toCharArray());
        // 设置代理端的URL地址，可以是多个
        options.setServerURIs(hostUrl);
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(10);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
        // 但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        // 设置"遗嘱"消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
        options.setWill("willTopic",WILL_DATA,2,false);
        // 接受离线消息  告诉代理客户端是否要建立持久会话   false为建立持久会话
        options.setCleanSession(false);
        //设置断开后重新连接
        options.setAutomaticReconnect(true);

        factory.setConnectionOptions(options);
        return factory;
    }


}

