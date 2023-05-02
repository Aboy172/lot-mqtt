package com.cym.lotmqtt.mqtt;

/**
 * @author cym
 */
public interface MsgEncoder<T> {

  /**
   * 数据库消息编码为string
   *
   * @param t 消息
   * @return 编码器
   */
  String encoder(T t);
}