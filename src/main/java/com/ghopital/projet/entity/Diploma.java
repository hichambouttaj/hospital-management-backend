package com.ghopital.projet.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;

@Entity
@Table(name = "diploma")
public class Diploma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "document_id")
    private File document;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    public Diploma() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public File getDocument() {
        return document;
    }

    public void setDocument(File document) {
        this.document = document;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if(employee.getDiplomas() == null) {
            employee.setDiplomas(new HashSet<>());
        }
        employee.getDiplomas().add(this);
        this.employee = employee;
    }
}
