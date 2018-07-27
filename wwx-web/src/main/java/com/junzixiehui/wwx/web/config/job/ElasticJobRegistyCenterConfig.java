package com.junzixiehui.wwx.web.config.job;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elastic-job注册中心配置
 *
 * @author zhoutao
 * @date created 2017-11-17
 */
//@Configuration
//@ConditionalOnExpression("'${elasticJob.registryCenter.serverLists}'.length()>0")
public class ElasticJobRegistyCenterConfig {
   /* @Value("${elasticJob.registryCenter.serverLists}")
    private String serverLists;
    @Value("${elasticJob.registryCenter.namespace}")
    private String namespace;
    @Value("${elasticJob.registryCenter.digest}")
    private String digest;*/

   /* @Bean(initMethod = "init")
    public ZookeeperRegistryCenter createRegistryCenter() {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(serverLists, namespace);
        zookeeperConfiguration.setDigest(digest);
        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }*/
}
