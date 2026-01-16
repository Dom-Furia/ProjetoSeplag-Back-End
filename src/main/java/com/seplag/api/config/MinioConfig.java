package com.seplag.api.config;

import io.minio.MinioClient;
import io.minio.MinioProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(MinioProperties.class)
//Cria o MinioClient para ser injetado em qualquer serice
public class MinioConfig {
    @Bean
    public MinioClient minioClient(com.seplag.api.config.MinioProperties props) {
        return MinioClient.builder()
                .endpoint(props.getUrl())
                .credentials(
                        props.getAccessKey(),
                        props.getSecretKey()
                )
                .build();
    }
}
