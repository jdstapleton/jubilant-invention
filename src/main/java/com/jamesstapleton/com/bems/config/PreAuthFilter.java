package com.jamesstapleton.com.bems.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * From AbstractPreAuthenticatedProcessingFilter:
 * Base class for processing filters that handle pre-authenticated authentication
 * requests, where it is assumed that the principal has already been authenticated
 * by an external system.
 */
public class PreAuthFilter extends AbstractPreAuthenticatedProcessingFilter {
    private static final Map<String, User> USERS =
            List.of(
                    new User("user", "password", auths("ROLE_USER")),
                    new User("admin", "password", auths("ROLE_ADMIN"))
            ).stream().collect(Collectors.toMap(User::getUsername, Function.identity()));

    private static Collection<GrantedAuthority> auths(String... allAuths) {
        return Stream.of(allAuths)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public static PreAuthFilter createFilter() {
        PreAuthFilter filter = new PreAuthFilter();
        // authentication manager takes in an authentication and returns a fully
        // filled out one (including granted authorities)
        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            var user = USERS.get(principal);
            if (user == null) {
                throw new BadCredentialsException("Unknown user.");
            }
            authentication.setAuthenticated(true);

            return new PreAuthenticatedAuthenticationToken(user, principal, user.getAuthorities());
        });

        return filter;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("X-REMOTE-USER");
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}
