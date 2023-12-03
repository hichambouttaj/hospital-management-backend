package com.ghopital.projet.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;

@Entity
@Table(name = "vacation")
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    @Column(name = "is_sick_vacation")
    private boolean isSickVacation;
    @ManyToOne
    @JoinTable(
            name = "employee_vacation",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "vacation_id")
    )
    public Employee employee;
    public Vacation() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isSickVacation() {
        return isSickVacation;
    }

    public void setSickVacation(boolean sickVacation) {
        isSickVacation = sickVacation;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if(employee.getVacations() == null) {
            employee.setVacations(new HashSet<>());
        }
        employee.getVacations().add(this);
        this.employee = employee;
    }
}
