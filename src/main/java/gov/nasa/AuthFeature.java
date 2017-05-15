package gov.nasa;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

public class AuthFeature implements DynamicFeature {

    private final Bouncer filter;

    public AuthFeature(Bouncer filter) {
        this.filter = filter;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if(!resourceInfo.getResourceMethod().isAnnotationPresent(NoAuth.class)){
            context.register(filter);
        }
    }
}
