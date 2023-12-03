package com.ghopital.projet.util;

import com.ghopital.projet.exception.AppException;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.HttpStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeUtils {
    public static byte[] generateQRCode(String qrCodeDate, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeDate, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", baos);
            baos.flush();

            byte[] qrCodeImage = baos.toByteArray();
            baos.close();

            return qrCodeImage;
        }catch (WriterException | IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating QR code");
        }
    }

    public static String readQRCode(byte[] qrCodeImage) {
        try {

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(qrCodeImage));
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));

            Result qrCodeResult = new MultiFormatReader().decode(bitmap);

            if (qrCodeResult == null) {
                throw new AppException(HttpStatus.NOT_FOUND, "QR code not found in the image");
            }

            return qrCodeResult.getText();
        }catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading QR code");
        }catch (NotFoundException e) {
            throw new AppException(HttpStatus.NOT_FOUND, "QR code not found in this image");
        }
    }

}
