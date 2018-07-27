package com.junzixiehui.wwx.web.config.mail;

import org.springframework.context.annotation.Configuration;


@Configuration
public class MailConfig {


   /* @Value("${mail.aliyun.host}")
    private String aliHost;
    @Value("${mail.aliyun.port}")
    private int aliPort;
    @Value("${mail.aliyun.username}")
    private String username;
    @Value("${mail.aliyun.password}")
    private String password;
    @Value("${mail.aliyun.smtp.auth}")
    private String aliSmtpAuth;
    @Value("${mail.aliyun.smtp.enable}")
    private String aliSmtpStarttlsEnable;
    @Value("${mail.aliyun.smtp.required}")
    private String aliSmtpStarttlsRequired;
    @Value("${mail.aliyun.smtp.timeout}")
    private String timeout;

    @Value("${mail.aliyun_spare.host}")
    private String aliSpareHost;
    @Value("${mail.aliyun_spare.port}")
    private int aliSparePort;
    @Value("${mail.aliyun_spare.username}")
    private String username1;
    @Value("${mail.aliyun_spare.password}")
    private String password1;
    @Value("${mail.aliyun_spare.smtp.auth}")
    private String aliSpareSmtpAuth;
    @Value("${mail.aliyun_spare.smtp.enable}")
    private String aliSpareSmtpStarttlsEnable;
    @Value("${mail.aliyun_spare.smtp.required}")
    private String aliSpareSmtpStarttlsRequired;
*/

    /*@Bean(name = "javaMailSenderAli")
    public JavaMailSender getSenderAli() {
        return getJavaMailSender(username, aliHost, aliPort, password, aliSmtpAuth, aliSmtpStarttlsEnable, aliSmtpStarttlsRequired, timeout);
    }


    @Bean(name = "javaMailSenderAliSpare")
    public JavaMailSender getSenderAliSpare() {
        return getJavaMailSender(username1, aliSpareHost, aliSparePort, password1, aliSpareSmtpAuth, aliSpareSmtpStarttlsEnable, aliSpareSmtpStarttlsRequired, timeout);
    }

    private JavaMailSender getJavaMailSender(String username1, String aliSpareHost, int aliSparePort, String password1, String aliSpareSmtpAuth, String aliSpareSmtpStarttlsEnable, String aliSpareSmtpStarttlsRequired, String timeout) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(username1);
        javaMailSender.setHost(aliSpareHost);
        javaMailSender.setPort(aliSparePort);
        javaMailSender.setPassword(password1);
        javaMailSender.setDefaultEncoding("UTF-8");
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", aliSpareSmtpAuth);
        props.setProperty("mail.smtp.starttls.enable", aliSpareSmtpStarttlsEnable);
        props.setProperty("mail.smtp.starttls.required", aliSpareSmtpStarttlsRequired);
        props.setProperty("mail.smtp.timeout", timeout);
        javaMailSender.setJavaMailProperties(props);
        return javaMailSender;
    }*/

}