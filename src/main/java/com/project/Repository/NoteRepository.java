package com.project.Repository;

import com.project.Model.Note;
import com.project.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NoteRepository extends CrudRepository<Note,Long>
{
    Optional<Note> findByIdAndUser(Long id, User user);
    void deleteByIdAndUser(Long noteId, User user);
}
