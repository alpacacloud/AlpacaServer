package com.alpaca.components.filemgr.service.impl;

import com.alpaca.components.filemgr.entity.Attachment;
import com.alpaca.components.filemgr.mapper.AttachmentDao;
import com.alpaca.components.filemgr.service.AttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 附件表 服务实现类
 * </p>
 *
 * @author lichenw
 * @since 2019-03-26
 */
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentDao, Attachment> implements AttachmentService {

}
