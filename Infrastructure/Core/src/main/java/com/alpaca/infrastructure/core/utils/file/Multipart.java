package com.alpaca.infrastructure.core.utils.file;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class Multipart {

    public List<MultipartFile> getUploadFiles(HttpServletRequest request) {

        // 解析器解析request的上下文
        List<MultipartFile> multipartFiles = new ArrayList<>();
        try {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            // 先判断request中是否包涵multipart类型的数据
            if (multipartResolver.isMultipart(request)) {
                // 再将request中的数据转化成multipart类型的数据
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                multipartFiles = multiRequest.getFiles("file");
            }
        } catch (Exception e) {
            throw e;
        }
        return multipartFiles;
    }

}



