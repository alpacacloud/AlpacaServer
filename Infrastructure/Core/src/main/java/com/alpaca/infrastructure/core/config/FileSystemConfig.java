package com.alpaca.infrastructure.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class FileSystemConfig {

    @Value("${ser.fileSystem.path}")
    private String path;
}
