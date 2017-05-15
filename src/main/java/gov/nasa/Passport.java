package gov.nasa;

import com.googlecode.objectify.Key;

import java.security.Principal;

public class Passport implements Principal {

    private final Key<?> userKey;

    public Passport(Key<?> userKey) {
        this.userKey = userKey;
    }

    public Key<?> getUserKey() {
        return userKey;
    }

    @Override
    public String getName() {
        return getUserKey().toWebSafeString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Passport{");
        sb.append("userKey=").append(userKey);
        sb.append('}');
        return sb.toString();
    }
}
