package com.project.Exception.User;

public class InvalidJWTTokenException extends UserServiceException
{
    public InvalidJWTTokenException(String message)
    {
        super(message);
    }
}
