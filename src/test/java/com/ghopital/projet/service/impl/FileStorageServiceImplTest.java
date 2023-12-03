package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.response.FileDtoResponse;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.repository.FileRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {FileStorageServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class FileStorageServiceImplTest {
    @MockBean
    private FileRepository fileRepository;

    @Autowired
    private FileStorageServiceImpl fileStorageServiceImpl;

    /**
     * Method under test: {@link FileStorageServiceImpl#uploadFile(MultipartFile)}
     */
    @Test
    void testUploadFile() throws IOException {
        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");
        when(fileRepository.save(Mockito.<File>any())).thenReturn(file);
        FileDtoResponse actualUploadFileResult = fileStorageServiceImpl.uploadFile(
                new MockMultipartFile("Name", "foo.txt", "text/plain", new ByteArrayInputStream("A.A.A.A.".getBytes("UTF-8"))));
        verify(fileRepository).save(Mockito.<File>any());
        assertEquals("Name", actualUploadFileResult.name());
        assertEquals("Type", actualUploadFileResult.type());
        assertEquals(1L, actualUploadFileResult.id().longValue());
        assertEquals(8L, actualUploadFileResult.size());
    }
    /**
     * Method under test:  {@link FileStorageServiceImpl#getAllFiles()}
     */
    @Test
    void testGetAllFiles() {
        when(fileRepository.findAll()).thenReturn(new ArrayList<>());
        List<FileDtoResponse> actualAllFiles = fileStorageServiceImpl.getAllFiles();
        verify(fileRepository).findAll();
        assertTrue(actualAllFiles.isEmpty());
    }

    /**
     * Method under test:  {@link FileStorageServiceImpl#getAllFiles()}
     */
    @Test
    void testGetAllFiles2() {
        when(fileRepository.findAll()).thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> fileStorageServiceImpl.getAllFiles());
        verify(fileRepository).findAll();
    }
}
