package com.seplag.api.controller;

import com.seplag.api.dto.*;
import com.seplag.api.domain.user.*;
import com.seplag.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints responsáveis pelo Registro e Login de usuario")
public class AuthController {

    private final UserService userService;


    //---------------------------------------Listar Usuário---------------------------//
    @Operation(
            summary = "Listar Usuários",
            description = "Retorna usuarios"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })

    @GetMapping("/users")
    public List<UserResponseDTO> listUsers() {
        return ResponseEntity.ok(userService.listUsers()).getBody();
    }


    //-----------------------------Registrar Usuario---------------------//
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
    public ResponseEntity<UserResponseDTO> register(
            @Parameter(description = "Nome", example = "João")
            @RequestParam(required = false) String name,

            @Parameter(description = "E-mail", example = "joao@test.com")
            @RequestParam(required = false) String email,

            @Parameter(description = "Senha", example = "Test@2026")
            @RequestParam(required = false) String password
    ){
            UserRequestDTO dto = new UserRequestDTO(name, email, password);
            return ResponseEntity.ok(userService.registerUser(dto));
    }


    //------------------------------Login Usuario-------------------------------------//
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
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body) {

        return ResponseEntity.ok(userService.login(body));
    }

    //--------------------------Excluir Usuário---------------------//
    @Operation(
            summary = "Excluir Usuário",
            description = "Remove um usuario pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário excluído"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable UUID id
        )
    {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "Álbum excluído com sucesso."));
    }

    //------------------------------------Atualizar Usuário---------------------//
    @Operation(
            summary = "Atualizar álbum",
            description = "Atualiza os campos do álbum (nome, ano ou imagem)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum atualizado"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado")
    })
    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID do álbum", required = true)
            @PathVariable UUID id,

            @Parameter(description = "Novo nome do usuário", example = "João")
            @RequestParam(required = false) String name,

            @Parameter(description = "Novo email do usuario", example = "joao@test.com")
            @RequestParam(required = false) String email,

            @Parameter(description = "Nova senha do usuario", example = "Test@2026")
            @RequestParam(required = false) String password
    ){
        UserRequestDTO dto = new UserRequestDTO(name, email, password);

        return ResponseEntity.ok(userService.updateUser(id, dto));
    }


//    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody RefreshRequestDTO request) {
//
//        User user = refreshTokenService.validate(request.refreshToken());
//
//        String newAccessToken = tokenService.generateToken(user);
//
//        return ResponseEntity.ok(new TokenResponseDTO(newAccessToken));
//    }


}
