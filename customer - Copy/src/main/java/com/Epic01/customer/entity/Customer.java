package com.Epic01.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private Integer customerId;

    @OneToOne
    @MapsId  // Dùng khi khóa chính cũng chính là khóa ngoại
    @JoinColumn(name = "customer_id")
    private User user;

    @Column(name = "medical_history")
    private String medicalHistory;

    @Column(name = "is_active")
    private boolean isActive;

    private Long user_id;

    public Customer() {
    }

    public Customer(int customerId, User user, String medicalHistory, boolean isActive) {
        this.customerId = customerId;
        this.user = user;
        this.medicalHistory = medicalHistory;
        this.isActive = isActive;
    }
}
