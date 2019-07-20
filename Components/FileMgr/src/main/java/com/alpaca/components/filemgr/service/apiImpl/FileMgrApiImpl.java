package com.alpaca.components.filemgr.service.apiImpl;

import com.alibaba.fastjson.JSON;
import com.alpaca.infrastructure.core.config.FileSystemConfig;
import com.alpaca.infrastructure.core.exception.ServiceException;
import com.alpaca.infrastructure.core.utils.DataUtil;
import com.alpaca.infrastructure.core.utils.GuidHelper;
import com.alpaca.infrastructure.core.utils.LocalDateHelper;
import com.alpaca.infrastructure.core.utils.file.FileHelper;
import com.alpaca.infrastructure.core.utils.file.Multipart;
import com.alpaca.infrastructure.core.utils.file.PicBuilder;
import com.alpaca.components.filemgr.api.FileMgrApi;
import com.alpaca.components.filemgr.domain.*;
import com.alpaca.components.filemgr.entity.Attachment;
import com.alpaca.components.filemgr.service.AttachmentService;
import com.alpaca.components.filemgr.service.AttachmentUploadService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lichenw
 * Created on 2019/3/25
 */
@Service
public class FileMgrApiImpl implements FileMgrApi, AttachmentUploadService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AttachmentService sysAttachmentService;

    @Autowired
    private FileSystemConfig fileSystemConfig;

    @Override
    public Attachment getAttachment(String Id) {
        return sysAttachmentService.getById(Id);
    }

    @Override
    public Attachment margePhoto(AttachmentPathDefine path, String bizId, List<String> attachmentIds) {

        Attachment result = new Attachment();
        result.setFid(GuidHelper.getGuid());
        result.setBizID(bizId);

        Attachment baseMap = sysAttachmentService.getById(attachmentIds.get(0));


        String targetFolderPath = DefaultPathDefine.ATTACHMENT_SPACE_PATHNAME + path.buildFolderPath(result.getFid());
        File targetFolderFile = new File(fileSystemConfig.getPath() + targetFolderPath);
        if (!targetFolderFile.exists()) {
            targetFolderFile.mkdirs();
        }
        String actualName = result.getFid() + "." + baseMap.getExten();
        String targetFileName = targetFolderPath + actualName;


        File buildImg = new File(fileSystemConfig.getPath() + targetFileName);
        try {
            PicBuilder builder = new PicBuilder();
            builder.init(new File(fileSystemConfig.getPath() + baseMap.getActulPath()));

            for (int index = 1; index < attachmentIds.size(); index++) {
                Attachment attachmentT = sysAttachmentService.getById(attachmentIds.get(index));
                builder.appendImage(new File(fileSystemConfig.getPath() + attachmentT.getActulPath()));
            }
            builder.build(buildImg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("生成作品图片失败！");
        }
        result.setActulName(actualName);
        result.setActulPath(targetFileName);
        result.setExten(baseMap.getExten());
        result.setCreateTime(LocalDateHelper.getCurrent());
        result.setFileName(actualName);
        result.setIsPic(1);
        result.setFileSize(buildImg.length());
        result.setFileState(FileState.FORMAL);
        result.setLogicStatus(0);

        sysAttachmentService.saveOrUpdate(result);
        return result;
    }

    @Override
    public String getActualPath(String fileId) {
        Attachment attachment = sysAttachmentService.getById(fileId);
        if (attachment == null) {
            return null;
        }
        return fileSystemConfig.getPath() + attachment.getActulPath();

    }

    @Override
    public ExistsResultType existsFile(String id) {
        Attachment attachment = sysAttachmentService.getById(id);
        if (attachment == null) {
            return ExistsResultType.UNFOUNDRECORD;
        }
        if (FileState.SWAP != attachment.getFileState()) {
            return ExistsResultType.STATEERROR;
        }
        if (FileStatus.DELETE == attachment.getLogicStatus()) {
            return ExistsResultType.DELETE;
        }

        //临时文件路径
        File file = new File(fileSystemConfig.getPath() + attachment.getActulPath());
        if (file.exists()) {
            return ExistsResultType.EXISTS;
        }
        return ExistsResultType.UNFOUNDFILE;
    }

    @Override
    public Attachment saveAttachment(AttachmentPathDefine path, String bizId, String fileId) {
        Attachment attachment = sysAttachmentService.getById(fileId);
        attachment.setBizID(bizId);
        attachment.setFileState(FileState.FORMAL);

        //move file
        String sourceFilePath = attachment.getActulPath();
        String targetFolderPath = DefaultPathDefine.ATTACHMENT_SPACE_PATHNAME + path.buildFolderPath(fileId);
        String targetFilePath = targetFolderPath + attachment.getActulName();
        File sourceFile = new File(fileSystemConfig.getPath() + sourceFilePath);
        File targetFolderFile = new File(fileSystemConfig.getPath() + targetFolderPath);
        File targetFile = new File(fileSystemConfig.getPath() + targetFilePath);
        if (!targetFolderFile.exists()) {
            targetFolderFile.mkdirs();
        }
        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        attachment.setActulPath(targetFilePath);

        // is img , build thumb
        if (FileHelper.isImage(attachment.getExten())) {
            attachment.setIsPic(1);
            String thumbFolderPath = DefaultPathDefine.THUMBNAIL_SPACE_PATHNAME + path.buildFolderPath(fileId);
            String thumbFilePath = thumbFolderPath + attachment.getActulName();
            File thumbDirs = new File(fileSystemConfig.getPath() + thumbFolderPath);
            if (!thumbDirs.exists()) {
                thumbDirs.mkdirs();
            }
            try {
                Thumbnails.of(targetFile).size(200, 150).toFile(fileSystemConfig.getPath() + thumbFilePath);
                attachment.setThumbPath(thumbFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            attachment.setIsPic(0);
        }

        //save record
        if (sysAttachmentService.saveOrUpdate(attachment)) {

            //delete source file
            try {
                Files.delete(sourceFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return attachment;
        }

        return null;
    }

    @Override
    public CommonResult upload(HttpServletRequest request) throws Exception {
        CommonResult cr = new CommonResult();
        List<Attachment> records = new ArrayList<>();
        try {
            List<MultipartFile> files = new Multipart().getUploadFiles(request);
            if (DataUtil.isEmpty(files)) {
                cr.setMsg("系统没有获取到文件信息");
                cr.setSuccess(false);
                return cr;
            }
            for (MultipartFile file : files) {
                //1.设置图片基本信息
                String attmentid = GuidHelper.getGuid();
                String folderPath = DefaultPathDefine.SWAP_SPACE_PATHNAME + FileHelper.BuildFolderPath(attmentid);
                String pic_path = fileSystemConfig.getPath() + folderPath;
                String filename = file.getOriginalFilename();
                String newfileName = attmentid + filename.substring(filename.lastIndexOf("."));
                //新的图片
                File f = new File(pic_path);
                if (!f.exists()) {
                    boolean mkdirs = f.mkdirs();
                }
                File attment = new File(pic_path + newfileName);

                //2把内存图片写入磁盘中
                file.transferTo(attment);

                //3写入数据库
                Attachment attachment = new Attachment();
                attachment.setFid(attmentid);
                attachment.setFileName(filename);
                attachment.setActulPath(folderPath + newfileName);
                attachment.setExten(FilenameUtils.getExtension(filename));
                attachment.setFileSize(file.getSize());
                attachment.setActulName(newfileName);
                attachment.setCreateTime(LocalDateHelper.getCurrent());
                attachment.setFileState(FileState.SWAP);
                attachment.setLogicStatus(FileStatus.NORMAL);
                sysAttachmentService.saveOrUpdate(attachment);
                records.add(attachment);
                file = null;
            }
            cr.setData(records);
            files = null;
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败", e);
        }
        return cr;
    }

}
