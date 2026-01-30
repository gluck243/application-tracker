package com.gluck.jobtracker.service;

import com.gluck.jobtracker.repository.UserEntity;
import com.gluck.jobtracker.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String safeUsername = username;

        UserEntity userEntity = repository.findByUsername(safeUsername);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User by " + safeUsername + " not found");
        }

        return User.withUsername(userEntity.getUsername())
            .password(userEntity.getPassword())
            .authorities(Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole())))
            .build();
    }
}

