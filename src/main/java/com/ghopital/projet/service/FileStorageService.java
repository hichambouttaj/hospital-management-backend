package com.ghopital.projet.service;

import com.ghopital.projet.dto.response.FileDtoResponse;
import com.ghopital.projet.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    FileDtoResponse uploadFile(MultipartFile file);
    File downloadFile(Long fileId);
    List<FileDtoResponse> getAllFiles();
}
