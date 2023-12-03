package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.entity.Location;
import com.ghopital.projet.entity.Material;
import com.ghopital.projet.entity.QRCode;
import com.ghopital.projet.entity.Stock;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.repository.MaterialRepository;
import com.ghopital.projet.repository.QRCodeRepository;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {QRCodeServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class QRCodeServiceImplTest {
    @MockBean
    private MaterialRepository materialRepository;

    @MockBean
    private QRCodeRepository qRCodeRepository;

    @Autowired
    private QRCodeServiceImpl qRCodeServiceImpl;

    /**
     * Method under test: {@link QRCodeServiceImpl#generateQRCodeMaterial(Long)}
     */
    @Test
    void testGenerateQRCodeMaterial() throws UnsupportedEncodingException {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.nio.file.InvalidPathException: Illegal char <*> at index 35: C:\Users\lenovo\AppData\Local\Temp\*
        //       at java.base/sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:182)
        //       at java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:153)
        //       at java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:77)
        //       at java.base/sun.nio.fs.WindowsPath.parse(WindowsPath.java:92)
        //       at java.base/sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:232)
        //       at java.base/java.io.File.toPath(File.java:2387)
        //       at java.desktop/javax.imageio.ImageIO.hasCachePermission(ImageIO.java:216)
        //       at java.desktop/javax.imageio.ImageIO.createImageOutputStream(ImageIO.java:415)
        //       at java.desktop/javax.imageio.ImageIO.write(ImageIO.java:1591)
        //       at com.google.zxing.client.j2se.MatrixToImageWriter.writeToStream(MatrixToImageWriter.java:159)
        //       at com.google.zxing.client.j2se.MatrixToImageWriter.writeToStream(MatrixToImageWriter.java:144)
        //       at com.ghopital.projet.util.QRCodeUtils.generateQRCode(QRCodeUtils.java:25)
        //       at com.ghopital.projet.service.impl.QRCodeServiceImpl.generateQRCodeMaterial(QRCodeServiceImpl.java:79)
        //   See https://diff.blue/R013 to resolve this issue.

        QRCode qrCode = new QRCode();
        qrCode.setId(1L);
        qrCode.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());

        Stock stock = new Stock();
        stock.setId(1L);
        stock.setLocation(location);
        stock.setQuantity(1L);

        Material material = new Material();
        material.setDeliveryNotes(new HashSet<>());
        material.setDescription("The characteristics of someone or something");
        material.setId(1L);
        material.setName("Name");
        material.setQrCode(qrCode);
        material.setStock(stock);
        Optional<Material> ofResult = Optional.of(material);

        QRCode qrCode2 = new QRCode();
        qrCode2.setId(1L);
        qrCode2.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(new HashSet<>());

        Stock stock2 = new Stock();
        stock2.setId(1L);
        stock2.setLocation(location2);
        stock2.setQuantity(1L);

        Material material2 = new Material();
        material2.setDeliveryNotes(new HashSet<>());
        material2.setDescription("The characteristics of someone or something");
        material2.setId(1L);
        material2.setName("Name");
        material2.setQrCode(qrCode2);
        material2.setStock(stock2);
        MaterialRepository materialRepository = mock(MaterialRepository.class);
        when(materialRepository.save(Mockito.<Material>any())).thenReturn(material2);
        when(materialRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        QRCodeRepository qrCodeRepository = mock(QRCodeRepository.class);
        doThrow(new AppException(HttpStatus.CONTINUE, "An error occurred")).when(qrCodeRepository)
                .delete(Mockito.<QRCode>any());
        assertThrows(AppException.class,
                () -> (new QRCodeServiceImpl(materialRepository, qrCodeRepository)).generateQRCodeMaterial(1L));
        verify(qrCodeRepository).delete(Mockito.<QRCode>any());
        verify(materialRepository).findById(Mockito.<Long>any());
        verify(materialRepository).save(Mockito.<Material>any());
    }

    /**
     * Method under test: {@link QRCodeServiceImpl#getQRCodeMaterial(Long)}
     */
    @Test
    void testGetQRCodeMaterial() throws UnsupportedEncodingException {
        QRCode qrCode = new QRCode();
        qrCode.setId(1L);
        qrCode.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

        Location location = new Location();
        location.setAddress("42 Main St");
        location.setDescription("The characteristics of someone or something");
        location.setId(1L);
        location.setLatitude("Latitude");
        location.setLongitude("Longitude");
        location.setName("Name");
        location.setStocks(new HashSet<>());

        Stock stock = new Stock();
        stock.setId(1L);
        stock.setLocation(location);
        stock.setQuantity(1L);

        Material material = new Material();
        material.setDeliveryNotes(new HashSet<>());
        material.setDescription("The characteristics of someone or something");
        material.setId(1L);
        material.setName("Name");
        material.setQrCode(qrCode);
        material.setStock(stock);
        Optional<Material> ofResult = Optional.of(material);

        QRCode qrCode2 = new QRCode();
        qrCode2.setId(1L);
        qrCode2.setQrCodeImage("AXAXAXAX".getBytes("UTF-8"));

        Location location2 = new Location();
        location2.setAddress("42 Main St");
        location2.setDescription("The characteristics of someone or something");
        location2.setId(1L);
        location2.setLatitude("Latitude");
        location2.setLongitude("Longitude");
        location2.setName("Name");
        location2.setStocks(new HashSet<>());

        Stock stock2 = new Stock();
        stock2.setId(1L);
        stock2.setLocation(location2);
        stock2.setQuantity(1L);

        Material material2 = new Material();
        material2.setDeliveryNotes(new HashSet<>());
        material2.setDescription("The characteristics of someone or something");
        material2.setId(1L);
        material2.setName("Name");
        material2.setQrCode(qrCode2);
        material2.setStock(stock2);
        when(materialRepository.save(Mockito.<Material>any())).thenReturn(material2);
        when(materialRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doThrow(new AppException(HttpStatus.CONTINUE, "An error occurred")).when(qRCodeRepository)
                .delete(Mockito.<QRCode>any());
        assertThrows(AppException.class, () -> qRCodeServiceImpl.getQRCodeMaterial(1L));
        verify(qRCodeRepository).delete(Mockito.<QRCode>any());
        verify(materialRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(materialRepository).save(Mockito.<Material>any());
    }
}
