package com.cym.lotmqtt.mqtt;

import com.cym.lotmqtt.constant.enums.Pattern;
import com.cym.lotmqtt.po.SubscriptTopic;
import com.cym.lotmqtt.utils.SpringBeanUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/**
 * @author cym
 */
@Slf4j
@Data
@AllArgsConstructor
public class MqttCallback implements MqttCallbackExtended {

    private List<SubscriptTopic> topicMap;


    /**
     * 客户端断开后触发
     *
     * @param throwable 异常
     */
    @SneakyThrows
    @Override
    public void connectionLost(Throwable throwable) {
        MqttClient client = SpringBeanUtils.getBean(MqttClient.class);
        MqttConnectOptions option = SpringBeanUtils.getBean(MqttConnectOptions.class);
        while (!client.isConnected()) {
            log.info("emqx重新连接....................................................");
            client.connect(option);
            Thread.sleep(1000);
        }
    }

    /**
     * 客户端收到消息触发
     *
     * @param topic   主题
     * @param message 消息
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        for (SubscriptTopic subscriptTopic : topicMap) {
            if (subscriptTopic.getPattern() != Pattern.NONE && isMatched(subscriptTopic.getTopic(),
                topic)) {
                subscriptTopic.getMessageListener().messageArrived(topic, message);
                break;
            }
        }
    }

    /**
     * 检测一个主题是否为一个通配符表示的子主题
     *
     * @param topicFilter 通配符主题
     * @param topic       子主题
     * @return 是否为通配符主题的子主题
     */
    private boolean isMatched(String topicFilter, String topic) {
        return MqttTopic.isMatched(topicFilter, topic);
    }

    /**
     * 发布消息成功
     *
     * @param token token
     */
    @SneakyThrows
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        String[] topics = token.getTopics();
        for (String topic : topics) {
            log.info("向主题：" + topic + "发送数据");
        }
    }

    /**
     * 连接emq服务器后触发
     *
     * @param reconnect true 自动重连 false 不自动重连
     * @param serverUri 连接服务器url
     */
    @SneakyThrows
    @Override
    public void connectComplete(boolean reconnect, String serverUri) {
        MqttClient client = SpringBeanUtils.getBean(MqttClient.class);
        if (client.isConnected()) {
            for (SubscriptTopic sub : topicMap) {
                client.subscribe(sub.getSubTopic(), sub.getQos(), sub.getMessageListener());
                log.info("订阅主题:" + sub.getSubTopic());
            }
            log.info("共订阅:   " + topicMap.size() + "   个主题!");
        }
    }
}
