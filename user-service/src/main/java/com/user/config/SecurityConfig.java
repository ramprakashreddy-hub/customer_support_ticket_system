package com.user.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private UserDetailsService userService;
	private JWTAuthFilter jwtAuthFIlter;
	private CustomAccessDeniedHandler accessDeniedHandler;

	public SecurityConfig(UserDetailsService userService,
			JWTAuthFilter jwtAuthFIlter, CustomAccessDeniedHandler accessDeniedHandler) {
		this.userService = userService;
		this.jwtAuthFIlter = jwtAuthFIlter;
		this.accessDeniedHandler =accessDeniedHandler;
	}

		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
			httpSecurity.csrf(AbstractHttpConfigurer::disable)
					.authorizeHttpRequests(request -> request.requestMatchers("/users/**").permitAll()
							.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
							.requestMatchers("/admin/**")
							.hasAuthority("ROLE_ADMIN").anyRequest().authenticated())
					.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.authenticationProvider(authenticationProvider())
					.addFilterBefore(jwtAuthFIlter, UsernamePasswordAuthenticationFilter.class);
			return httpSecurity.exceptionHandling(exception -> exception
        .accessDeniedHandler(accessDeniedHandler)
    ).build();
		}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}