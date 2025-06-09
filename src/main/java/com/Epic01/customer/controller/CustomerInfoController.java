package com.Epic01.customer.controller;

import com.Epic01.customer.dto.UserInfoResponseDTO;
import com.Epic01.customer.entity.Customer;
import com.Epic01.customer.entity.User;
import com.Epic01.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerInfoController {

    @Autowired
    private CustomerRepository customerRepository;

    @PreAuthorize("hasAuthority('customer')")
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Customer customer = customerRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        UserInfoResponseDTO dto = new UserInfoResponseDTO();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhones(user.getPhone()); // ✅ đúng tên method
        dto.setGender(user.getGender() != null ? user.getGender().toString() : "UNSPECIFIED");
        dto.setDob(user.getDob() != null ? user.getDob().toString() : null);
        dto.setIsActive(customer.isActive());
        dto.setMedicalHistory(customer.getMedicalHistory());

        return ResponseEntity.ok(dto);
    }
}
