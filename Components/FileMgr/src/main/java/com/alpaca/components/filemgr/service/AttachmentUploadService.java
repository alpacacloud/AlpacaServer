package com.alpaca.components.filemgr.service;

import com.alpaca.components.filemgr.domain.CommonResult;

import javax.servlet.http.HttpServletRequest;

public interface AttachmentUploadService {

    /**
     * 附件存储到交换区
     * 生成附件记录， 标记为暂存记录
     */
    CommonResult upload(HttpServletRequest servletRequest) throws Exception;


}
