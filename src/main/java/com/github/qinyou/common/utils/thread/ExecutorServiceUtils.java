package com.github.qinyou.common.utils.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池容器，供其它工具使用
 *
 * @author chuang
 */
@Slf4j
public class ExecutorServiceUtils {
    public static ExecutorService pool;

    static {
        log.debug("thread pool init");
        int corePoolSize = 5;
        int maximumPoolSize = 10;
        long keepAliveTime = 0L;
        String threadName = "my_curd-%d";

        pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNameFormat(threadName).setDaemon(true).build());
    }
}
