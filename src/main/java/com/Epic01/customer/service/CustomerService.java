package com.Epic01.customer.service;

import com.Epic01.customer.dto.CustomerResponseDTO;
import com.Epic01.customer.entity.Customer;
import com.Epic01.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<CustomerResponseDTO> getAllCustomers(){
        List <Customer> customer = customerRepository.findAll();

        return customer.stream()
                .map(c -> new CustomerResponseDTO(
                        c.getCustomerId(),
                        c.getUser().getName(),
                        c.getUser().getEmail(),
                        c.isActive(),
                        c.getMedicalHistory()
                ))
                .collect(Collectors.toList());
    }

}
