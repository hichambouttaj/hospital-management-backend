package com.ghopital.projet.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "supplier", uniqueConstraints = {@UniqueConstraint(columnNames = {"phone", "email"})})
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "email", nullable = false)
    private String email;
    @OneToMany(mappedBy = "supplier", orphanRemoval = true)
    private Set<StockOrder> stockOrders;
    public Supplier() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<StockOrder> getDeliveryNotes() {
        return stockOrders;
    }

    public void setDeliveryNotes(Set<StockOrder> stockOrders) {
        this.stockOrders = stockOrders;
    }
}
