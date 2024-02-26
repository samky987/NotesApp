package com.project.Exception.User;

import com.project.Exception.General.ApplicationException;

public abstract class UserServiceException extends ApplicationException
{
    protected UserServiceException(String message)
    {
        super(message);
    }
}
