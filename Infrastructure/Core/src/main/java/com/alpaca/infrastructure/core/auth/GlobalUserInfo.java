package com.alpaca.infrastructure.core.auth;

import lombok.Data;

/**
 * @Author lichenw
 * @Created 2019/5/10 9:10
 */
@Data
public class GlobalUserInfo {
    private String userId;
    private String userName;
    private String openId;

}
