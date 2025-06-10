package com.Epic01.customer.controller;

import com.Epic01.customer.dto.UserInfoResponseDTO;
import com.Epic01.customer.entity.Customer;
import com.Epic01.customer.entity.User;
import com.Epic01.customer.repository.CustomerRepository;
import com.Epic01.customer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/customer")
public class CustomerInfoController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('customer')")
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        System.out.println("🧾 Authenticated user: " + authentication.getName());
        System.out.println("✅ Authorities: " + authentication.getAuthorities());


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

    @PreAuthorize("hasRole('customer')")
    @PutMapping("/me")
    public ResponseEntity<?> updateMyInfo(@RequestBody UserInfoResponseDTO dto, Authentication authentication  ){
        if(!(authentication.getPrincipal() instanceof User)){
            return ResponseEntity.status(401).body("User not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        Customer customer = customerRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // cap nha thong tin
        if(dto.getName() != null){user.setName(dto.getName());}
        if (dto.getPhones() != null){user.setPhone(dto.getPhones());}
        if(dto.getDob() != null){
            try {
                user.setDob(LocalDate.parse(dto.getDob()));
            } catch(DateTimeException e){
                return ResponseEntity.badRequest().body("Invalid date format. Please use YYYY-MM-DD format");
            }
        }

        if(dto.getGender() != null){
            try {
                user.setGender(com.Epic01.customer.enums.Genders.valueOf(dto.getGender()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid gender value: " + dto.getGender());
            }
        }
        if(dto.getMedicalHistory() != null){
            customer.setMedicalHistory(dto.getMedicalHistory());
        }

        user.setUpdateAt(LocalDate.now());
        customerRepository.save(customer);
        userRepository.save(user);

        return ResponseEntity.ok("Update successfully");
    }
}
