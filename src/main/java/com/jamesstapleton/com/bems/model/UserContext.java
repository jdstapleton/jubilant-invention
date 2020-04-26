package com.jamesstapleton.com.bems.model;

import java.util.Set;

public class UserContext {
    private final Set<String> authorizations;

    public UserContext(Set<String> authorizations) {
        this.authorizations = authorizations;
    }

    public Set<String> getAuthorizations() {
        return authorizations;
    }
}
