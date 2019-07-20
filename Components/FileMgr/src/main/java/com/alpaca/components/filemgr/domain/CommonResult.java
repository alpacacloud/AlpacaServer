package com.alpaca.components.filemgr.domain;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class CommonResult implements Serializable {

    private static final long serialVersionUID = 5629988944390710057L;

    //默认成功
    private boolean success = true;

    private String msg;

    private Object data;

    private Map<String, Object> extra = new HashMap<>();


    public void putExtra(String key, Object value) {
        this.extra.put(key, value);
    }

    public Object getExtra(String key) {
        return this.extra.get(key);
    }

}
