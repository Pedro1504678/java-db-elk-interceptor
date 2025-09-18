package com.example.teste.app.service;

import com.example.teste.infra.entity.UserEntity;
import com.example.teste.infra.repository.UserRepository;
import com.example.teste.app.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void salvarUsuario(UserDto userDto) {
        UserEntity user = new UserEntity();
        user.setName(userDto.getName().toUpperCase());
        user.setEmail(userDto.getEmail().toUpperCase());
        user.setCpf(userDto.getCpf());
        user.setAge(userDto.getAge());

        userRepository.save(user);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }
}
