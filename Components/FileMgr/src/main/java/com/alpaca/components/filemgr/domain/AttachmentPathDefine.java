package com.alpaca.components.filemgr.domain;

import com.alpaca.infrastructure.core.utils.file.FileHelper;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class AttachmentPathDefine implements Serializable {

    private static final long serialVersionUID = 1L;
    private String path;

    private AttachmentPathDefine childPathDefine;

    public AttachmentPathDefine(String path) {
        setPath(path);
    }

    private void setChildPathRec(String childPathRec) {
        if (getChildPathDefine() == null) {
            setChildPathDefine(new AttachmentPathDefine(childPathRec));
        } else {
            getChildPathDefine().setChildPathRec(childPathRec); //递归
        }
    }

    /**
     * 设置子路径，自动追加到路径的末尾
     *
     * @param childPath
     * @return
     */
    public AttachmentPathDefine setChildPath(String childPath) {
        setChildPathRec(childPath);
        return this;
    }

    /**
     * 获取整体文件根目录路径
     *
     * @return
     */
    public String getFullRootPath() {
        if (childPathDefine != null) {
            return path + FileHelper.PATH_SPLIT_CHARTER + childPathDefine.getFullRootPath();
        }
        return path;
    }


    public String buildFolderPath(String fileId) {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(getFullRootPath());
        pathBuilder.append(FileHelper.PATH_SPLIT_CHARTER);
        pathBuilder.append(FileHelper.BuildFolderPath(fileId));
        return pathBuilder.toString();
    }
}
