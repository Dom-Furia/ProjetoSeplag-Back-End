package com.seplag.api.service;

import com.seplag.api.domain.user.User;
import com.seplag.api.dto.*;
import com.seplag.api.repositories.UserRepository;
import com.seplag.api.security.RefreshTokenService;
import com.seplag.api.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    /* --------------------------CRIAR --------------------- */
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


    //---------------------------- Listar Usuarios ----------------------------------//
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

    //---------------------------- Atualizar Usuario ----------------------------------//
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

    //---------------------------- Excluir Usuario ----------------------------------//
    public void deleteUser(UUID id) {

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        userRepository.deleteById(id);
    }

    //---------------------------- Login Usuario ----------------------------------//
    public LoginResponseDTO login(LoginRequestDTO logindto) {

        var authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        logindto.email(),
                        logindto.password()
                );

        authenticationManager.authenticate(authenticationToken);

        User user = userRepository.findByEmail(logindto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String accessToken = tokenService.generateToken(user);
        String refreshToken = refreshTokenService.create(user);

        return new LoginResponseDTO(
                user.getName(),
                accessToken,
                refreshToken
        );
    }






}
