package com.ghopital.projet.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "code")
    private Long code;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private Set<DeliveryNote> deliveryNotes;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    public Set<DeliveryNote> getDeliveryNotes() {
        return deliveryNotes;
    }
    public void setDeliveryNotes(Set<DeliveryNote> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }
}
