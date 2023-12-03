package com.ghopital.projet.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stock_order")
public class StockOrder extends Order{
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;
    @Column(name = "delivery_location", nullable = false)
    private String deliveryLocation;
    public StockOrder() {
    }
    public Supplier getSupplier() {
        return supplier;
    }
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    public String getDeliveryLocation() {
        return deliveryLocation;
    }
    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }
}
