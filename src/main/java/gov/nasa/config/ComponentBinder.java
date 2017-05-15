package gov.nasa.config;

import gov.nasa.AuthData;
import gov.nasa.AuthDataValueFactoryProvider;
import gov.nasa.Bouncer;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Singleton;

public class ComponentBinder extends AbstractBinder{
    @Override
    protected void configure() {
        bind(Bouncer.class).to(Bouncer.class);
        bind(AuthDataValueFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
        bind(AuthDataValueFactoryProvider.InjectionResolver.class).to(new TypeLiteral<InjectionResolver<AuthData>>() {})
                .in(Singleton.class);
    }
}
