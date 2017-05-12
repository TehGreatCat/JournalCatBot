package gov.nasa.config;

import gov.nasa.data.Datastore.NotesDatastore;
import gov.nasa.data.Datastore.UsersDatastore;
import gov.nasa.data.NotesRepository;
import gov.nasa.data.UsersRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class StorageBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(NotesDatastore.class).to(NotesRepository.class);
        bind(UsersDatastore.class).to(UsersRepository.class);
    }
}
