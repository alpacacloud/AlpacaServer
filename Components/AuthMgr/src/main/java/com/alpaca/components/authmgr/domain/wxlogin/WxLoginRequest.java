package com.alpaca.components.authmgr.domain.wxlogin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author ：lichenw
 * @Date ：Created in 13:11 2019/3/31
 * @Description：
 * @Modified By：
 */
@Data
public class WxLoginRequest implements Serializable {

    @JsonProperty("code")
    private String code;
    @JsonProperty("iv")
    private String iv;
    @JsonProperty("encryptedData")
    private String encryptedData;
}
