package com.ghopital.projet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.dto.response.QRCodeDtoResponse;
import com.ghopital.projet.service.QRCodeService;
import com.ghopital.projet.service.impl.QRCodeServiceImpl;

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

@ContextConfiguration(classes = {QRCodeController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class QRCodeControllerTest {
    @Autowired
    private QRCodeController qRCodeController;

    @MockBean
    private QRCodeService qRCodeService;

    /**
     * Method under test:  {@link QRCodeController#generateQRCodeMaterial(Long)}
     */
    @Test
    void testGenerateQRCodeMaterial() throws Exception {
        when(qRCodeService.generateQRCodeMaterial(Mockito.<Long>any()))
                .thenReturn(new QRCodeDtoResponse(1L, "AXAXAXAX".getBytes("UTF-8")));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/materials/{materialId}/qrCode",
                1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(qRCodeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"qrCodeImage\":\"QVhBWEFYQVg=\"}"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/materials/1/qrCode/1"));
    }

    /**
     * Method under test:  {@link QRCodeController#getQRCodeMaterial(Long)}
     */
    @Test
    void testGetQRCodeMaterial() throws Exception {
        when(qRCodeService.getQRCodeMaterial(Mockito.<Long>any())).thenReturn("AXAXAXAX".getBytes("UTF-8"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/materials/{materialId}/qrCode",
                1L);
        MockMvcBuilders.standaloneSetup(qRCodeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("image/png"))
                .andExpect(MockMvcResultMatchers.content().string("AXAXAXAX"));
    }

    /**
     * Method under test: {@link QRCodeController#readQRCode(MultipartFile)}
     */
    @Test
    void testReadQRCode() throws IOException {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   jakarta.servlet.ServletException: Request processing failed: org.springframework.web.multipart.MultipartException: Current request is not a multipart request
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   org.springframework.web.multipart.MultipartException: Current request is not a multipart request
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
        //       at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
        //   See https://diff.blue/R013 to resolve this issue.

        QRCodeServiceImpl qrCodeService = mock(QRCodeServiceImpl.class);
        when(qrCodeService.readQRCodeMaterial(Mockito.<byte[]>any())).thenReturn("Qr Code Material");
        QRCodeController qrCodeController = new QRCodeController(qrCodeService);
        ResponseEntity<String> actualReadQRCodeResult = qrCodeController
                .readQRCode(new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        verify(qrCodeService).readQRCodeMaterial(Mockito.<byte[]>any());
        assertEquals("Qr Code Material", actualReadQRCodeResult.getBody());
        assertEquals(200, actualReadQRCodeResult.getStatusCodeValue());
        assertTrue(actualReadQRCodeResult.getHeaders().isEmpty());
    }
}
