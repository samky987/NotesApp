package com.project.Security;

import java.util.Collection;
import com.project.Model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails
{
    private static final long serialVersionUID = 1L;
    private final String username;
    @JsonIgnore
    private final String password;
    public UserDetailsImpl(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    public static UserDetailsImpl build(User user)
    {
        return new UserDetailsImpl(
                user.getUsername(),
                user.getPassword());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}