package com.alpaca.components.authmgr.domain.wxlogin;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author ：lichenw
 * @Date ：Created in 10:24 2019/5/25
 * @Description：
 * @Modified By：
 */
@Data
public class WxLoginResult implements Serializable {
    public String token;
}
