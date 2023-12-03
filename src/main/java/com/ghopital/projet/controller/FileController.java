package com.ghopital.projet.controller;

import com.ghopital.projet.dto.response.FileDtoResponse;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileDtoResponse> uploadFile(@RequestPart(name = "file") MultipartFile file) {
        FileDtoResponse response = fileStorageService.uploadFile(file);
        return ResponseEntity.created(URI.create("/files/" + response.id()))
                .body(response);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable(name = "fileId") Long fileId) {
        File downloadedFile = fileStorageService.downloadFile(fileId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(downloadedFile.getType()))
                .body(downloadedFile.getData());
    }
}
