package ute.tri.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import ute.tri.repository.UserInfoRepository;
import ute.tri.service.UserInfoService;

@Configuration
@EnableWebSecurity
	public class SecurityConfig {
	
	@Autowired
	UserInfoRepository repository;
	// authentication
	@Bean
	UserDetailsService userDetailsService() {

	return new UserInfoService(repository);
	}
	@Bean
	// authentication
	public UserDetailsService userDetailsService(PasswordEncoder encoder) {
	UserDetails admin = User.withUsername("tri")
		.password(encoder.encode("123"))
		.roles("ADMIN")
			.build();
	UserDetails user = User.withUsername("user")
		.password(encoder.encode("123"))
		.roles("USER")
		.build();
	return new InMemoryUserDetailsManager(admin, user);
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	AuthenticationProvider authenticationProvider(){
	DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
	authenticationProvider.setUserDetailsService(userDetailsService());
	authenticationProvider.setPasswordEncoder(passwordEncoder());
	return authenticationProvider;
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
	.authorizeHttpRequests(auth -> auth
	.requestMatchers("/").permitAll()
	.requestMatchers("/customer/ ** ").authenticated()
	//.anyRequest().authenticated()

	)
	.formLogin(Customizer.withDefaults())
	.build();

	}

}

