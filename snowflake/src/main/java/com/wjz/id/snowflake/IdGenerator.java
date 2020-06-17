package com.wjz.id.snowflake;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 可以为单例以保证snowflake为单例
 */
public class IdGenerator {

    private long workerId = 0; // 最大值 2^5，机器Id
    private long datacenterId = 1; // 最大值 2^5，机房Id
    private Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId); // 保证单例

    /**
     * 1毫秒最大并发数为4096
     * nextId = time | dataCenterId | workerId | sequence
     * @return
     */
    public synchronized long getId() {
        return snowflake.nextId();
    }

    public synchronized long getId(long workerId, long datacenterId) {
        Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        IdGenerator idGenerator = new IdGenerator();
        System.out.println(idGenerator.getId());
//        System.out.println(idGenerator.getId(5, 5));

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            executor.submit(() -> {
                System.out.println(idGenerator.getId());
            });
        }
        executor.shutdown();

    }
}
