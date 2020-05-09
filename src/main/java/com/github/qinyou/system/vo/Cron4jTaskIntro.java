package com.github.qinyou.system.vo;

/**
 * 定时任务 信息 vo
 * @author chuang
 */
public class Cron4jTaskIntro {

    private String name;        // 任务名
    private String cron;        // cron表达式
    private String className;   // task class 名
    private String daemon;      // 是否守护进程
    private String enable;      // 是否启用

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDaemon() {
        return daemon;
    }

    public void setDaemon(String daemon) {
        this.daemon = daemon;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
}
