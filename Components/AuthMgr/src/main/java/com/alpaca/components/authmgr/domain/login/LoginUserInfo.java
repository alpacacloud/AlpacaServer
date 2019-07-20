package com.alpaca.components.authmgr.domain.login;

import com.alpaca.components.authmgr.entity.Privileges;
import com.alpaca.components.authmgr.entity.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LoginUserInfo implements Serializable {

    private String persionName;

    private List<Role> roles;

    private List<Privileges> privileges;
}
