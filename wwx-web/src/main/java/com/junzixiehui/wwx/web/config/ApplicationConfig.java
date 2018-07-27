package com.junzixiehui.wwx.web.config;


import com.junzixiehui.application.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: http配置,存储平台,短信,邮箱配置</p>
 *
 * @author: by qulibin
 * @date: 2017/5/23  13:31
 * @version: 1.0
 */
@Configuration
public class ApplicationConfig {

    @Bean(name = "httpClientUtils")
    public HttpClientUtils httpClientUtils() {
        return new HttpClientUtils();
    }


    /*@Bean(name = "traceHttpClientUtils")
    public HttpClientUtils traceHttpClientUtils() {
       return new HttpClientUtils();
    } */



    @Value("${sms.message_send_url}")
    public String messageSmsUrl;

   /* @Bean(name = "smsClient")
    public SmsClient smsClient() {
        SmsClient smsSendClient = new SmsClient();
        smsSendClient.setHttpClientUtils(traceHttpClientUtils());
        smsSendClient.setSendUrl(messageSmsUrl);
        return smsSendClient;
    }


    @Bean(name = "bootstrap",initMethod = "init")
    public Bootstrap bootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.setPackages(Lists.newArrayList("com.renrenche.aftersale.order"));
        return bootstrap;
    }

*/

}
