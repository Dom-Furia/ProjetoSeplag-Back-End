package com.seplag.api.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;


    @Value("${minio.expiry-minutes:30}")
    private int expiryMinutes;

    /**
     * Garante que o bucket exista ao iniciar a aplicação
     */
    @PostConstruct
    public void init() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar/criar bucket MinIO", e);
        }
    }

    /**
     * Upload do arquivo
     */
    public String upload(MultipartFile file) {
        try {
            String sanitizedName = Objects.requireNonNull(file.getOriginalFilename())
                    .replaceAll("[^a-zA-Z0-9.-]", "_");

            String fileName = UUID.randomUUID() + "_" + sanitizedName;

            try (InputStream input = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(input, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar arquivo para MinIO", e);
        }
    }

    /**
     * Gera URL temporária para download
     */
    public String generateUrl(String objectName) {
        try {

            // troca URL interna do Docker pela externa do navegador
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expiryMinutes, TimeUnit.MINUTES)
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar URL temporária", e);
        }
    }

    /**
     * Remove arquivo
     */
    public void delete(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover arquivo do MinIO", e);
        }
    }
}
