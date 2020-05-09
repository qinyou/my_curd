package com.github.qinyou.common.utils.thread;

import java.util.concurrent.CountDownLatch;

/**
 * 包含计数器 子线程基类
 *
 * @author zhangchuang
 */
public abstract class BaseServiceThread implements Runnable {
    private CountDownLatch latch;
    private String serviceName;
    private boolean serviceUp;

    public BaseServiceThread(String serviceName, CountDownLatch latch) {
        super();
        this.latch = latch;
        this.serviceName = serviceName;
        this.serviceUp = false;
    }

    /**
     * 实际业务类实现
     */
    public abstract void service();

    @Override
    public void run() {
        try {
            service();
            serviceUp = true;
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            serviceUp = false;
        } finally {
            if (latch != null) {
                latch.countDown();
            }
        }
    }


    public String getServiceName() {
        return serviceName;
    }

    public boolean isServiceUp() {
        return serviceUp;
    }
}
