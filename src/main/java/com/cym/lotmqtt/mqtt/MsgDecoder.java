package com.cym.lotmqtt.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author cym
 */
public interface MsgDecoder<T> {

  /**
   * 下位机消息解码器
   *
   * @param msg 消息
   * @return 解码器
   */
  T decoder(MqttMessage msg);
}