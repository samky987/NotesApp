package com.project.Service;

import com.project.DTO.User.UserAuthenticationResponseDTO;
import com.project.DTO.User.RegisterUserRequestDTO;
import com.project.Exception.User.InvalidCredentialsException;
import com.project.Exception.User.UserAlreadyExistException;
import com.project.Exception.User.InvalidJWTTokenException;
import com.project.Security.JwtUtils;
import com.project.DTO.User.UserAuthenticationRequestDTO;
import com.project.Model.User;
import com.project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService
{
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;

    public UserAuthenticationResponseDTO authenticateUser(UserAuthenticationRequestDTO userAuthenticationRequestDTO) throws InvalidCredentialsException
    {
        try
        {
            log.debug("UserService.authenticateUser execution started");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userAuthenticationRequestDTO.getUsername(), userAuthenticationRequestDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserAuthenticationResponseDTO userAuthenticationResponseDTO = new UserAuthenticationResponseDTO(jwt);

            log.debug("UserService.authenticateUser execution ended");
            return userAuthenticationResponseDTO;

        }
        catch (AuthenticationException exception)
        {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
    public Long registerUser(RegisterUserRequestDTO registerUserRequestDTO) throws UserAlreadyExistException
    {
        log.debug("UserService.registerUser execution started");

        User user = new User(registerUserRequestDTO.getFullName(), registerUserRequestDTO.getUsername(),
                encoder.encode(registerUserRequestDTO.getPassword()));
        Long persistedUserID;

        if (!userRepository.existsByUsername(user.getUsername()))
             persistedUserID =  userRepository.save(user).getId();
        else
        {
            log.error("UserService.registerUser User with this credentials already Exist");
            throw new UserAlreadyExistException("User with these credentials already Exist");
        }

        log.info("UserService.registerUser persisted user ID is {}", persistedUserID);
        log.debug("UserService.registerUser execution ended");

        return persistedUserID;
    }
    public User getUserFromToken (String token) throws InvalidJWTTokenException
    {
        log.debug("UserService.getUserFromToken execution started");

        Optional<User> optionalUser = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
        User user =  optionalUser.orElseThrow(() -> {
            log.error("UserService.getUserFromToken No user found that corresponds to the JWT token");
            return new InvalidJWTTokenException("User not found");
        });

        log.info("UserService.getUserFromToken user found with the ID {}" , user.getId());
        log.debug("UserService.getUserFromToken execution ended");
        return user;

    }
}
