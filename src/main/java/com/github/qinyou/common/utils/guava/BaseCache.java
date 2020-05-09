package com.github.qinyou.common.utils.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

import java.util.concurrent.TimeUnit;

/**
 * google guava 缓存工具
 *
 * @author chuang
 * @date 2018/9/3
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class BaseCache<K, V> {
    private final LoadingCache<K, V> cache;


    public BaseCache() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) {
                        return loadData(k);
                    }
                });
    }

    /**
     * 超时缓存：数据写入缓存超过一定时间自动刷新
     *
     * @param duration 超时时间值
     * @param timeUtil 时间单位
     */
    public BaseCache(long duration, TimeUnit timeUtil) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(duration, timeUtil)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) {
                        return loadData(k);
                    }
                });
    }

    /**
     * 限容缓存：缓存数据个数不能超过maxSize
     *
     * @param maxSize 缓存最大个数
     */
    public BaseCache(long maxSize) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) {
                        return loadData(k);
                    }
                });
    }

    /**
     * 权重缓存：缓存数据权重和不能超过maxWeight
     *
     * @param maxWeight                   最大权重
     * @param weigher：权重函数类，需要实现计算元素权重的函数
     */
    public BaseCache(long maxWeight, Weigher<K, V> weigher) {
        cache = CacheBuilder.newBuilder()
                .maximumWeight(maxWeight)
                .weigher(weigher)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) {
                        return loadData(k);
                    }
                });
    }

    public LoadingCache<K, V> getCache() {
        return cache;
    }

    /**
     * 缓存数据加载方法
     *
     * @param k 初始加载参数
     * @return 初始值
     */
    protected abstract V loadData(K k);

    /**
     * 从缓存获取数据
     *
     * @param param 缓存key
     * @return 缓存值
     */
    public V getCache(K param) {
        return cache.getUnchecked(param);
    }

    /**
     * 清除缓存数据，缓存清除后，数据会重新调用load方法获取
     *
     * @param k
     */
    public void refresh(K k) {
        cache.refresh(k);
    }

    /**
     * 主动设置缓存数据
     *
     * @param k 缓存key
     * @param v 缓存value
     */
    public void put(K k, V v) {
        cache.put(k, v);
    }

}
