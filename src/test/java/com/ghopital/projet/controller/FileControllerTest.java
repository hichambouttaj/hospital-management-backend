package com.ghopital.projet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.response.FileDtoResponse;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.service.FileStorageService;
import com.ghopital.projet.service.impl.FileStorageServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {FileController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class FileControllerTest {
    @Autowired
    private FileController fileController;

    @MockBean
    private FileStorageService fileStorageService;

    /**
     * Method under test: {@link FileController#uploadFile(MultipartFile)}
     */
    @Test
    void testUploadFile() throws IOException {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: org.springframework.web.multipart.MultipartException: Current request is not a multipart request
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:590)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   org.springframework.web.multipart.MultipartException: Current request is not a multipart request
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:590)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        FileStorageServiceImpl fileStorageService = mock(FileStorageServiceImpl.class);
        when(fileStorageService.uploadFile(Mockito.<MultipartFile>any()))
                .thenReturn(new FileDtoResponse(1L, "Name", "Type", 3L));
        FileController fileController = new FileController(fileStorageService);
        ResponseEntity<FileDtoResponse> actualUploadFileResult = fileController
                .uploadFile(new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        verify(fileStorageService).uploadFile(Mockito.<MultipartFile>any());
        assertEquals(1, actualUploadFileResult.getHeaders().size());
        assertEquals(201, actualUploadFileResult.getStatusCodeValue());
        assertTrue(actualUploadFileResult.hasBody());
    }

    /**
     * Method under test: {@link FileController#downloadFile(Long)}
     */
    @Test
    void testDownloadFile() throws Exception {
        File file = new File();
        file.setData("AXAXAXAX".getBytes("UTF-8"));
        file.setId(1L);
        file.setName("Name");
        file.setType("Type");
        when(fileStorageService.downloadFile(Mockito.<Long>any())).thenReturn(file);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/files/download/{fileId}",
                "Uri Variables", "Uri Variables");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(fileController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}
