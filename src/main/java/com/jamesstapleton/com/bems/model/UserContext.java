package com.jamesstapleton.com.bems.model;

import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
@Model
public interface UserContext {
    static UserContext of(Set<String> authorizations) {
        return ImmutableUserContext.of(authorizations);
    }

    @Value.Parameter
    Set<String> getAuthorizations();
}
