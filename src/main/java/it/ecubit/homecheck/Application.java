package it.ecubit.homecheck;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.CacheControl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

import it.ecubit.pse.mongo.encoders.SHA256PasswordEncoder;
import it.ecubit.pse.mongo.inheritance.InheritanceAwareMongoRepositoryFactoryBean;
import it.ecubit.pse.mongo.inheritance.InheritanceAwareSimpleMongoRepository;

@SpringBootApplication(scanBasePackages = "it.ecubit")
@EntityScan(basePackages = "it.ecubit.pse.mongo.entities")
@EnableAspectJAutoProxy
@EnableMongoRepositories(basePackages = "it.ecubit.pse.mongo.repositories", repositoryBaseClass = InheritanceAwareSimpleMongoRepository.class, repositoryFactoryBeanClass = InheritanceAwareMongoRepositoryFactoryBean.class)
public class Application extends SpringBootServletInitializer implements WebMvcConfigurer {

	@Autowired
	BuildProperties buildProperties;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);  
	}

	/*
	 * @Bean public PasswordEncoder passwordEncoder() { return new
	 * SHA256PasswordEncoder(); }
	 */

	/*
	 * @Bean public OpenAPI pseRestOpenAPI() { return new OpenAPI() .info(new
	 * Info().title("PSE (Ecubit Sanitary Platform) REST API").version("0.0.1")
	 * .description("REST services for the sanitary platform (patients management and tele-monitoring)"
	 * ).termsOfService("http://swagger.io/terms/") .license(new
	 * License().name("Commercial").url("https://www.ecubit.it/"))) .components(new
	 * Components().addSecuritySchemes("jwt-bearer-token", new
	 * SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat
	 * ("JWT"))); }
	 */

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
		registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
