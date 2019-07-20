package com.alpaca.infrastructure.core.wxpay.domain;

import lombok.Data;

/**
 * @author Lichenw
 * Created on 2019/6/15
 */
@Data
public class SignInfoResponse extends SignInfo {
    private String paySign;
}
