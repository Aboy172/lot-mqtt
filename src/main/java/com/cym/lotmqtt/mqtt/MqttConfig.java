package com.cym.lotmqtt.mqtt;

import com.cym.lotmqtt.constant.enums.Pattern;
import com.cym.lotmqtt.po.SubscriptTopic;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

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
    private String hostUrl;
    @Value("${mqtt.username}")
    private String username;
    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.cleanSession}")
    private boolean cleanSession;
    @Value("${mqtt.reconnect}")
    private boolean reconnect;

    @Value("${mqtt.timeout}")
    private int timeout;

    @Value("${mqtt.keepAlive}")
    private int keepAlive;


    /**
     * mqtt客户端工厂公共方法
     *
     * @return {@link MqttPahoClientFactory}
     */
    @Bean
    public MqttConnectOptions getOption() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(cleanSession);
        //断线重连
        options.setAutomaticReconnect(reconnect);
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(timeout);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*10秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(keepAlive);
        return options;
    }

    @Bean
    public MqttClient getClient(MqttConnectOptions options, ApplicationContext applicationContext) throws Exception {
        List<SubscriptTopic> topicMap = new ArrayList<>();
        MqttClient client = new MqttClient(hostUrl, clientId, new MemoryPersistence());
        //得到所有使用@Topic注解的类
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(
            Topic.class);
        for (String className : beansWithAnnotation.keySet()) {
            Class<?> classByteCode = beansWithAnnotation.get(className).getClass();
            //获取类的注解属性
            Topic annotation = AnnotationUtils.findAnnotation(classByteCode, Topic.class);
            if (annotation != null) {
                String topic = annotation.topic();
                int qos = annotation.qos();
                Pattern patten = annotation.patten();
                String group = annotation.group();
                String subTopic = topic;
                if (patten == Pattern.SHARE) {
                    subTopic = "$share/" + group + "/" + topic;
                } else if (patten == Pattern.QUEUE) {
                    subTopic = "$queue/" + topic;
                }
                topicMap.add(new SubscriptTopic(topic, subTopic, patten, qos,
                    (IMqttMessageListener) applicationContext.getBean(classByteCode)));
            }
        }
        client.setCallback(new MqttCallback(topicMap));
        client.connect(options);
        return client;
    }


}

