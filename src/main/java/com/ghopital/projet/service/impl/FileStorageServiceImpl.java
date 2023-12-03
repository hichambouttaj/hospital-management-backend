package com.ghopital.projet.service.impl;

import com.ghopital.projet.dto.response.FileDtoResponse;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.FileMapper;
import com.ghopital.projet.repository.FileRepository;
import com.ghopital.projet.service.FileStorageService;
import com.ghopital.projet.util.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final FileRepository fileRepository;
    public FileStorageServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    @Override
    public FileDtoResponse uploadFile(MultipartFile file) {
        try {
            File newFile = new File();

            String fileName = file.getOriginalFilename();

            assert fileName != null;
            newFile.setName(fileName.substring(0, fileName.lastIndexOf('.')));
            newFile.setType(file.getContentType());
            newFile.setData(FileUtils.compress(file.getBytes()));

            File savedFile = fileRepository.save(newFile);

            return new FileDtoResponse(
                    savedFile.getId(),
                    savedFile.getName(),
                    savedFile.getType(),
                    savedFile.getData().length
            );
        }catch (IOException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "failed to upload file");
        }
    }

    @Override
    public File downloadFile(Long fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(
                () -> new ResourceNotFoundException("File", "name", fileId.toString()));

        file.setData(FileUtils.decompress(file.getData()));

        return file;
    }

    @Override
    public List<FileDtoResponse> getAllFiles() {
        return fileRepository.findAll().stream()
                .map(FileMapper.INSTANCE::fileToFileDtoResponse)
                .collect(Collectors.toList());
    }
}
