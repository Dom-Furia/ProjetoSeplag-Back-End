package com.seplag.api.controller;

import com.seplag.api.domain.user.*;
import com.seplag.api.repositories.UserRepository;
import com.seplag.api.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints responsáveis pelo Registro e Login de usuario")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;



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
    public ResponseEntity<MessageResponseDTO> register(@RequestBody RegisterRequestDTO body){

        Optional<User> user = this.userRepository.findByEmail(body.email());

        if (user.isEmpty()){
            User newUser = new User();
            newUser.setName(body.name());
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            this.userRepository.save(newUser);

            return ResponseEntity.ok(new MessageResponseDTO("Usúario Cadastrado com sucesso"));
        }

        return ResponseEntity.badRequest().build();
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
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body) {

        User user = userRepository.findByEmail(body.email())
                .orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDTO("Usuário não encontrado"));
        }

        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDTO("Email ou senha inválidos"));
        }

        String token = tokenService.generateToken(user);
        return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
    }
}
