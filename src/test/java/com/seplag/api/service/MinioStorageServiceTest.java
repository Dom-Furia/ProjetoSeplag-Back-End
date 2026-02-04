package com.seplag.api.service;

import com.seplag.api.config.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioStorageServiceTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private MinioProperties minioProperties;

    @InjectMocks
    private MinioStorageService service;

    @Test
    public void deveFazerUploadComSucesso() throws Exception {

            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "teste.png",
                    "image/png",
                    "conteudo".getBytes()
            );

            when(minioProperties.getBucket()).thenReturn("albuns");
            when(minioClient.bucketExists(any())).thenReturn(true);

            String fileName = service.upload(file);

            assertNotNull(fileName);
            verify(minioClient).putObject(any(PutObjectArgs.class));
        }

    @Test
    void deveGerarUrlPreAssinada() throws Exception {
        when(minioProperties.getBucket()).thenReturn("albuns");
        when(minioClient.getPresignedObjectUrl(any()))
                .thenReturn("http://minio/files");

        String url = service.generateUrl("arquivo.png");

        assertEquals("http://minio/files", url);
    }

    //-------------------------Teste De Exceções-------------------------//
    @Test
    void deveLancarExcecaoQuandoFalharUpload() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "teste.png",
                "image/png",
                "conteudo".getBytes()
        );

        when(minioProperties.getBucket()).thenReturn("bucket-test");
        when(minioClient.bucketExists(any())).thenReturn(true);

        doThrow(new RuntimeException("Erro MinIO"))
                .when(minioClient)
                .putObject(any(PutObjectArgs.class));

        assertThrows(
                RuntimeException.class,
                () -> service.upload(file)
        );

    }

    @Test
    void deveLancarExcecaoQuandoFalharGeracaoDeUrl() throws Exception {
        when(minioProperties.getBucket()).thenReturn("bucket-test");

        when(minioClient.getPresignedObjectUrl(any()))
                .thenThrow(new RuntimeException("Erro MinIO"));

         assertThrows(
                 RuntimeException.class,
                () -> service.generateUrl("arquivo.png")
        );

    }

}
