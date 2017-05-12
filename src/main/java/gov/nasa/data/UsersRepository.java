package gov.nasa.data;

import gov.nasa.domain.User;

public interface UsersRepository {

    User getUserByToken(String token) throws UserNotFoundException;

    User createUser(String token);

    class UserNotFoundException extends Exception {}
}
