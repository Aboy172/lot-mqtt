package com.cym.lotmqtt.config;

import com.cym.lotmqtt.constant.MqttConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * mqtt入站消息处理配置
 * @author cym
 */
@Configuration
@Slf4j
public class MqttInConfig {

    @Value("${mqtt.qos}")
    private int qos;
    @Value("${mqtt.client.id}")
    private String clientId;
    @Value("${mqtt.topic}")
    private String defaultTopic;





    /**
     * 对于当前应用来讲，接收的mqtt消息的生产者。将生产者绑定到mqtt入站通道，即通过入站通道进入的消息为生产者生产的消息。
     * 可创建多个消息生产者，对应多个不同的消息入站通道，同时生产者监听不同的topic
     */
    @Bean
    public MessageProducer channelInbound (MessageChannel mqttInputChannel,MqttPahoClientFactory factory) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId + "-in",
                        factory,defaultTopic);
        adapter.setCompletionTimeout(5000);
        // defaultPahoMessageConverter.setPayloadAsBytes(true); // 发送默认按字节类型发送消息
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(qos);
        adapter.setOutputChannel(mqttInputChannel);
        return adapter;
    }
    /**
     * mqtt消息入站通道，订阅消息后消息进入的通道。
     * 可创建多个入站通道，对应多个不同的消息生产者。
     *
     * @return {@link MessageChannel}
     */
    @Bean
    public MessageChannel mqttInputChannel ( ) {
        return new DirectChannel();
    }

    /**
     * mqtt入站消息处理工具，对于指定消息入站通道接收到生产者生产的消息后处理消息的工具。
     *
     * @return {@link MessageHandler MessageHandler}
     */
    @Bean
    @ServiceActivator(inputChannel = MqttConstant.CHANNEL_NAME_IN)
    public MessageHandler mqttMessageHandler ( ) {
        return message -> {
            String topic = String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
            String payload = String.valueOf(message.getPayload());
            log.info("接收到 mqtt消息，主题:{} 消息:{}",topic,payload);
        };
    }

}

