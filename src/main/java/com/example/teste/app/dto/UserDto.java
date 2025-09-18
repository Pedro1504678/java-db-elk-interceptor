package com.example.teste.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String name;
    private String cpf;
    private String email;
    private String age;
}
