package com.project.Controller;

import com.project.Constant.Status;
import com.project.DTO.User.UserAuthenticationResponseDTO;
import com.project.DTO.User.UserAuthenticationRequestDTO;
import com.project.DTO.User.RegisterUserRequestDTO;
import com.project.Exception.User.InvalidCredentialsException;
import com.project.Exception.User.UserAlreadyExistException;
import com.project.Response.ResponseWithData;
import com.project.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/public/users")
@RequiredArgsConstructor
@Slf4j
public class UserController
{
    private final UserService userService;
    @PostMapping("/session")
    public ResponseEntity<ResponseWithData<UserAuthenticationResponseDTO>> authenticateUser
            (@Valid @RequestBody UserAuthenticationRequestDTO userAuthenticationRequestDTO) throws InvalidCredentialsException
    {
        log.debug("UserController.authenticateUser execution started");

        UserAuthenticationResponseDTO userAuthenticationResponseDTO = userService.authenticateUser(userAuthenticationRequestDTO);
        ResponseWithData<UserAuthenticationResponseDTO> response = ResponseWithData.<UserAuthenticationResponseDTO>builder()
                .status(Status.SUCCESS)
                .data(userAuthenticationResponseDTO)
                .build();

        log.debug("UserController.authenticateUser execution ended");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterUserRequestDTO registerUserRequestDTO) throws UserAlreadyExistException
    {
        log.debug("UserController.registerUser execution started");

        Long resourceID = userService.registerUser(registerUserRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resourceID)
                .toUri();

        log.info("UserController.registerUser created a user with the ID {}", resourceID);
        log.debug("UserController.registerUser execution ended");

        return ResponseEntity.created(location).build();
    }
}

