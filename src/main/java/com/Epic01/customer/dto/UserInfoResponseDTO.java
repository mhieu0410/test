package com.Epic01.customer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponseDTO {
    private int id;
    private String name;
    private String email;
    private String phones;
    private String gender;
    private String dob;
    private String medicalHistory;
    private Boolean isActive;
}
