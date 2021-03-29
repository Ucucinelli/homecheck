package it.ecubit.homecheck.config;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import it.ecubit.pse.mongo.entities.PSEUser;
import lombok.Getter;
import lombok.NonNull;

public class MongoUser extends User {

	private static final long serialVersionUID = -8771220658922945185L;

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Getter
	private Map<String, String> additionalDetails = new HashMap<>();

	public MongoUser(@NonNull PSEUser mongoUserEntity) {
		super(
			mongoUserEntity.getEmail(), 
			mongoUserEntity.getPassword(), 
			true, 
			true, 
			true, 
			true, 
			mongoUserEntity.getRuoli().stream().map(pseRole -> new SimpleGrantedAuthority(pseRole.getRuolo())).collect(Collectors.toList()));
		String id = mongoUserEntity.getId();
		String firstName = mongoUserEntity.getNome();
		String surname = mongoUserEntity.getCognome();
		String organization = mongoUserEntity.getOrganizzazione();
		String email = mongoUserEntity.getEmail();
		String encodedPassword = mongoUserEntity.getPassword();
		String roles = mongoUserEntity.getRuoli().stream().map(pseRole -> pseRole.getRuolo()).collect(Collectors.joining(","));
		String creationDateAsString = mongoUserEntity.getDataCreazione().format(formatter);
		String lastModifiedDateAsString = mongoUserEntity.getDataUltimaModifica().format(formatter);
		Map<String, String> additionalDetails = new HashMap<>();
		additionalDetails.put("id", id);
		additionalDetails.put("firstName", firstName);
		additionalDetails.put("surname", surname);
		additionalDetails.put("organization", organization);
		additionalDetails.put("email", email);
		additionalDetails.put("password", encodedPassword);
		additionalDetails.put("roles", roles);
		additionalDetails.put("creationDate", creationDateAsString);
		additionalDetails.put("lastModifiedDate", lastModifiedDateAsString);
		this.additionalDetails = additionalDetails;
	}

	public MongoUser(@NonNull String username, @NonNull String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, @NonNull Collection<? extends GrantedAuthority> authorities, 
			@NonNull Map<String, String> additionalDetails) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.additionalDetails = additionalDetails;
	}

	public MongoUser(@NonNull String username, @NonNull String password, @NonNull Collection<? extends GrantedAuthority> authorities, 
			@NonNull Map<String, String> additionalDetails) {
		super(username, password, authorities);
		this.additionalDetails = additionalDetails;
	}

	public String getDetailInfo(String key) {
		return this.additionalDetails.get(key);
	}

}
