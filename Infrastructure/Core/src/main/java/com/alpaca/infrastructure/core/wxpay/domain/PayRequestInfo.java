package com.alpaca.infrastructure.core.wxpay.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Lichenw
 * Created on 2019/6/1
 */
@Data
public class PayRequestInfo implements Serializable {

    /**
     * 小程序ID
     */
    private String appid;
    /**
     * 商户号
     */
    private String mch_id;
    /**
     * 随机字符串
     */
    private String nonce_str;
    /**
     * 签名类型
     */
    private String sign_type;
    /**
     * 商品描述
     */
    private String sign;
    /**
     * 商品描述
     */
    private String body;
    /**
     * 商户订单号
     */
    private String out_trade_no;
    /**
     * 标价金额 ,单位为分
     */
    private int total_fee;
    /**
     * 终端IP
     */
    private String spbill_create_ip;
    /**
     * 通知地址
     */
    private String notify_url;
    /**
     * 交易类型
     */
    private String trade_type;
    /**
     * 用户标识
     */
    private String openid;

}
