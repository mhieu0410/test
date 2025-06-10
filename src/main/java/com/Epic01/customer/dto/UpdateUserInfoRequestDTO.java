package com.Epic01.customer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserInfoRequestDTO {
    private String name;
    private String email;
    private String phones;
    private String gender;
    private String dob;
    private String medicalHistory;
}
