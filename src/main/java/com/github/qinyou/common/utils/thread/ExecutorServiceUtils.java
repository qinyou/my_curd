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

    private static int corePoolSize = 5;
    private static int maximumPoolSize = 10;
    private static long keepAliveTime = 0L;   // 单位毫秒
    private static String threadName = "my_curd-%d";

    static {
        log.debug("thread pool init");
        pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNameFormat(threadName).setDaemon(true).build());
    }
}
