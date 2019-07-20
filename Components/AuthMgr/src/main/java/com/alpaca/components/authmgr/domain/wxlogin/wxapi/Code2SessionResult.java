package com.alpaca.components.authmgr.domain.wxlogin.wxapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author ：lichenw
 * @Date ：Created in 14:00 2019/3/31
 * @Description：
 * @Modified By：
 */
@Data
public class Code2SessionResult extends WxResponseBase {
    @JsonProperty("openid")
    private String openid;
    @JsonProperty("session_key")
    private String session_key;
    @JsonProperty("unionid")
    private String unionid;

}
