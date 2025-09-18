package com.example.teste.app.resource;

import com.example.teste.app.dto.GetUserDto;
import com.example.teste.app.dto.UserDto;
import com.example.teste.app.service.UserService;
import com.example.teste.infra.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class SaveUserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveUserResource.class);

    protected final UserService userService;

    @PostMapping("/save_user")
    public void saveUser(
            @RequestBody UserDto user) {
        userService.salvarUsuario(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<GetUserDto>> getUsers() {

        List<UserEntity> getUsersServiceResponse = userService.findAll();
        List<GetUserDto> getUserDtos = new ArrayList<>();

        getUsersServiceResponse.forEach(user -> {

            GetUserDto getUserDto = GetUserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .cpf(user.getCpf())
                    .email(user.getEmail())
                    .age(user.getAge())
                    .build();

            getUserDtos.add(getUserDto);
        });

        return ResponseEntity.ok().body(getUserDtos);
    }
}
