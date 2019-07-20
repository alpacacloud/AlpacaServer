package com.alpaca.infrastructure.core.wxpay.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Lichenw
 * Created on 2019/6/1
 */
@Data
public class PayResponseInfo implements Serializable {
    private String return_code;
    private String return_msg;
    private String result_code;
    private String appid;
    private String mch_id;
    private String nonce_str;
    private String sign;
    private String prepay_id;
    private String trade_type;
}
