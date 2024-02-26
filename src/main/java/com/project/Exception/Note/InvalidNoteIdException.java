package com.project.Exception.Note;

public class InvalidNoteIdException extends NoteServiceException
{
    public InvalidNoteIdException(String message)
    {
        super(message);
    }
}
