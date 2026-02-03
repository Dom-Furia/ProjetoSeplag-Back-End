package com.seplag.api.controller;

import com.seplag.api.dto.*;
import com.seplag.api.domain.user.*;
import com.seplag.api.repositories.UserRepository;
import com.seplag.api.security.TokenService;
import com.seplag.api.security.RefreshTokenService;
import com.seplag.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints responsáveis pelo Registro e Login de usuario")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private  final UserService userService;



    @Operation(
            summary = "Criar novo Usuario",
            description = "Cria um usuario informando nome, e-mail e senha"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO body){

            return ResponseEntity.ok(userService.registerUser(body));
    }

    @Operation(
            summary = "Login de Usuario",
            description = "Acesso do usuario no sistema informando e-mail e senha"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Acesso Permitido"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos / Acesso negado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {

        var authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                );

        authenticationManager.authenticate(authenticationToken);
        // 👆 se chegou aqui, login é válido

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String accessToken = tokenService.generateToken(user);
        String refreshToken = refreshTokenService.create(user);

        return ResponseEntity.ok(
                new AuthResponseDTO(
                        user.getName(),
                        accessToken,
                        refreshToken
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody RefreshRequestDTO request) {

        User user = refreshTokenService.validate(request.refreshToken());

        String newAccessToken = tokenService.generateToken(user);

        return ResponseEntity.ok(new TokenResponseDTO(newAccessToken));
    }


}
