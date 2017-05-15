package gov.nasa;

import gov.nasa.data.UsersRepository;
import gov.nasa.domain.User;

import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;

public class Bouncer implements ContainerRequestFilter {

    private final UsersRepository usersDatabase;

    @Inject
    public Bouncer(UsersRepository usersDatabase) {
        this.usersDatabase = usersDatabase;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if(authHeader == null){
            throw new ForbiddenException("no auth token");
        }
        final User user;
        final String token = authHeader.substring(authHeader.lastIndexOf(' ') + 1);
        try {
            user = usersDatabase.getUserByToken(token);
        } catch (UsersRepository.UserNotFoundException e) {
            throw new ForbiddenException("invalid user");
        }
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Passport(user.getKey());
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        });

    }
}
