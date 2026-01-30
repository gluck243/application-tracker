package com.gluck.jobtracker.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    private final AuthenticationContext context;

    public SecurityService(AuthenticationContext context) {
        this.context = context;
    }

    public Optional<UserDetails> getAuthenticatedUser() {
        return context.getAuthenticatedUser(UserDetails.class);
    }

    public void logout() {
        context.logout();
    }
}

