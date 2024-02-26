package com.project.test;

import com.project.DTO.User.RegisterUserRequestDTO;
import com.project.DTO.User.UserAuthenticationRequestDTO;
import com.project.DTO.User.UserAuthenticationResponseDTO;
import com.project.Exception.User.InvalidCredentialsException;
import com.project.Exception.User.InvalidJWTTokenException;
import com.project.Exception.User.UserAlreadyExistException;
import com.project.Model.User;
import com.project.Repository.UserRepository;
import com.project.Security.JwtUtils;
import com.project.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest
{

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testAuthenticateUser_ValidCredentials_ReturnsUserAuthenticationResponseDTO() throws InvalidCredentialsException
    {
        UserAuthenticationRequestDTO requestDTO = new UserAuthenticationRequestDTO("validUsername", "validPassword");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("validToken");

        UserAuthenticationResponseDTO responseDTO = userService.authenticateUser(requestDTO);

        assertNotNull(responseDTO);
        assertFalse(responseDTO.getJwt().isEmpty());
        verify(authenticationManager).authenticate(any());
        verify(jwtUtils).generateJwtToken(authentication);
    }

    @Test
    void testAuthenticateUser_InvalidCredentials_ThrowsInvalidCredentialsException() throws AuthenticationException
    {
        UserAuthenticationRequestDTO requestDTO = new UserAuthenticationRequestDTO("invalidUsername", "invalidPassword");
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("invalid credentials") {});

        assertThrows(InvalidCredentialsException.class, () -> userService.authenticateUser(requestDTO));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void testRegisterUser_UserDoesntExist_ReturnsPersistedUserId() throws UserAlreadyExistException
    {
        String name = "Adam";
        String username = "user";
        String password = "password";
        String encodedPassword = "password";
        long persistedUserID = 1L;
        RegisterUserRequestDTO requestDTO = new RegisterUserRequestDTO(name, username, password);
        User persistedUser = new User(name, username, encodedPassword);
        persistedUser.setId(persistedUserID);

        when(encoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.save(any())).thenReturn(persistedUser);

        Long userId = userService.registerUser(requestDTO);

        assertEquals(persistedUserID, userId);
        verify(userRepository, times(1)).existsByUsername(username);
        verify(encoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any());
    }
    @Test
    void testRegisterUser_UserExist_ThrowsUserAlreadyExistException()
    {
        String name = "Adam";
        String username = "user";
        String password = "password";
        String encodedPassword = "password";
        RegisterUserRequestDTO requestDTO = new RegisterUserRequestDTO(name, username, password);

        when(encoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.existsByUsername(username)).thenReturn(true);


        assertThrows(UserAlreadyExistException.class, () ->
        {
            userService.registerUser(requestDTO);
        });
    }
    @Test
    void testUserFromToken_ValidJWTToken_ReturnUser() throws InvalidJWTTokenException
    {
        String name = "Adam";
        String username = "username";
        String password = "password";
        User user = new User(name,username,password);
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByUsername(any())).thenReturn(optionalUser);

        User result = userService.getUserFromToken(any());

        verify(userRepository).findByUsername(any());
        assertEquals(user, result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getNotes(), result.getNotes());
        assertEquals(user.getFullName(), result.getFullName());
        assertEquals(user.getPassword(), result.getPassword());
    }
    @Test
    void testUserFromToken_InvalidJWTToken_ThrowsInvalidJWTTokenException()
    {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(InvalidJWTTokenException.class, () ->
        {
            userService.getUserFromToken(any());
        });
    }


}
