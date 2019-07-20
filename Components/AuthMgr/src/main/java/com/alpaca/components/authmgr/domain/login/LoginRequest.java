package com.alpaca.components.authmgr.domain.login;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRequest  implements Serializable{

    private String loginName;

    private String password;
}