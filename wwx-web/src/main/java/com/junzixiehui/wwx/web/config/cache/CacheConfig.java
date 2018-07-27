package com.junzixiehui.wwx.web.config.cache;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author: qulibin
 * @description: 缓冲配置
 * @date: 17:30 2018/3/6
 * @modify：
 */
@Configuration
@EnableCaching(proxyTargetClass = true)
public class CacheConfig {

    @Value("${cache.local.cacheNames}")
    public String  localCacheNames;
   /* @Value("${redis.cacheNames}")
    public String  redisCacheNames;*/

  /*  @Resource(name = "redisTemplate")
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;*/

    @Bean
    @Primary
    public CacheManager concurrentMapCacheManager(){
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();
        concurrentMapCacheManager.setCacheNames(Sets.newHashSet(localCacheNames.split(",")));
        return concurrentMapCacheManager;
    }

   /* @Bean
    public CacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setCacheNames(Sets.newHashSet(redisCacheNames.split(",")));
        return redisCacheManager;
    }*/

   /* @Bean
    public CacheManager guavaCacheManager(){
        GuavaCacheManager guavaCacheManager = new GuavaCacheManager();
        CacheBuilder cacheBuild = CacheBuilder.newBuilder().
                expireAfterWrite(10, TimeUnit.SECONDS).
                maximumSize(1000);
        guavaCacheManager.setCacheBuilder(cacheBuild);
        return guavaCacheManager;
    }*/

    @Bean
    public CompositeCacheManager compositeCacheManager(){
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        List<CacheManager> cacheManagerList = Lists.newArrayList();
        cacheManagerList.add(concurrentMapCacheManager());
       /* cacheManagerList.add(redisCacheManager());*/
        //cacheManagerList.add(guavaCacheManager());
        compositeCacheManager.setCacheManagers(cacheManagerList);
        return compositeCacheManager;
    }
}
