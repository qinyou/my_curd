package com.github.qinyou.common.utils.Id;

/**
 * SnowflakeIdWorker id 生成算法
 * 1. 充份运用位运算提高效率
 * 2. 灵活配置，在节点数、毫秒内并发、使用年限 根据实际业务灵活配置
 */
@SuppressWarnings("FieldCanBeLocal")
public class SnowflakeIdWorker {
    private final long workerId;                  // 工作机器ID(0 ~ 2的workerIdBits次方 - 1)
    private final long dataCenterId;              // 数据中心ID(0 ~ 2的dataCenterIdBits次方 - 1)

    // long 总共 64 位，保证整数，使用 63位。 时间戳差 + [workerIdBits+dataCenterIdBits] + [sequenceBits]
    // 时间戳差 所占位数 决定 此算法能使用 年数
    // workerIdBits + dataCenterIdBits 组合决定了节点可部署数量
    // sequenceBits 决定每毫秒内并发数量
    // 此三部分可以调整，根据实际情况配置
    // 此配置 有效期 69年，1024 个节点， 每毫秒内最大并发 4096
    private final long workerIdBits = 5L;      // 机器id所占的位数
    private final long dataCenterIdBits = 5L;  // 数据标识id所占的位数
    private final  long sequenceBits = 12L;

    private final  long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;  // 22
    private final  long dataCenterIdShift = sequenceBits + workerIdBits;  // 17
    private final long workerIdShift = sequenceBits; //12

    // 开始时间截 (2015-01-01)，一旦使用生成了数据，轻易不要更改。
    private final long twepoch = 1420041600000L;
    private long sequence = 0L;                // 毫秒内序列(0~4095)
    private long lastTimestamp = -1L;          // 上次生成ID的时间截

    /**
     * 构造函数
     * @param workerId     工作ID
     * @param dataCenterId 数据中心ID
     */
    SnowflakeIdWorker(long workerId, long dataCenterId) {
        long maxWorkerId = ~(-1L << workerIdBits); // -(-1 * 2的workerIdBits次方 + 1)
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id 取值范围 为  0 到 %d", maxWorkerId));
        }
        long maxDataCenterId = ~(-1L << dataCenterIdBits);
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenter Id  取值范围 为  0 到 %d  ", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 获得下一个ID
     * @return SnowflakeId
     */
    synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("系统时钟回退，暂停生成Id %d 毫秒", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            //  1毫秒内 需生成多个id， 追加排序号

            // 生成序列的掩码，这里为4095
            long sequenceMask = ~(-1L << sequenceBits); // 同 1L << sequenceBits - 1, 但纯位运算可能效率更高
            // 很精妙的算法，0~4095 正常取值，一旦sequence + 1 = 4096，变为 0
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }


    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }
}
