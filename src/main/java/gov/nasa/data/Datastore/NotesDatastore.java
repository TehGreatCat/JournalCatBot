package gov.nasa.data.Datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import gov.nasa.data.NotesRepository;
import gov.nasa.domain.Note;

import java.awt.*;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class NotesDatastore implements NotesRepository {
    @Override
    public Iterable<Note> getNotes(Key<?> userKey) {
        return ofy().load().type(Note.class).ancestor(userKey).list();
    }

    @Override
    public String createNote(Key<?> userKey, String text, String timestamp, String image) {
        Note note = new Note(text, image, timestamp);
        note.setCreator(userKey);
        Result<Key<Note>> result = ofy().save().entity(note);
        return result.now().toWebSafeString();
    }

    @Override
    public Note getNote(String id) throws NoteNotFoundException {
        Note note =(Note) ofy().load().key(Key.create(id)).now();
        if (note == null){
            throw new NoteNotFoundException();
        }
        return note;
    }

    @Override
    public Iterable<Note> getNoteByDate(String timestamp, Key<?> userKey) {
        return ofy().load().type(Note.class).ancestor(userKey).filter("timestamp",timestamp).list();
    }

    @Override
    public void deleteNote(String id) throws NoteNotFoundException {
        ofy().delete().entity(getNote(id)); // Как оформить result?
    }

    @Override
    public String updateNote(String id, String text, String timestamp, String image) throws NoteNotFoundException {
        Note note = new Note(text, image, timestamp);
        ofy().save().toEntity(note);        //?????????????????
        return null;
    }
}
