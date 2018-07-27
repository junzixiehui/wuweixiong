package com.junzixiehui.wwx.web.config.monitor;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * 公司自动部署脚本规定的server.address=0.0.0.0的限制使得spring boot admin client无法注册到远端admin server
 * 此类获取真实ip地址在注册前定义需要注册的三个地址managmentUrl、serverUrl、healthUrl
 */
@Configuration
@Slf4j
public class SpringBootAdminClientPropertiesExtConfig {



    /**
     * 获取第一块网卡ip地址
     *
     * @return
     * @throws SocketException
     */
    private String getRealIp() throws SocketException {
        List<String> ipList = Lists.newArrayList();
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                    ipList.add(inetAddress.getHostAddress());
                }
            }
        }
        return ipList.get(0);
    }

}
