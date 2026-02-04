package com.seplag.api.health;

import io.minio.MinioClient;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("MinIO-S3")
public class MinioHealthIndicator implements HealthIndicator {

    private final MinioClient minioClient;

    public MinioHealthIndicator(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public Health health() {
        try {
            minioClient.listBuckets();
            return Health.up().withDetail("minio", "Disponível").build();
        } catch (Exception e) {
            return Health.down(e).withDetail("minio", "Indisponível").build();
        }
    }
}

