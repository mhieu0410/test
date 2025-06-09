package com.Epic01.customer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String name;
    private String email;
    private String password;
    private String medicalHistory;
    private String phone;
    private String gender;
    private String dob;
}
