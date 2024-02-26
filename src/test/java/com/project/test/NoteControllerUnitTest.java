package com.project.test;

import com.project.Constant.Status;
import com.project.Controller.NoteController;
import com.project.DTO.Note.NoteCreationRequestDTO;
import com.project.DTO.Note.NoteCreationResponseDTO;
import com.project.DTO.Note.NoteRetrievalResponseDTO;
import com.project.Exception.Note.InvalidNoteIdException;
import com.project.Exception.User.InvalidJWTTokenException;
import com.project.Response.ResponseWithData;
import com.project.Service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteControllerUnitTest
{

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    @BeforeEach
    public void setup()
    {
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }
    @Test
    void testGetNote_ValidInput_ReturnResponseEntityWithStatusCode200() throws InvalidNoteIdException, InvalidJWTTokenException
    {
        Long noteId = 1L;
        String token = "token";
        NoteRetrievalResponseDTO noteRetrievalResponseDTO = new NoteRetrievalResponseDTO();

        when(noteService.getNote(noteId, token)).thenReturn(noteRetrievalResponseDTO);


        ResponseEntity<ResponseWithData<NoteRetrievalResponseDTO>> responseEntity = noteController.getNote(noteId, token);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(noteRetrievalResponseDTO, responseEntity.getBody().getData());
        assertEquals(Status.SUCCESS, responseEntity.getBody().getStatus());

        verify(noteService, times(1)).getNote(noteId, token);
    }

    @Test
    void testCreateNote_ValidInput_ReturnResponseEntityWithStatusCode201() throws InvalidJWTTokenException
    {
        String token = "token";
        NoteCreationRequestDTO requestDTO = new NoteCreationRequestDTO();
        NoteCreationResponseDTO noteCreationResponseDTO = new NoteCreationResponseDTO();

        when(noteService.createNote(requestDTO, token)).thenReturn(noteCreationResponseDTO);

        ResponseEntity<ResponseWithData<NoteCreationResponseDTO>> responseEntity = noteController.createNote(requestDTO, token);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(noteCreationResponseDTO, responseEntity.getBody().getData());

        verify(noteService, times(1)).createNote(requestDTO, token);
    }

    @Test
    void testDeleteNote_ValidInput_ReturnResponseEntityWithStatusCode204() throws InvalidJWTTokenException
    {
        Long noteId = 1L;
        String token = "validToken";

        ResponseEntity<Void> responseEntity = noteController.deleteNote(noteId, token);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        verify(noteService, times(1)).deleteNote(noteId, token);
    }
    @Test
    void testGetNote_InvalidInput_ThrowInvalidNoteIdException() throws InvalidNoteIdException, InvalidJWTTokenException
    {
        Long noteId = 1L;
        String token = "token";

        when(noteService.getNote(noteId, token)).thenThrow(InvalidNoteIdException.class);

        assertThrows(InvalidNoteIdException.class,() -> {
            noteController.getNote(noteId,token);
        });

        verify(noteService, times(1)).getNote(noteId, token);
    }
}
