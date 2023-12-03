package com.ghopital.projet.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "service", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class HospitalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @OneToMany(mappedBy = "hospitalService")
    private Set<ProductOrder> productOrders;
    public HospitalService() {
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
    public Set<ProductOrder> getProductOrders() {
        return productOrders;
    }
    public void setProductOrders(Set<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }
}
