package com.ghopital.projet.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_order")
public class ProductOrder extends Order {
    @ManyToOne
    @JoinColumn(name = "service_id")
    private HospitalService hospitalService;
    public HospitalService getHospitalService() {
        return hospitalService;
    }
    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }
}
