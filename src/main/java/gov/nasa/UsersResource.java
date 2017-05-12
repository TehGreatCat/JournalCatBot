package gov.nasa;

import gov.nasa.data.UsersRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;

public class UsersResource {

    @Inject
    UsersRepository database;

    @GET

}
