package com.Epic01.customer.controller;

import com.Epic01.customer.dto.LoginRequestDTO;
import com.Epic01.customer.dto.LoginResponseDTO;
import com.Epic01.customer.dto.RegisterRequestDTO;
import com.Epic01.customer.entity.Customer;
import com.Epic01.customer.entity.Role;
import com.Epic01.customer.entity.User;
import com.Epic01.customer.enums.Genders;
import com.Epic01.customer.repository.CustomerRepository;
import com.Epic01.customer.repository.RoleRepository;
import com.Epic01.customer.repository.UserRepository;
import com.Epic01.customer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO dto){
        // 1. check email trung
        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("Email is already taken");
        }

        // 2. Lay role Customer
        Role customerRole = roleRepository.findByRoleName("customer")
                .orElseThrow(() -> new RuntimeException("Role CUSTOMER is not existed"));

        // 3. Tao user
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        if (dto.getGender() != null) {
            try {
                user.setGender(Genders.valueOf(dto.getGender()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid gender value: " + dto.getGender());
            }
        }
        if (dto.getDob() != null) {
            try {
                user.setDob(LocalDate.parse(dto.getDob()));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD format", e);
            }
        }
        user.setCreateAt(LocalDate.now());
        user.setUpdateAt(LocalDate.now());
        user.setRole(customerRole);

        User saveUser = userRepository.save(user);


        // 4. tao Customer
        Customer customer = new Customer();
        customer.setUser(saveUser);
        customer.setMedicalHistory(dto.getMedicalHistory());
        customer.setActive(true);

        customerRepository.save(customer);

        return ResponseEntity.ok("Register successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request){
        return userRepository.findByEmail(request.getEmail())
                .map(user ->{
                    if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
                        String token = jwtUtil.generateToken(user.getEmail());
                        return ResponseEntity.ok(new LoginResponseDTO(token));
                    } else {
                        return ResponseEntity.status(401).body("Invalid password");
                    }
                })
                .orElse(ResponseEntity.status(401).body("Invalid account"));
    }

}
