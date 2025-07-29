package com.user.dto;

import lombok.Data;

@Data
public class CreatUserRequest {
    private String name;
    private String email;
    private String password;
    private String role;

}
