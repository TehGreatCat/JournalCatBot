package gov.nasa.data;

import com.googlecode.objectify.Key;
import gov.nasa.domain.Note;

import java.awt.*;

public interface NotesRepository {

    Iterable<Note> getNotes(Key<?> userKey);

    String createNote(Key<?> userKey, String text, String timestamp, Image image);

    Note getNote (String id) throws NoteNotFoundException;

    void deleteNote(String id) throws NoteNotFoundException;

    String updateNote(String id, String text, String timestamp, Image image) throws NoteNotFoundException;

    class NoteNotFoundException extends Exception {}
}
