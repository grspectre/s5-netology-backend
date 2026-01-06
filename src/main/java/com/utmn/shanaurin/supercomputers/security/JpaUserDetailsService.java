package com.utmn.shanaurin.supercomputers.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JpaUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final PasswordEncoder encoder;

    public JpaUserDetailsService(PersonRepository personRepository, PasswordEncoder encoder) {
        this.personRepository = personRepository;
        this.encoder = encoder;
    }

    // src/main/java/.../security/JpaUserDetailsService.java
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByEmailIgnoreCase(username);
        if (person != null) {
            return User.withUsername(person.getEmail())
                    .accountLocked(!person.isEnabled())
                    .password(person.getPassword()) // <-- НЕ encode здесь
                    .roles(person.getRole())
                    .build();
        }
        throw new UsernameNotFoundException("Пользователь не найден: " + username);
    }
}
