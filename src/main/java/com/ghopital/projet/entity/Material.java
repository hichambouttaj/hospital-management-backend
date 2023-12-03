package com.ghopital.projet.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "material")
public class Material extends Product{
    @OneToOne
    @JoinColumn(name = "qr_code_id", nullable = true)
    private QRCode qrCode;
    public Material() {
    }
    public QRCode getQrCode() {
        return qrCode;
    }
    public void setQrCode(QRCode qrCode) {
        this.qrCode = qrCode;
    }
}
