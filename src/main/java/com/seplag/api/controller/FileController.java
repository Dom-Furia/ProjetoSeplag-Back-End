package com.seplag.api.controller;

import com.seplag.api.service.MinioStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileController {

    private final MinioStorageService storageService;

        public FileController(MinioStorageService storageService) {
            this.storageService = storageService;
        }

        @GetMapping("/download/{fileName}")
        public ResponseEntity<String> getDownloadLink(@PathVariable String fileName) {
            String url = storageService.generateUrl(fileName);
            return ResponseEntity.ok(url);
        }

}
