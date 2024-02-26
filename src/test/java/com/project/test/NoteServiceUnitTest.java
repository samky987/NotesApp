package com.project.test;


import com.project.DTO.Note.NoteCreationRequestDTO;
import com.project.DTO.Note.NoteCreationResponseDTO;
import com.project.DTO.Note.NoteRetrievalResponseDTO;
import com.project.Exception.Note.InvalidNoteIdException;
import com.project.Exception.User.InvalidJWTTokenException;
import com.project.Model.Note;
import com.project.Model.User;
import com.project.Repository.NoteRepository;
import com.project.Service.NoteService;
import com.project.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceUnitTest
{

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private NoteService noteService;


    @Test
    void testGetNote_ValidInput_ReturnsNote() throws InvalidJWTTokenException, InvalidNoteIdException
    {
        User user = new User();
        Note note = new Note();
        Optional<Note> optionalNote =Optional.of(note);
        String token = "token";
        NoteRetrievalResponseDTO noteRetrievalResponseDTO = new NoteRetrievalResponseDTO();

        when(userService.getUserFromToken(token)).thenReturn(user);
        when(noteRepository.findByIdAndUser(1L, user)).thenReturn(optionalNote);
        when(modelMapper.map(note, NoteRetrievalResponseDTO.class)).thenReturn(noteRetrievalResponseDTO);

        NoteRetrievalResponseDTO getNoteResponse = noteService.getNote(1L,token);


        verify(userService).getUserFromToken(token);
        verify(noteRepository).findByIdAndUser(1L,user);
        assertEquals(getNoteResponse, noteRetrievalResponseDTO);
        assertEquals(getNoteResponse.getTitle(), noteRetrievalResponseDTO.getTitle());
        assertEquals(getNoteResponse.getDescription(), noteRetrievalResponseDTO.getDescription());
    }
    @Test
    void testGetNote_InvalidID_ThrowsInvalidNoteIdException() throws InvalidJWTTokenException, InvalidNoteIdException
    {
        User user = new User();
        Optional<Note> optionalNote =Optional.empty();
        String token = "token";

        when(userService.getUserFromToken(token)).thenReturn(user);
        when(noteRepository.findByIdAndUser(1L, user)).thenReturn(optionalNote);

        assertThrows(InvalidNoteIdException.class, () ->
        {
            noteService.getNote(1L,token);
        });
    }
    @Test
    void testDeleteNote_ValidInput_deleteByIdAndUserAndGetUserFromTokenCalled() throws InvalidJWTTokenException
    {
        long id = 1;
        User user = new User();
        String token = "";

        when(userService.getUserFromToken(token)).thenReturn(user);

        noteService.deleteNote(id, token);

        verify(noteRepository).deleteByIdAndUser(id, user);
        verify(userService).getUserFromToken(token);
    }
    @Test
   void testCreateNote_ValidInput_ReturnNoteCreationResponseDTO () throws InvalidJWTTokenException
   {
       String title = "title";
       String description = "description";
       long id = 1L;
       String token = "token";
       Note note = new Note();
       User user = new User();


       NoteCreationRequestDTO noteCreationRequestDTO = new NoteCreationRequestDTO(title, description);
       NoteCreationResponseDTO noteCreationResponseDTO = new NoteCreationResponseDTO(id ,title,description);

       when(modelMapper.map(noteCreationRequestDTO,Note.class)).thenReturn(note);
       when(userService.getUserFromToken(token)).thenReturn(user);
       when(noteRepository.save(note)).thenReturn(note);
       when(modelMapper.map(note, NoteCreationResponseDTO.class)).thenReturn(noteCreationResponseDTO);

       NoteCreationResponseDTO response = noteService.createNote(noteCreationRequestDTO,token);


       verify(userService).getUserFromToken(token);
       verify(noteRepository).save(note);

       assertEquals(response,noteCreationResponseDTO);
       assertEquals(response.getTitle(),noteCreationRequestDTO.getTitle());
       assertEquals(response.getDescription(),noteCreationRequestDTO.getDescription());
   }
}

