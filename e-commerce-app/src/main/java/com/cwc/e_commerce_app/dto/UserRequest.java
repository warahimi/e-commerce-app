package com.cwc.e_commerce_app.dto;

import com.cwc.e_commerce_app.Entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    Address address;
}
