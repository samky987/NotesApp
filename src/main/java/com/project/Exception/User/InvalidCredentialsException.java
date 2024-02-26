package com.project.Exception.User;

public class InvalidCredentialsException extends UserServiceException
{
    public InvalidCredentialsException(String message)
    {
        super(message);
    }
}
