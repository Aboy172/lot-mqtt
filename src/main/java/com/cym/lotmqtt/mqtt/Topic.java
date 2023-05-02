package com.cym.lotmqtt.mqtt;


import com.cym.lotmqtt.constant.enums.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * 自定义标记注解
 *
 * @author jie
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Topic {

    /**
     * topic
     */
    String topic() default "";

    /**
     * qos
     */
    int qos() default 0;

    /**
     * 订阅模式
     */
    Pattern patten() default Pattern.NONE;

    /**
     * 共享订阅组
     *
     * @return 订阅组
     */
    String group() default "group1";
}
