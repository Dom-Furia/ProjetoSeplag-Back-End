package com.seplag.api.service;

import com.seplag.api.config.MinioProperties;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MinioStorageService {
    private final MinioClient minioClient;
    private final MinioProperties  properties;

    public MinioStorageService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.properties = minioProperties;
    }


    public String upload(MultipartFile file) {
        try {
            createBucketIfNotExists();
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(fileName)
                            .stream(
                                    file.getInputStream(),
                                    file.getSize(),
                                    -1
                            )
                            .contentType(file.getContentType())
                            .build()
            );
            return fileName;

        }catch (Exception e){
            throw new RuntimeException("Erro ao fazer upload da imagem",e);
        }
    }

    //Metodo para gerar link pré-assinados com expiração
    public String generateUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(properties.getBucket())
                            .object(fileName)
                            .expiry(30, TimeUnit.MINUTES) // 30 minutos
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar link de download", e);
        }
    }


    private void createBucketIfNotExists() throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(properties.getBucket())
                        .build()
        );

        if (!exists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(properties.getBucket())
                            .build()
            );
        }
    }
}
