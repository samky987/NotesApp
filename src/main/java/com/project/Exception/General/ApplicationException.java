package com.project.Exception.General;

public abstract class ApplicationException extends Exception
{
    protected ApplicationException(String message)
    {
        super(message);
    }
}
