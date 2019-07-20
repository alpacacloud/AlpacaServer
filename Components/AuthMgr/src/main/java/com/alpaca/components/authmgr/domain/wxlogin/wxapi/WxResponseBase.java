package com.alpaca.components.authmgr.domain.wxlogin.wxapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author ：lichenw
 * @Date ：Created in 15:47 2019/3/31
 * @Description：
 * @Modified By：
 */
@Data
public class WxResponseBase implements Serializable {
    @JsonProperty("errcode")
    private int errcode;
    @JsonProperty("errmsg")
    private String errmsg;
}
