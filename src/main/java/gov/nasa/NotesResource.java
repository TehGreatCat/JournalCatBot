package gov.nasa;

import com.google.gson.JsonObject;
import com.googlecode.objectify.Result;
import gov.nasa.data.NotesRepository;
import gov.nasa.domain.Note;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Path("/notes")
public class NotesResource {

    @Inject
    private NotesRepository notesRepository;


    @GET
    @Path("/{noteId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Note getNote(@PathParam("noteId") String id){
        try {
            return notesRepository.getNote(id);
        } catch (NotesRepository.NoteNotFoundException e) {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("/time/{timestamp}")
    @Produces(MediaType.APPLICATION_JSON)
    public Iterable<Note> getNotesByDate(@PathParam("timestamp") String timestamp, @AuthData Passport passport) {
        return notesRepository.getNoteByDate(timestamp, passport.getUserKey());
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createNote(String json, @AuthData Passport passport){
        JSONObject noteinfo = new JSONObject(json);
        String name = noteinfo.get("name").toString();
        String image = noteinfo.get("imageLink").toString();
        String timestamp = noteinfo.get("timestamp").toString();
        Note note = new Note(name, image, timestamp);
        String noteID = notesRepository.createNote(passport.getUserKey(), name, timestamp, image);
        URI uri = buildNoteUri(noteID);
        return Response.created(uri).build();
    }

    //@PUT

    @DELETE
    @Path("/{noteId}")
    public Response deleteNote(@PathParam("noteId") String id){
        try {
            notesRepository.deleteNote(id);
        } catch (NotesRepository.NoteNotFoundException e) {
            throw new NotFoundException();
        }
        return Response.ok().build();
    }

    private URI buildNoteUri(String noteId) {
        return UriBuilder
                .fromResource(NotesResource.class)
                .path(noteId)
                .build();
    }

}


