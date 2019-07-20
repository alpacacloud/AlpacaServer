package com.alpaca.infrastructure.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class WxmpConfig {
    @Value("${ser.wx.appid}")
    private String appid;
    @Value("${ser.wx.secret}")
    private String secret;

    @Value("${ser.wx.payNotifyUrl}")
    private String payNotifyUrl;

    @Value("${ser.wx.payKey}")
    private String payKey;

    @Value("${ser.wx.paymchId}")
    private String payMchId;
}
