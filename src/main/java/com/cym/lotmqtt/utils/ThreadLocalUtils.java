package com.cym.lotmqtt.utils;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * @Author: cym
 * @create: 2023-05-04 01:45
 * @description:
 * @version: 1.0
 **/

public class ThreadLocalUtils {

  public static ExecutorService getExecutorService() {
    int nThreads = Runtime.getRuntime().availableProcessors();

    ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNamePrefix("mqtt-thread-1")
        .build();
    return ExecutorBuilder
        .create()
        .setCorePoolSize(5)
        .setMaxPoolSize(nThreads)
        .setKeepAliveTime(0L)
        .setWorkQueue(new LinkedBlockingQueue<>(1024))
        .setThreadFactory(threadFactory)
        .build();


  }

}
