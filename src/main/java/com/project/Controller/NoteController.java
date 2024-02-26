package com.project.Controller;

import com.project.Constant.Status;
import com.project.DTO.Note.NoteCreationRequestDTO;
import com.project.DTO.Note.NoteCreationResponseDTO;
import com.project.DTO.Note.NoteRetrievalResponseDTO;
import com.project.Exception.Note.InvalidNoteIdException;
import com.project.Exception.User.InvalidJWTTokenException;
import com.project.Response.ResponseWithData;
import com.project.Service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
@Slf4j
public class NoteController
{
	private final NoteService noteService;

	@GetMapping("/{id}")
	public ResponseEntity<ResponseWithData<NoteRetrievalResponseDTO>> getNote(@PathVariable Long id, @RequestHeader("Authorization") String token)
																				throws InvalidNoteIdException, InvalidJWTTokenException
	{
		log.debug("NoteController.getNote execution started");

		NoteRetrievalResponseDTO note = noteService.getNote(id, token);
		ResponseWithData<NoteRetrievalResponseDTO> response = ResponseWithData.<NoteRetrievalResponseDTO>builder().
				status(Status.SUCCESS).data(note).build();

		log.debug("NoteController.getNote execution ended");

		return ResponseEntity.ok(response);
	}
	@PostMapping
	public ResponseEntity<ResponseWithData<NoteCreationResponseDTO>> createNote(@RequestBody @Valid NoteCreationRequestDTO noteCreationRequestDTO,
										   										@RequestHeader("Authorization") String token) throws InvalidJWTTokenException
	{
		log.debug("NoteController.createNote execution started");

		NoteCreationResponseDTO noteCreationResponseDTO = noteService.createNote(noteCreationRequestDTO, token);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(noteCreationResponseDTO.getId())
				.toUri();

		ResponseWithData<NoteCreationResponseDTO> response = ResponseWithData.<NoteCreationResponseDTO>builder()
				.status(Status.SUCCESS)
				.data(noteCreationResponseDTO)
				.build();

		log.info("NoteController.createNote created a note in this location {}", location.toString());
		log.debug("NoteController.createNote execution ended");

		return ResponseEntity.created(location).body(response);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteNote(@PathVariable Long id, @RequestHeader("Authorization") String token) throws InvalidJWTTokenException
	{
		log.debug("NoteController.deleteNote execution started");

		noteService.deleteNote(id, token);

		log.debug("NoteController.deleteNote execution ended");

		return ResponseEntity.noContent().build();
	}
}
