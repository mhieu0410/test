package com.Epic01.customer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDTO {
    private int id;
    private String name;
    private String email;
    private Boolean isActive;
    private String medicalHistory;

    public CustomerResponseDTO(int id, String name, String email, Boolean isActive, String medicalHistory) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isActive = isActive;
        this.medicalHistory = medicalHistory;
    }

    public CustomerResponseDTO() {
    }


}
