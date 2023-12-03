package com.ghopital.projet.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "employee", uniqueConstraints = @UniqueConstraint(columnNames = {"cin", "registration_number"}))
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "cin", nullable = false)
    private String cin;
    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;
    @Column(name = "recruitment_date", nullable = false)
    private LocalDate recruitmentDate;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private HospitalService hospitalService;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "employee")
    private Set<Vacation> vacations;
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "employee_document",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private Set<File> documents;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "employee")
    private Set<Diploma> diplomas;
    public Employee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public LocalDate getRecruitmentDate() {
        return recruitmentDate;
    }

    public void setRecruitmentDate(LocalDate recruitmentDate) {
        this.recruitmentDate = recruitmentDate;
    }

    public HospitalService getService() {
        return hospitalService;
    }

    public void setService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    public Set<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(Set<Vacation> vacations) {
        this.vacations = vacations;
    }

    public Set<File> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<File> documents) {
        this.documents = documents;
    }

    public Set<Diploma> getDiplomas() {
        return diplomas;
    }

    public void setDiplomas(Set<Diploma> diplomas) {
        this.diplomas = diplomas;
    }
}
