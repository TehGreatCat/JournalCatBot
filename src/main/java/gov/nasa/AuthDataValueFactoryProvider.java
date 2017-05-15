package gov.nasa;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ForbiddenException;
import java.security.Principal;

public class AuthDataValueFactoryProvider extends AbstractValueFactoryProvider {

    @Inject
    public AuthDataValueFactoryProvider(MultivaluedParameterExtractorProvider mpep, ServiceLocator locator, Parameter.Source... compatibleSources) {
        super(mpep, locator, Parameter.Source.UNKNOWN);
    }

    @Override
    protected Factory<?> createValueFactory(Parameter parameter) {
        if(isNoAuthDataAnnotation(parameter) || isNotBraceletClass(parameter)) {
            return null;
        }
        return new AbstractContainerRequestValueFactory<Passport>() {
            @Override
            public Passport provide() {
                Principal passport = getContainerRequest().getSecurityContext().getUserPrincipal();
                if(passport == null){
                    throw new ForbiddenException("unauthenticated request");
                }
                return (Passport) passport;
            }
        };
    }

    private static boolean isNoAuthDataAnnotation(Parameter parameter) {
        return !parameter.isAnnotationPresent(AuthData.class);
    }

    private static boolean isNotBraceletClass(Parameter parameter) {
        return !parameter.getRawType().equals(Bouncer.class);
    }

    @Singleton
    public static class InjectionResolver extends ParamInjectionResolver<AuthData>{
        public InjectionResolver() {
            super(AuthDataValueFactoryProvider.class);}
    }

}
