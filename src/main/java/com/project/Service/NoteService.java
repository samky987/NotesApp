package com.project.Service;

import com.project.DTO.Note.NoteCreationResponseDTO;
import com.project.DTO.Note.NoteRetrievalResponseDTO;
import com.project.Exception.Note.InvalidNoteIdException;
import com.project.Exception.User.InvalidJWTTokenException;
import com.project.Repository.NoteRepository;

import com.project.DTO.Note.NoteCreationRequestDTO;
import com.project.Model.Note;
import com.project.Model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NoteService
{
    private final ModelMapper modelMapper;
    private final NoteRepository noteRepository;
    private final UserService userService;

    //The token + note ID are cache keys to make the keys unique for every user
    @Cacheable(value = "notes", key = "#noteID + '-' + #token")
    public NoteRetrievalResponseDTO getNote(Long noteID, String token) throws InvalidNoteIdException, InvalidJWTTokenException
    {
        log.debug("NoteService.getNote execution started");

        User user = userService.getUserFromToken(token);
        Optional<Note> optionalNote = noteRepository.findByIdAndUser(noteID, user);

        Note note = optionalNote.orElseThrow(() -> {
            log.error("NoteService.getNote No note found with this ID");
            return new InvalidNoteIdException("No note exist with this ID");
        });

        log.info("NoteService.getNote fetched the note with ID {}" , note.getId());
        log.debug("NoteService.getNote execution ended");
        return this.modelMapper.map(note, NoteRetrievalResponseDTO.class);
    }
    @CacheEvict(value = "notes", key = "#noteID + '-' + #token")
    public void deleteNote(Long noteID, String token) throws InvalidJWTTokenException
    {
        log.debug("NoteService.deleteNote execution started");

        noteRepository.deleteByIdAndUser(noteID, userService.getUserFromToken(token));

        log.debug("NoteService.getNote execution ended");
    }
    public NoteCreationResponseDTO createNote(NoteCreationRequestDTO noteCreationRequestDTO, String token) throws InvalidJWTTokenException
    {
        log.debug("NoteService.createNote execution started");

        Note note = this.modelMapper.map(noteCreationRequestDTO, Note.class);
        note.setUser(userService.getUserFromToken(token));
        Note persistedNote = noteRepository.save(note);

        log.info("NoteService.createNote persisted the note with this ID {}" , persistedNote.getId());
        log.debug("NoteService.createNote execution ended");

        return modelMapper.map(persistedNote, NoteCreationResponseDTO.class);
    }
}
