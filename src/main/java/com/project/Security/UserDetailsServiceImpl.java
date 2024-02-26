package com.project.Security;


import com.project.Model.User;
import com.project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("UserDetailsServiceImpl.loadUserByUsername username {} not found ", username);
                    return new UsernameNotFoundException("Username Not Found");
                });
        return UserDetailsImpl.build(user);
    }
}