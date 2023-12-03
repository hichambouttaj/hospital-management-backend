package com.ghopital.projet.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "medication")
public class Medication extends Product{
    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    public Medication() {
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
