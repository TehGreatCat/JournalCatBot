package gov.nasa;

import com.googlecode.objectify.Key;
import gov.nasa.data.UsersRepository;
import gov.nasa.domain.User;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("/users")
public class UsersResource {

    @Inject
    UsersRepository database;

    @GET    // поиск юзера
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserByToken(@QueryParam("token") String token) throws UsersRepository.UserNotFoundException {
        return database.getUserByToken(token);
    }

    @NoAuth
    @POST   //добавление юзера
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@QueryParam("token") String token){
        User user = database.createUser(token);
        Key<?> userKey = Key.create(User.class, user.getId());
        return Response.created(UriBuilder.fromResource(User.class).path(userKey.toWebSafeString())
                .build()).entity(user).build();
    }





}
