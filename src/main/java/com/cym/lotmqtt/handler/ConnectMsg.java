package com.cym.lotmqtt.handler;

import com.alibaba.fastjson.JSON;
import com.cym.lotmqtt.constant.enums.Pattern;
import com.cym.lotmqtt.mqtt.SuperConsumer;
import com.cym.lotmqtt.mqtt.Topic;
import com.cym.lotmqtt.po.Connect;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * @author cym
 */
@Topic(topic = "$SYS/brokers/+/clients/+/connected", patten = Pattern.SHARE)
@Slf4j
public class ConnectMsg extends SuperConsumer<Connect> {

    @Override
    protected void msgHandler(String topic, Connect entity) {
        log.info("当前主题是: " + topic);
    }

    @Override
    public Connect decoder(MqttMessage msg) {
        return JSON.parseObject(new String(msg.getPayload()), Connect.class);
    }
}
