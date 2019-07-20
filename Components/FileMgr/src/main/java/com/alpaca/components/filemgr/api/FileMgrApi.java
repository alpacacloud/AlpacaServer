package com.alpaca.components.filemgr.api;

import com.alpaca.components.filemgr.domain.AttachmentPathDefine;
import com.alpaca.components.filemgr.domain.ExistsResultType;
import com.alpaca.components.filemgr.entity.Attachment;

import java.util.List;

/**
 * @author Lichenw
 * Created on 2019/3/25
 */
public interface FileMgrApi {

    /**
     * exists file in SWAP area
     * @param id
     * @return
     */
    ExistsResultType existsFile(String id);

    /**
     * save file info, and move to formal area
     * @param path
     * @param bizId
     * @param fileId
     * @return
     */
    Attachment saveAttachment(AttachmentPathDefine path, String bizId, String fileId);

    /**
     * 获取附件对象
     * @param Id
     * @return
     */
    Attachment getAttachment(String Id);

    /**
     * 合成图片
     *
     * @param path
     * @param attachmentIds 以第一张图片作为底图，顺序叠加其他图片
     * @return
     */
    Attachment margePhoto(AttachmentPathDefine path,String bizId, List<String> attachmentIds);

    /**
     * 获取文件的物理路径
     * @param fileId
     * @return
     */
    String getActualPath(String fileId);
}
