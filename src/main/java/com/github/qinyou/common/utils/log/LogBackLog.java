package com.github.qinyou.common.utils.log;

import com.jfinal.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jfinal 集成 logback
 *
 * @author zhangchuang
 */
public class LogBackLog extends Log {

    public static Logger log;

    public LogBackLog(Class<?> clazz) {
        log = LoggerFactory.getLogger(clazz);
    }

    public LogBackLog(String name) {
        log = LoggerFactory.getLogger(name);
    }

    public static LogBackLog getLog(Class<?> clazz) {
        return new LogBackLog(clazz);
    }

    public static LogBackLog getLog(String name) {
        return new LogBackLog(name);
    }

    @Override
    public void debug(String message) {
        log.debug(message);
    }

    @Override
    public void debug(String message, Throwable t) {
        log.debug(message, t);
    }

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void info(String message, Throwable t) {
        log.info(message, t);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    @Override
    public void warn(String message, Throwable t) {
        log.warn(message, t);
    }

    @Override
    public void error(String message) {
        log.error(message);
    }

    @Override
    public void error(String message, Throwable t) {
        log.error(message, t);
    }

    @Override
    public void fatal(String message) {
        log.error(message);
    }

    @Override
    public void fatal(String message, Throwable t) {
        log.error(message, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return log.isErrorEnabled();
    }
}
