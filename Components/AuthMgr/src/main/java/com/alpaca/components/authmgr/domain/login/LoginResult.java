package com.alpaca.components.authmgr.domain.login;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResult implements Serializable{

    private LoginUserInfo userInfo;

    private int loginStatus;

    private String message;

    private String token;

}
