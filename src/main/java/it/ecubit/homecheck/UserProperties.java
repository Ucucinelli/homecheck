package it.ecubit.homecheck;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix="user")
public class UserProperties {
	

		private @Getter @Setter String domain;


	}