package com.alpaca.components.filemgr.domain;

public enum ExistsResultType {
    /**
     * 未找到附件原件
     */
    UNFOUNDFILE,
    /**
     * 未找到临时区记录
     */
    UNFOUNDRECORD,
    /**
     * 存在附件记录
     */
    EXISTS,
    /**
     * 非临时文件
     */
    STATEERROR,
    /**
     * 已删除的临时文件
     */
    DELETE,

}