package it.ecubit.homecheck.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.ecubit.pse.mongo.entities.PSERole;
import it.ecubit.pse.mongo.entities.PSEUser;
import it.ecubit.pse.mongo.repositories.UserRepository;
import it.ecubit.homecheck.config.MongoUser;
import it.ecubit.homecheck.web.dto.UserRegistrationDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PSEUser findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public PSEUser save(UserRegistrationDto registration){
        PSEUser user = new PSEUser();
		/*
		 * user.setFirstName(registration.getFirstName());
		 * user.setLastName(registration.getLastName());
		 * user.setEmail(registration.getEmail());
		 * user.setPassword(passwordEncoder.encode(registration.getPassword()));
		 * user.setRoles(Arrays.asList(new Role("ROLE_USER")));
		 */
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PSEUser user = userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
		/* return new MongoUser(user); */
		
		  return new
		  org.springframework.security.core.userdetails.User(user.getEmail(),
		  user.getPassword(), mapRolesToAuthorities(user.getRuoli()));
		 
    }

	
	  private Collection<? extends GrantedAuthority>
	  mapRolesToAuthorities(Collection<PSERole> roles){ return roles.stream()
	  .map(role -> new SimpleGrantedAuthority(role.getRuolo()))
	  .collect(Collectors.toList()); }
	 
}
