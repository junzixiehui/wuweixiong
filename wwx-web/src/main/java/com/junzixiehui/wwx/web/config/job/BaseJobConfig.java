package com.junzixiehui.wwx.web.config.job;


import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author yangjunbing
 * @description:
 * @date created in 16:43 2017/11/24
 */
@Configuration
public class BaseJobConfig {
    /**
     * 注册中心实例
     */
  /*  @Resource
    public ZookeeperRegistryCenter registryCenter;

    *//**
     * 是否开启任务执行失效转移，开启表示如果作业在一次任务执行中途宕机，允许将该次未完成的任务在另一作业节点上补偿执行
     *//*
    private boolean failover = true;
    *//**
     * 是否开启错过任务重新执行
     *//*
    private boolean misfire = true;


    public LiteJobConfiguration createJobConfiguration(Class jobClass, String jobName, String cron, int shardingTotalCount) {
        // 定义作业核心配置
        JobCoreConfiguration.Builder jobCoreConfigBuilder = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount);
        jobCoreConfigBuilder.failover(failover);
        jobCoreConfigBuilder.misfire(misfire);
        JobCoreConfiguration simpleCoreConfig = jobCoreConfigBuilder.build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, jobClass.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        return simpleJobRootConfig;
    }*/
}
