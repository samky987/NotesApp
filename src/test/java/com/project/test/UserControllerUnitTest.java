package com.project.test;

import com.project.Constant.Status;
import com.project.Controller.UserController;
import com.project.DTO.User.RegisterUserRequestDTO;
import com.project.DTO.User.UserAuthenticationRequestDTO;
import com.project.DTO.User.UserAuthenticationResponseDTO;
import com.project.Exception.User.InvalidCredentialsException;
import com.project.Exception.User.UserAlreadyExistException;
import com.project.Response.ResponseWithData;
import com.project.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest
{

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup()
    {
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }
    @Test
    void testAuthenticateUser_ValidInput_ReturnResponseEntityWithStatusCode200() throws InvalidCredentialsException
    {
        UserAuthenticationRequestDTO requestDTO = new UserAuthenticationRequestDTO("username","password");
        UserAuthenticationResponseDTO mockResponse = new UserAuthenticationResponseDTO("token");

        when(userService.authenticateUser(requestDTO)).thenReturn(mockResponse);

        ResponseEntity<ResponseWithData<UserAuthenticationResponseDTO>> responseEntity = userController.authenticateUser(requestDTO);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(Status.SUCCESS, responseEntity.getBody().getStatus());
        assertEquals(mockResponse, responseEntity.getBody().getData());
    }

    @Test
    void testRegisterUser_ValidInput_ReturnResponseEntityWithStatusCode201() throws UserAlreadyExistException
    {
        long id = 1L;
        RegisterUserRequestDTO requestDTO = new RegisterUserRequestDTO("name","username","password");

        when(userService.registerUser(requestDTO)).thenReturn(id);

        ResponseEntity<Void> responseEntity = userController.registerUser(requestDTO);
        URI expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        assertNotNull(responseEntity);
        assertEquals(201, responseEntity.getStatusCode().value());
        assertEquals(expectedLocation, responseEntity.getHeaders().getLocation());
    }
    @Test
    void testRegisterUser_InvalidInput_ThrowsUserAlreadyExistException() throws UserAlreadyExistException
    {
        long id = 1L;
        RegisterUserRequestDTO requestDTO = new RegisterUserRequestDTO("name","username","password");

        when(userService.registerUser(requestDTO)).thenThrow(UserAlreadyExistException.class);

        assertThrows(UserAlreadyExistException.class, () ->
        {
            userController.registerUser(requestDTO);
        });
    }
}
