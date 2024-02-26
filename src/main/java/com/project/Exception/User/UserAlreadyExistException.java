package com.project.Exception.User;

public class UserAlreadyExistException extends UserServiceException
{
    public UserAlreadyExistException(String message)
    {
        super(message);
    }
}
