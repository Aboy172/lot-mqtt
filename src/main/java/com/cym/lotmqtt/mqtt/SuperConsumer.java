package com.cym.lotmqtt.mqtt;

import com.cym.lotmqtt.utils.ThreadLocalUtils;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author cym
 */
@Slf4j
public abstract class SuperConsumer<T> implements IMqttMessageListener, MsgDecoder<T> {


    public static ExecutorService executorService = ThreadLocalUtils.getExecutorService();


    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        log.info(
            "\r\n 收到主题 :\r\n" + topic + " 的消息:\r\n" + new String(mqttMessage.getPayload()));
        executorService.submit(() -> {
            try {
                T decoder = decoder(mqttMessage);
                msgHandler(topic, decoder);
            } catch (Exception ex) {
                //解决业务处理错误导致断线问题
                log.error(ex.toString());
            }
        });
    }

    /**
     * 业务操作
     *
     * @param topic  主题
     * @param entity 接收参数
     */
    protected abstract void msgHandler(String topic, T entity);
}
