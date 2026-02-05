package com.seplag.api.service;

import com.seplag.api.config.MinioProperties;
import io.minio.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioStorageServiceTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private MinioProperties minioProperties;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private MinioStorageService minioStorageService;

    /* ---------------- UPLOAD ---------------- */

    @Test
    @DisplayName("Deve fazer upload do arquivo com sucesso")
    void deveFazerUploadComSucesso() throws Exception {

        when(minioProperties.getBucket()).thenReturn("bucket-test");
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);

        when(multipartFile.getOriginalFilename()).thenReturn("imagem.png");
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(multipartFile.getSize()).thenReturn(10L);
        when(multipartFile.getInputStream())
                .thenReturn(new ByteArrayInputStream("teste".getBytes()));

        String fileName = minioStorageService.upload(multipartFile);

        assertThat(fileName).contains("imagem.png");

        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    @DisplayName("Deve criar bucket quando não existir antes do upload")
    void deveCriarBucketQuandoNaoExistir() throws Exception {

        when(minioProperties.getBucket()).thenReturn("bucket-test");
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);

        when(multipartFile.getOriginalFilename()).thenReturn("file.txt");
        when(multipartFile.getContentType()).thenReturn("text/plain");
        when(multipartFile.getSize()).thenReturn(5L);
        when(multipartFile.getInputStream())
                .thenReturn(new ByteArrayInputStream("teste".getBytes()));

        minioStorageService.upload(multipartFile);

        verify(minioClient).makeBucket(any(MakeBucketArgs.class));
        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro no upload")
    void deveLancarExcecaoNoUpload() throws Exception {

        when(minioProperties.getBucket()).thenReturn("bucket-test");
        when(minioClient.bucketExists(any(BucketExistsArgs.class)))
                .thenThrow(new RuntimeException("Erro MinIO"));

        assertThatThrownBy(() -> minioStorageService.upload(multipartFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Erro ao fazer upload da imagem");
    }

    /* ---------------- GENERATE URL ---------------- */

    @Test
    @DisplayName("Deve gerar URL pré-assinada com sucesso")
    void deveGerarUrlComSucesso() throws Exception {

        when(minioProperties.getBucket()).thenReturn("bucket-test");
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenReturn("http://minio/download/url");

        String url = minioStorageService.generateUrl("arquivo.png");

        assertThat(url).isEqualTo("http://minio/download/url");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar gerar URL")
    void deveLancarExcecaoAoGerarUrl() throws Exception {

        when(minioProperties.getBucket()).thenReturn("bucket-test");
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenThrow(new RuntimeException("Erro MinIO"));

        assertThatThrownBy(() -> minioStorageService.generateUrl("arquivo.png"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Erro ao gerar link de download");
    }

    /* ---------------- DELETE ---------------- */

    @Test
    @DisplayName("Deve remover arquivo com sucesso")
    void deveRemoverArquivoComSucesso() throws Exception {

        when(minioProperties.getBucket()).thenReturn("bucket-test");

        minioStorageService.delete("arquivo.png");

        verify(minioClient).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover arquivo")
    void deveLancarExcecaoAoRemoverArquivo() throws Exception {

        when(minioProperties.getBucket()).thenReturn("bucket-test");
        doThrow(new RuntimeException("Erro MinIO"))
                .when(minioClient).removeObject(any(RemoveObjectArgs.class));

        assertThatThrownBy(() -> minioStorageService.delete("arquivo.png"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Erro ao remover arquivo do MinIO");
    }
}
