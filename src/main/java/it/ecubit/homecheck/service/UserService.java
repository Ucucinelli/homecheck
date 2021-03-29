package it.ecubit.homecheck.service;

import org.springframework.security.core.userdetails.UserDetailsService;


import it.ecubit.homecheck.web.dto.UserRegistrationDto;
import it.ecubit.pse.mongo.entities.PSEUser;

public interface UserService extends UserDetailsService {

    PSEUser findByEmail(String email);

    PSEUser save(UserRegistrationDto registration);
}
