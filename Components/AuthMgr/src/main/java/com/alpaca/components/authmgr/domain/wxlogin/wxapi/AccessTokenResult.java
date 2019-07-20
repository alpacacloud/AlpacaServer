package com.alpaca.components.authmgr.domain.wxlogin.wxapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author ：lichenw
 * @Date ：Created in 15:48 2019/3/31
 * @Description：
 * @Modified By：
 */
@Data
public class AccessTokenResult extends WxResponseBase {
    @JsonProperty("expires_in")
    public int expires_in;
    @JsonProperty("access_token")
    public String access_token;
}
