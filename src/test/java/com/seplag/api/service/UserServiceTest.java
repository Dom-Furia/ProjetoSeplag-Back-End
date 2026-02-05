package com.seplag.api.service;

import com.seplag.api.domain.user.User;
import com.seplag.api.dto.LoginRequestDTO;
import com.seplag.api.dto.LoginResponseDTO;
import com.seplag.api.dto.UserRequestDTO;
import com.seplag.api.dto.UserResponseDTO;
import com.seplag.api.repositories.UserRepository;
import com.seplag.api.security.RefreshTokenService;
import com.seplag.api.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setName("Julio");
        user.setEmail("julio@email.com");
        user.setPassword("senha-criptografada");
    }

    /* ---------------- REGISTER ---------------- */

    @Test
    @DisplayName("Deve registrar usuário com sucesso")
    void deveRegistrarUsuarioComSucesso() {
        UserRequestDTO dto = new UserRequestDTO(
                "Julio",
                "julio@email.com",
                "123456"
        );

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.password())).thenReturn("senha-criptografada");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.registerUser(dto);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Julio");
        assertThat(response.email()).isEqualTo("julio@email.com");

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar erro quando email já estiver cadastrado")
    void deveLancarErroQuandoEmailJaCadastrado() {
        UserRequestDTO dto = new UserRequestDTO(
                "Julio",
                "julio@email.com",
                "123456"
        );

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.registerUser(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário já cadastrado.");
    }

    /* ---------------- LIST ---------------- */

    @Test
    @DisplayName("Deve listar usuários")
    void deveListarUsuarios() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> users = userService.listUsers();

        assertThat(users).hasSize(1);
        assertThat(users.getFirst().email()).isEqualTo(user.getEmail());
    }

    /* ---------------- UPDATE ---------------- */

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        UserRequestDTO dto = new UserRequestDTO(
                "Novo Nome",
                "novo@email.com",
                "novaSenha"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(dto.password())).thenReturn("senhaNova");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.updateUser(userId, dto);

        assertThat(response.name()).isEqualTo("Novo Nome");
        assertThat(response.email()).isEqualTo("novo@email.com");
    }

    @Test
    @DisplayName("Deve lançar erro ao atualizar usuário inexistente")
    void deveLancarErroAoAtualizarUsuarioInexistente() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(userId, new UserRequestDTO(null, null, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário não encontrado");
    }

    /* ---------------- DELETE ---------------- */

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Deve lançar erro ao deletar usuário inexistente")
    void deveLancarErroAoDeletarUsuarioInexistente() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário não encontrado");
    }

    /* ---------------- LOGIN ---------------- */

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void deveRealizarLoginComSucesso() {
        LoginRequestDTO loginDTO = new LoginRequestDTO(
                "julio@email.com",
                "123456"
        );

        when(userRepository.findByEmail(loginDTO.email())).thenReturn(Optional.of(user));
        when(tokenService.generateToken(user)).thenReturn("access-token");
        when(refreshTokenService.create(user)).thenReturn("refresh-token");

        LoginResponseDTO response = userService.login(loginDTO);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        assertThat(response.nome()).isEqualTo("Julio");
    }

    @Test
    @DisplayName("Deve lançar erro quando usuário não existir no login")
    void deveLancarErroQuandoUsuarioNaoExisteNoLogin() {
        LoginRequestDTO loginDTO = new LoginRequestDTO(
                "naoexiste@email.com",
                "123456"
        );

        when(userRepository.findByEmail(loginDTO.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(loginDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não encontrado");
    }
}
