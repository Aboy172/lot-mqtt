package com.cym.lotmqtt.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author cym
 */
@Component
public class SpringBeanUtils implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  /**
   * 获取Bean对象
   *
   * @param name  bean名称
   * @param clazz bean类型
   * @param <T>   bean类型
   * @return Bean对象
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    return applicationContext.getBean(name, clazz);
  }

  /**
   * 获取Bean对象
   *
   * @param clazz bean类型
   * @param <T>   bean类型
   * @return Bean对象
   */
  public static <T> T getBean(Class<T> clazz) {
    return applicationContext.getBean(clazz);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringBeanUtils.applicationContext = applicationContext;
  }

}
