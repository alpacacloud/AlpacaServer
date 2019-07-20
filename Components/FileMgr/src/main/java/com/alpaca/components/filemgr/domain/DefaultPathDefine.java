package com.alpaca.components.filemgr.domain;


import java.io.Serializable;

public class DefaultPathDefine implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 临时文件交换区路径
     */
    public final static String SWAP_SPACE_PATHNAME = "SWAP/";
    /**
     * 缩略图文件目录名
     */
    public final static String THUMBNAIL_SPACE_PATHNAME = "THUMBNAIL/";
    /**
     * 附件存储目录名
     */
    public final static String ATTACHMENT_SPACE_PATHNAME = "ATTACHMENTS/";
    /**
     * 解压区
     */
    public final static String UNZIP = "UNZIP/";


}

