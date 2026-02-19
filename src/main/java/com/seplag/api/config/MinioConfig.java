package com.seplag.api.config;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class MinioConfig {

    @Value("${minio.url}") // http://minio:9000
    private String minioUrl;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;


    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl) // Conecta internamente via Docker network
                .credentials(accessKey, secretKey)
                .build();
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Cuiaba"));
    }
}
