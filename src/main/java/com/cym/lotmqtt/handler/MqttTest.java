package com.cym.lotmqtt.handler;

import com.alibaba.fastjson.JSON;
import com.cym.lotmqtt.constant.enums.Pattern;
import com.cym.lotmqtt.mqtt.SuperConsumer;
import com.cym.lotmqtt.mqtt.Topic;
import com.cym.lotmqtt.po.Connect;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @Author: cym
 * @create: 2023-05-04 01:25
 * @description:
 * @version: 1.0
 **/
@Topic(topic = "test", patten = Pattern.SHARE)
@Slf4j
public class MqttTest extends SuperConsumer<Connect> {


  @Override
  public Connect decoder(MqttMessage msg) {
    return JSON.parseObject(new String(msg.getPayload()), Connect.class);
  }

  @Override
  protected void msgHandler(String topic, Connect entity) {
    log.info("当前主题是: " + topic);
    log.info("消息内容为:{}", entity.getUserName());
  }
}
