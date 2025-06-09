package com.Epic01.customer.controller;

import com.Epic01.customer.dto.CustomerResponseDTO;
import com.Epic01.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customers")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>>getAllCustomers(){
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
}
