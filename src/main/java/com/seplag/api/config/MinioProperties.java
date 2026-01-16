package com.seplag.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
//Ler configs do application.properties
public class MinioProperties {
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;
}

