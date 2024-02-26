package com.project.Exception.Note;

import com.project.Exception.General.ApplicationException;

public abstract class NoteServiceException extends ApplicationException
{
    protected NoteServiceException (String message)
    {
        super(message);
    }
}
