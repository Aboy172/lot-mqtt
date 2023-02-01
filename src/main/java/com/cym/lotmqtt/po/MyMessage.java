package com.cym.lotmqtt.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: cym
 * @create: 2023-01-30 15:05
 * @description: 消息实体类 可序列化
 * @version: 1.0
 **/
@Data
public class MyMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主题
     */
    private String topic;
    /**
     * 内容
     */
    private String content;
    /**
     * 通信质量
     */
    private int qos;

}
