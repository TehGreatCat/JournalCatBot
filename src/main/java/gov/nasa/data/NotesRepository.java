package gov.nasa.data;

import com.googlecode.objectify.Key;
import gov.nasa.domain.Note;

import java.awt.*;

public interface NotesRepository {

    Iterable<Note> getNotes(Key<?> userKey);

    String createNote(Key<?> userKey, String text, String timestamp, String image);

    Note getNote (String id) throws NoteNotFoundException;

    Iterable<Note> getNoteByDate (String timestamp, Key<?> userKey);

    void deleteNote(String id) throws NoteNotFoundException;

    String updateNote(String id, String text, String timestamp, String image) throws NoteNotFoundException;

    class NoteNotFoundException extends Exception {}
}
