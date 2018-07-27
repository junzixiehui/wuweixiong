package com.junzixiehui.wwx.web.config.db;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

;

/**
 * @author: qulibin
 * @description:
 * @date: 17:52 2017/12/7
 * @modify：
 */
@Getter
@Setter
@ToString
public  class DataSourceSettings {
    protected String driverClassName;
    protected String url;
    protected String username;
    protected String password;
}
