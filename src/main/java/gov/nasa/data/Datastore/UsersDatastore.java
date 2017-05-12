package gov.nasa.data.Datastore;

import gov.nasa.data.UsersRepository;
import gov.nasa.domain.User;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class UsersDatastore implements UsersRepository {
    @Override
    public User getUserByToken(String token) throws UserNotFoundException {
        return ofy().load().type(User.class).filter("token", token).first().now();
    }

    @Override
    public User createUser(String token) {
        User user = new User(token);
        ofy().save().entity(user).now(); //почему здесь синхронно, и в чем разница
                                         // между синхронной и асинхронной загрузкой
       return user;
    }
}
