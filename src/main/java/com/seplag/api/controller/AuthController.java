package com.seplag.api.controller;

import com.seplag.api.dto.*;
import com.seplag.api.domain.user.*;
import com.seplag.api.security.RefreshTokenService;
import com.seplag.api.security.TokenService;
import com.seplag.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Auth",
        description = "Endpoints responsáveis pelo registro, autenticação e gerenciamento de usuários"
)
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    // ----------------------------- LISTAR USUÁRIOS ----------------------------- //
    @Operation(
            summary = "Listar usuários",
            description = "Retorna a lista de usuários cadastrados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> listUsers() {

        return ResponseEntity.ok(
                userService.listUsers()
        );
    }

    // ----------------------------- REGISTRAR USUÁRIO ----------------------------- //
    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um usuário informando nome, e-mail e senha.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponseDTO> register(
            @RequestBody UserRequestDTO dto
    ) {

        return ResponseEntity
                .status(201)
                .body(userService.registerUser(dto));
    }

    // ----------------------------- LOGIN ----------------------------- //
    @Operation(
            summary = "Login de usuário",
            description = "Autentica o usuário informando e-mail e senha.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO dto
    ) {

        return ResponseEntity.ok(
                userService.login(dto)
        );
    }

    // ----------------------------- ATUALIZAR USUÁRIO ----------------------------- //
    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados do usuário.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRequestDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping(value = "/user/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponseDTO> updateUser(

            @Parameter(
                    description = "ID do usuário",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,

            @RequestBody UserRequestDTO dto
    ) {

        return ResponseEntity.ok(
                userService.updateUser(id, dto)
        );
    }

    // ----------------------------- EXCLUIR USUÁRIO ----------------------------- //
    @Operation(
            summary = "Excluir usuário",
            description = "Remove um usuário pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(

            @Parameter(
                    description = "ID do usuário",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id
    ) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------------------- REFRESH TOKEN ----------------------------- //
    @Operation(
            summary = "Renovar token de acesso",
            description = "Gera um novo token de acesso a partir de um refresh token válido.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequestDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token gerado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Refresh token inválido ou expirado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping(value = "/refresh", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TokenResponseDTO> refresh(

            @RequestBody RefreshTokenRequestDTO request
    ) {

        User user = refreshTokenService.validate(request.refreshtoken());
        String newAccessToken = tokenService.generateToken(user);

        return ResponseEntity.ok(
                new TokenResponseDTO(newAccessToken)
        );
    }
}

