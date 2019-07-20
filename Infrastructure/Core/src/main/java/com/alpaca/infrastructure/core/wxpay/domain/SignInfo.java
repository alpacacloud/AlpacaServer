package com.alpaca.infrastructure.core.wxpay.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Lichenw
 * Created on 2019/6/1
 */
@Data
public class SignInfo implements Serializable {
    /**
     * 小程序ID
     */
    private String appId;
    /**
     * 时间戳
     */
    private String timeStamp;
    /**
     * 随机串
     */
    private String nonceStr;
    /**
     *
     */
    @XStreamAlias("package")
    private String repay_id;
    /**
     * 签名方式
     */
    private String signType;

}
