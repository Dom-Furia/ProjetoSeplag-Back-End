package com.seplag.api.service;

import com.seplag.api.domain.user.User;
import com.seplag.api.dto.UserResponseDTO;
import com.seplag.api.dto.UserRequestDTO;
import com.seplag.api.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO registerUser(UserRequestDTO userDTO){

        if (userDTO.name() == null || userDTO.name().isBlank() ) {
            throw new IllegalArgumentException("O nome do usuario é obrigatório");
        }

        if (userDTO.email() == null || userDTO.email().isBlank() ) {
            throw new IllegalArgumentException("O E-mail do usuario é obrigatório");
        }

        if (userDTO.password() == null || userDTO.password().isBlank() ) {
            throw new IllegalArgumentException("A Senha do usuario é obrigatório");
        }

        Optional<User> user = userRepository.findByEmail(userDTO.email());
        if (user.isPresent()) {
            throw new IllegalArgumentException("Usuário já cadastrado.");
        }

        User newUser = new User();
        newUser.setName(userDTO.name());
        newUser.setEmail(userDTO.email());
        newUser.setPassword(passwordEncoder.encode(userDTO.password()));

        User savedUser = userRepository.save(newUser);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );

    }

    public List<UserResponseDTO> listUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (dto.name() != null && !dto.name().isBlank()) {
            user.setName(dto.name());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            user.setEmail(dto.email());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        User updatedUser = userRepository.save(user);

        return new UserResponseDTO(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail()
        );
    }

    public void deleteUser(UUID id) {

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        userRepository.deleteById(id);
    }






}
