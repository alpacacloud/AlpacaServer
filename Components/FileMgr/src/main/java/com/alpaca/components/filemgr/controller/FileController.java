package com.alpaca.components.filemgr.controller;

import com.alpaca.components.filemgr.api.FileMgrApi;
import com.alpaca.components.filemgr.domain.AttachmentPathDefine;
import com.alpaca.components.filemgr.domain.CommonResult;
import com.alpaca.components.filemgr.domain.ExistsResultType;
import com.alpaca.components.filemgr.entity.Attachment;
import com.alpaca.components.filemgr.service.AttachmentUploadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/filemgr/attachment")
public class FileController {

    @Autowired
    private AttachmentUploadService attachmentUploadService;

    /**
     * 上传临时文件，并返回临时文件Id,便于后续业务操作
     *
     * @param servletRequest
     * @return
     */
    @PostMapping(value = "/uploadTmpFile")
    @ApiOperation("以列表集合批量上传多个文件。文件暂时存放在临时库，需要在后续操作中调用 commitToStore 方法，提交文件到正式仓库。")
    public CommonResult uploadTmpFile(HttpServletRequest servletRequest) throws Exception {
        return attachmentUploadService.upload(servletRequest);
    }

    @Autowired
    private FileMgrApi fileMgrApi;

    @PostMapping(value = "/tow")
    public Attachment tow(String fileId){
        AttachmentPathDefine define = new AttachmentPathDefine("ACD");
        define.setChildPath("BCD").setChildPath("CCD");

        if(fileMgrApi.existsFile(fileId) == ExistsResultType.EXISTS){
            return fileMgrApi.saveAttachment(define, "aaa", fileId) ;
        }
        return null;
    }
}
